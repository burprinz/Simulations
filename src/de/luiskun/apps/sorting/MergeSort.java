package de.luiskun.apps.sorting;

import de.luiskun.apps.Application;

public class MergeSort extends SortingAlgorithm {
    @Override
    protected void sort() {
        sort(0, am - 1);
    }

    void merge(int l, int m, int r)
    {
        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = list[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = list[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                list[k] = L[i];
                i++;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                }
                if (!sorting) {
                    return;
                }
            }
            else {
                list[k] = R[j];
                j++;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                }
                if (!sorting) {
                    return;
                }
            }
            k++;
        }

        while (i < n1) {
            list[k] = L[i];
            i++;
            k++;
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
            }
            if (!sorting) {
                return;
            }
        }

        while (j < n2) {
            list[k] = R[j];
            j++;
            k++;
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
            }
            if (!sorting) {
                return;
            }
        }
    }

    void sort(int l, int r)
    {
        if (l < r) {
            int m = l + (r - l) / 2;
            sort(l, m);
            if (!sorting) {
                return;
            }
            sort(m + 1, r);
            if (!sorting) {
                return;
            }
            merge(l, m, r);
        }
    }
}
