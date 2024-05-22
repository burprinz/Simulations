package de.luiskun.apps.sorting;

public class Cocktailsort extends SortingAlgorithm {
    @Override
    protected void sort() {
        boolean swapped = true;
        int start = 0;
        int end = am;

        while (swapped == true) {
            swapped = false;

            for (int i = start; i < end - 1; ++i) {
                if (list[i] > list[i + 1]) {
                    swap(i, i+1);
                    swapped = true;
                    if (!sorting) {
                        return;
                    }
                }
                if (!sorting) {
                    return;
                }
            }

            if (swapped == false)
                break;

            swapped = false;

            end = end - 1;

            for (int i = end - 1; i >= start; i--)
            {
                if (list[i] > list[i + 1])
                {
                    swap(i, i+1);
                    swapped = true;
                    if (!sorting) {
                        return;
                    }
                }
                if (!sorting) {
                    return;
                }
            }

            start = start + 1;
        }
    }
}
