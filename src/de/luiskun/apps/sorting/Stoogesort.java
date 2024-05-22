package de.luiskun.apps.sorting;

public class Stoogesort extends SortingAlgorithm{
    @Override
    protected void sort() {
        stoogesort(0, am-1);
    }

    private void stoogesort(int l, int h) {
        if (l >= h)
            return;

        if (list[l] > list[h]) {
            swap(l, h);
        }

        if (!sorting) {
            return;
        }

        if (h - l + 1 > 2) {
            int t = (h - l + 1) / 3;

            stoogesort(l, h - t);
            if (!sorting) {
                return;
            }

            stoogesort(l + t, h);
            if (!sorting) {
                return;
            }

            stoogesort(l, h - t);
        }
    }
}
