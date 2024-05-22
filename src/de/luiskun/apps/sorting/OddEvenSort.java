package de.luiskun.apps.sorting;

public class OddEvenSort extends SortingAlgorithm {
    @Override
    protected void sort() {
        boolean isSorted = false;

        while (!isSorted) {
            isSorted = true;

            for (int i=1; i<=am-2; i=i+2)
            {
                if (list[i] > list[i+1]) {
                    swap(i, i+1);
                    isSorted = false;
                }
                if (!sorting) {
                    return;
                }
            }

            for (int i=0; i<=am-2; i=i+2) {
                if (list[i] > list[i+1]) {
                    swap(i, i+1);
                    isSorted = false;
                }
                if (!sorting) {
                    return;
                }
            }
        }
    }
}
