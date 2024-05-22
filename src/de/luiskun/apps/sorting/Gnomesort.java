package de.luiskun.apps.sorting;

public class Gnomesort extends SortingAlgorithm{
    @Override
    protected void sort() {
        int index = 0;

        while (index < am) {
            if (index == 0)
                index++;
            if (list[index] >= list[index - 1])
                index++;
            else {
                swap(index, index-1);
                if (!sorting) {
                    return;
                }
                index--;
            }
        }
    }
}
