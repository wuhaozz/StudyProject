package algorithm.primary.class01;

/**
 * 选择排序
 */
public class Code04_SelectionSort {

    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 3, 2, 4, 9, 6, 6, 8, 4, 1, 2};
        printArray(arr);
        selectSort(arr);
        printArray(arr);
    }

    public static void selectSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        // 0 ~ n-1
        // 1 ~ n-1
        // 2 ~ n-1
        for (int i = 0; i < n; i++) {
            // i ~ n-1
            int minValueIndex = i; // i
            for (int j = i + 1; j < n; j++) {
                minValueIndex = arr[j] < arr[minValueIndex] ? j : minValueIndex;
            }
            swap(arr, i, minValueIndex);
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    public static void printArray(int[] arr) {
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

}
