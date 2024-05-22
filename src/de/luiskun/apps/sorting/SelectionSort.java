package de.luiskun.apps.sorting;

public class SelectionSort extends SortingAlgorithm{
    @Override
    protected void sort() {

        for (int i = 0; i < am - 1; i++) {
            for (int j = i + 1; j < am; j++) {
                if (list[i] > list[j]) {
                    swap(i, j);
                }
                if (!sorting) {
                    return;
                }
            }
        }

    }
}
