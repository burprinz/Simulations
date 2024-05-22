package de.luiskun.apps.sorting;

public class Pancakesort extends SortingAlgorithm{


    @Override
    protected void sort() {
        for (int curr_size = am; curr_size > 1;
             --curr_size) {
            int mi = findMax(curr_size);

            if (mi != curr_size-1) {
                flip(mi);
                flip(curr_size-1);
                if (!sorting) {
                    return;
                }
            }
        }
    }

    private void flip(int i) {
        int start = 0;
        while (start < i)
        {
            swap(start, i);
            start++;
            i--;
            if (!sorting) {
                return;
            }
        }
    }

    private int findMax(int n) {
        int mi, i;
        for (mi = 0, i = 0; i < n; ++i)
            if (list[i] > list[mi])
                mi = i;
        return mi;
    }

}
