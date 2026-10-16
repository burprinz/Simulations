package de.luiskun.apps.sims;

import de.luiskun.apps.Application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fourier-Epizykel: Eine schwarze Linienzeichnung auf weißem Hintergrund wird
 * mit immer mehr rotierenden Kreisen (diskrete Fourier-Transformation) angenähert.
 */
public class FourierDrawing extends Application {

    // ---- Stellschrauben ----
    private static final String IMAGE_PATH = "res/sims/fourierdrawing/test3.png";
    private static final int MAX_PIXELS = 6000;   // max. Pixel für die Pfadsuche (Performance)
    private static final int MAX_SAMPLES = 2000;  // Anzahl Punkte für die DFT = maximale Kreisanzahl
    private static final int STEPS = 600;         // Stützpunkte der grauen Annäherungskurve
    private static final double UMDREHUNG_SEKUNDEN = 3.0; // Dauer einer vollen Umdrehung
    private static final double WACHSTUM = 1.5;   // Kreisanzahl pro Umdrehung mal diesen Faktor
    private static final int DARK = 128;          // Helligkeit unter diesem Wert = Linie

    private boolean ready = false;
    private int imgW, imgH;
    private double[] cr, ci;  // Koeffizienten (Real-/Imaginärteil), nach Größe sortiert
    private int[] fr;         // zugehörige Frequenzen
    private int total;        // Anzahl aller Kreise

    private int anzahlKreise = 2;  // aktuell verwendete Kreise (Mittelpunkt + größter Kreis)
    private double zeit = 0;       // Fortschritt der Umdrehung 0..1
    private long letzteZeit = System.nanoTime();
    private final List<double[]> spur = new ArrayList<>();
    private Path2D.Double approx = new Path2D.Double();

    public FourierDrawing() {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(IMAGE_PATH));
        } catch (IOException e) {
            System.out.println("ERROR LOADING IMAGE. PATH: " + IMAGE_PATH);
            e.printStackTrace();
            return;
        }
        if (img == null) {
            System.out.println("ERROR: Kein lesbares Bild: " + IMAGE_PATH);
            return;
        }

        imgW = img.getWidth();
        imgH = img.getHeight();

        List<int[]> path = order(thin(readBlack(img)));
        if (path.isEmpty()) {
            System.out.println("ERROR: Keine schwarzen Pixel gefunden in " + IMAGE_PATH);
            return;
        }
        computeCoefficients(path);
        anzahlKreise = Math.min(anzahlKreise, total);
        rebuildApprox();
        letzteZeit = System.nanoTime();
        ready = true;
    }

    @Override
    public void close() {
        spur.clear();
    }

    @Override
    public void update() {
        if (!ready) return;

        // zeitbasiert: unabhängig davon, wie oft update() aufgerufen wird
        long jetzt = System.nanoTime();
        double dt = Math.min((jetzt - letzteZeit) / 1e9, 0.1);
        letzteZeit = jetzt;

        zeit += dt / UMDREHUNG_SEKUNDEN;
        if (zeit >= 1) { // Umdrehung fertig -> mehr Kreise
            zeit = 0;
            spur.clear();
            anzahlKreise = Math.min(total, Math.max(anzahlKreise + 1, (int) (anzahlKreise * WACHSTUM)));
            rebuildApprox();
        }
        spur.add(tip(anzahlKreise, zeit));
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (!ready) {
            g.setColor(Color.RED);
            g.drawString("Bild konnte nicht geladen werden: " + IMAGE_PATH, 10, 20);
            g.dispose();
            return;
        }

        g.setColor(Color.DARK_GRAY);
        g.drawString("Kreise: " + anzahlKreise + " / " + total, 10, 18);

        // Zeichnung auf die Fenstergröße skalieren und zentrieren
        double s = Math.min(WIDTH / (double) imgW, HEIGHT / (double) imgH);
        g.translate((WIDTH - imgW * s) / 2, (HEIGHT - imgH * s) / 2);
        g.scale(s, s);
        float px = (float) (1 / s); // 1 Bildschirmpixel

        // Annäherung mit den aktuellen Kreisen (hellgrau)
        g.setStroke(new BasicStroke(px));
        g.setColor(new Color(190, 190, 190));
        g.draw(approx);

        // Kreise
        double x = 0, y = 0;
        for (int i = 0; i < anzahlKreise; i++) {
            double ang = 2 * Math.PI * fr[i] * zeit;
            double c = Math.cos(ang), sn = Math.sin(ang);
            double dx = cr[i] * c - ci[i] * sn;
            double dy = cr[i] * sn + ci[i] * c;
            double r = Math.hypot(cr[i], ci[i]);
            if (fr[i] != 0 && r * s > 1.5) {
                g.setColor(new Color(0, 90, 200, 90));
                g.draw(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
                g.setColor(new Color(0, 90, 200, 160));
                g.draw(new Line2D.Double(x, y, x + dx, y + dy));
            }
            x += dx;
            y += dy;
        }

        // Spur der Spitze
        if (spur.size() > 1) {
            Path2D.Double p = new Path2D.Double();
            double[] first = spur.get(0);
            p.moveTo(first[0], first[1]);
            for (double[] q : spur) p.lineTo(q[0], q[1]);
            g.setStroke(new BasicStroke(2f * px, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Color.RED);
            g.draw(p);
        }
        g.dispose();
    }

    // ---------- Fourier ----------

    /** Pfad auf n Punkte verteilen und per DFT in Koeffizienten zerlegen. */
    private void computeCoefficients(List<int[]> path) {
        int n = Math.min(path.size(), MAX_SAMPLES);
        double[] zx = new double[n], zy = new double[n];
        for (int i = 0; i < n; i++) {
            int[] p = path.get((int) ((long) i * path.size() / n));
            zx[i] = p[0];
            zy[i] = p[1];
        }

        // c_k = 1/n * sum z_j * e^(-2*pi*i*k*j/n), k = -n/2 .. n/2-1
        double[] cos = new double[n], sin = new double[n];
        for (int i = 0; i < n; i++) {
            cos[i] = Math.cos(2 * Math.PI * i / n);
            sin[i] = Math.sin(2 * Math.PI * i / n);
        }

        double[] re = new double[n], im = new double[n];
        int[] freq = new int[n];
        for (int a = 0; a < n; a++) {
            int k = a - n / 2;
            freq[a] = k;
            double sr = 0, si = 0;
            for (int j = 0; j < n; j++) {
                int idx = (int) ((((long) k * j) % n + n) % n);
                double c = cos[idx], s = -sin[idx]; // e^(-i*phi)
                sr += zx[j] * c - zy[j] * s;
                si += zx[j] * s + zy[j] * c;
            }
            re[a] = sr / n;
            im[a] = si / n;
        }

        // nach Radius sortieren (k=0 immer zuerst: das ist der Mittelpunkt)
        Integer[] ord = new Integer[n];
        for (int i = 0; i < n; i++) ord[i] = i;
        Arrays.sort(ord, (a, b) -> Double.compare(amp(re, im, freq, b), amp(re, im, freq, a)));

        total = n;
        cr = new double[n];
        ci = new double[n];
        fr = new int[n];
        for (int i = 0; i < n; i++) {
            cr[i] = re[ord[i]];
            ci[i] = im[ord[i]];
            fr[i] = freq[ord[i]];
        }
    }

    private static double amp(double[] re, double[] im, int[] freq, int i) {
        return freq[i] == 0 ? Double.MAX_VALUE : Math.hypot(re[i], im[i]);
    }

    /** Position der Spitze der Kreiskette mit k Kreisen zum Zeitpunkt t (0..1). */
    private double[] tip(int k, double t) {
        double x = 0, y = 0;
        for (int i = 0; i < k; i++) {
            double ang = 2 * Math.PI * fr[i] * t;
            double c = Math.cos(ang), s = Math.sin(ang);
            x += cr[i] * c - ci[i] * s;
            y += cr[i] * s + ci[i] * c;
        }
        return new double[]{x, y};
    }

    private void rebuildApprox() {
        approx = new Path2D.Double();
        for (int i = 0; i <= STEPS; i++) {
            double[] p = tip(anzahlKreise, (double) i / STEPS);
            if (i == 0) approx.moveTo(p[0], p[1]);
            else approx.lineTo(p[0], p[1]);
        }
    }

    // ---------- Bildverarbeitung ----------

    private static boolean[][] readBlack(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        boolean[][] a = new boolean[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = img.getRGB(x, y);
                int lum = (((rgb >> 16) & 255) + ((rgb >> 8) & 255) + (rgb & 255)) / 3;
                a[y][x] = lum < DARK;
            }
        }
        return a;
    }

    /** Zhang-Suen-Thinning: dicke Linien -> 1 Pixel breite Linien. */
    private static boolean[][] thin(boolean[][] a) {
        int h = a.length, w = a[0].length;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int pass = 0; pass < 2; pass++) {
                List<int[]> del = new ArrayList<>();
                for (int y = 1; y < h - 1; y++) {
                    for (int x = 1; x < w - 1; x++) {
                        if (!a[y][x]) continue;
                        boolean[] n = {a[y - 1][x], a[y - 1][x + 1], a[y][x + 1], a[y + 1][x + 1],
                                a[y + 1][x], a[y + 1][x - 1], a[y][x - 1], a[y - 1][x - 1]};
                        int b = 0;
                        for (boolean v : n) if (v) b++;
                        if (b < 2 || b > 6) continue;
                        int trans = 0;
                        for (int i = 0; i < 8; i++) if (!n[i] && n[(i + 1) % 8]) trans++;
                        if (trans != 1) continue;
                        boolean p2 = n[0], p4 = n[2], p6 = n[4], p8 = n[6];
                        if (pass == 0 ? (p2 && p4 && p6) || (p4 && p6 && p8)
                                : (p2 && p4 && p8) || (p2 && p6 && p8)) continue;
                        del.add(new int[]{x, y});
                    }
                }
                for (int[] d : del) { a[d[1]][d[0]] = false; changed = true; }
            }
        }
        return a;
    }

    /** Bringt die Linienpixel per "nächster Nachbar" in eine Reihenfolge (ein durchgehender Pfad). */
    private static List<int[]> order(boolean[][] a) {
        List<int[]> all = new ArrayList<>();
        for (int y = 0; y < a.length; y++)
            for (int x = 0; x < a[0].length; x++)
                if (a[y][x]) all.add(new int[]{x, y});
        if (all.isEmpty()) return all;

        if (all.size() > MAX_PIXELS) {
            int m = (all.size() + MAX_PIXELS - 1) / MAX_PIXELS;
            List<int[]> sub = new ArrayList<>();
            for (int i = 0; i < all.size(); i += m) sub.add(all.get(i));
            all = sub;
        }

        int n = all.size();
        boolean[] used = new boolean[n];
        List<int[]> out = new ArrayList<>(n);
        int cur = 0;
        used[0] = true;
        out.add(all.get(0));
        for (int i = 1; i < n; i++) {
            int[] c = all.get(cur);
            long best = Long.MAX_VALUE;
            int bi = -1;
            for (int j = 0; j < n; j++) {
                if (used[j]) continue;
                int[] p = all.get(j);
                long dx = p[0] - c[0], dy = p[1] - c[1];
                long d = dx * dx + dy * dy;
                if (d < best) { best = d; bi = j; }
            }
            used[bi] = true;
            cur = bi;
            out.add(all.get(bi));
        }
        return out;
    }
}