package algorithmzuo.primary.class01;

/**
 * 冒泡排序
 */
public class Code03_BubbleSort {

    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 3, 2, 4, 9, 6, 6, 8, 4, 1, 2};
        printArray(arr);
        bubbleSort(arr);
        printArray(arr);
    }


    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        // 0 ~ n-1
        // 0 ~ n-2
        // 0 ~ n-3
        // 0 ~ end
        for (int end = n - 1; end >= 0; end--) {
            for (int second = 1; second <= end; second++) {
                if (arr[second - 1] > arr[second]) {
                    swap(arr, second - 1, second);
                }
            }
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
