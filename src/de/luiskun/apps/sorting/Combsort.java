package de.luiskun.apps.sorting;

public class Combsort extends SortingAlgorithm {
    @Override
    protected void sort() {
        int gap = am;

        boolean swapped = true;

        while (gap != 1 || swapped == true)
        {
            gap = getNextGap(gap);

            swapped = false;

            for (int i=0; i<am-gap; i++)
            {
                if (list[i] > list[i+gap])
                {
                    swap(i, i+gap);
                    swapped = true;
                }
                if (!sorting) {
                    return;
                }
            }
        }
    }

    private int getNextGap(int gap)
    {
        gap = (gap*10)/13;
        if (gap < 1)
            return 1;
        return gap;
    }
}
