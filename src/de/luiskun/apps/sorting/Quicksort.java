package de.luiskun.apps.sorting;

public class Quicksort extends SortingAlgorithm {
    @Override
    protected void sort() {
        quickSort( 0, am-1);
    }

    private int partition(int low, int high) {
        int pivot = list[high];

        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {

            if (list[j] < pivot) {

                i++;
                swap(i, j);
            }
            if (!sorting) {
                return -1;
            }
        }
        swap(i + 1, high);
        return (i + 1);
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            if (!sorting) {
                return;
            }
            quickSort(low, pi - 1);
            if (!sorting) {
                return;
            }
            quickSort(pi + 1, high);
        }
    }
}
