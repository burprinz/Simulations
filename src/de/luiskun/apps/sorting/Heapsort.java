package de.luiskun.apps.sorting;

public class Heapsort extends SortingAlgorithm{
    @Override
    protected void sort() {
        BuildMaxHeap();
        for(int i = am -1; i > 0; i--) {
            swap(i, 0);
            versickern(0, i);
            if (!sorting) {
                return;
            }
        }
    }

    private void BuildMaxHeap() {
        for(int i = (am / 2) - 1; i >= 0 ; i--) {
            versickern(i, am);
            if (!sorting) {
                return;
            }
        }
    }
    private void versickern(int i, int j) {

        while(i <= (j / 2) - 1) {

            int kindIndex = ((i+1) * 2) - 1;
            if(kindIndex + 1 <= j -1) {
                if(list[kindIndex] < list[kindIndex+1]) {
                    kindIndex++;
                }
            }
            if(list[i] < list[kindIndex]) {
                swap(i,kindIndex);
                i = kindIndex;
            } else break;

            if (!sorting) {
                return;
            }
        }
    }

}
