package de.luiskun.apps.sorting;

public class Bubblesort extends SortingAlgorithm{
    @Override
    protected void sort() {
        boolean found = true;

        for (int i = 0; i < am && found; i++) {
            found = false;

            for (int y = 0; y < am-1; y++) {
                if(list[y] > list[y + 1]) {
                    swap(y, y+1);
                    found = true;
                }
                if (!sorting) {
                    return;
                }
            }
        }
    }
}
