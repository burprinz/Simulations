package de.luiskun.apps.sorting;

public class Cyclesort extends SortingAlgorithm {
    @Override
    protected void sort() {

        for (int cycle_start = 0; cycle_start <= am - 2; cycle_start++) {
            if (!sorting) {
                return;
            }

            int item = list[cycle_start];

            int pos = cycle_start;
            for (int i = cycle_start + 1; i < am; i++)
                if (list[i] < item)
                    pos++;

            if (pos == cycle_start)
                continue;

            while (item == list[pos])
                pos += 1;

            if (pos != cycle_start) {
                int temp = item;
                item = list[pos];
                list[pos] = temp;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                }
            }

            while (pos != cycle_start) {
                pos = cycle_start;

                for (int i = cycle_start + 1; i < am; i++)
                    if (list[i] < item)
                        pos += 1;

                while (item == list[pos])
                    pos += 1;

                if (item != list[pos]) {
                    int temp = item;
                    item = list[pos];
                    list[pos] = temp;
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                    }
                }
                if (!sorting) {
                    return;
                }
            }
        }
    }
}
