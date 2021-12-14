package algorithmzuo.noob.class01;

/**
 * 插入排序
 */
public class Code05_InsertSort {

    public static void main(String[] args) {
        int[] arr = {7, 5, 1, 3, 2, 4, 9, 6, 6, 8, 4, 1, 2};
        printArray(arr);

        insertSort(arr);
        printArray(arr);

        insertSortV2(arr);
        printArray(arr);
    }

    public static void insertSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        // 0 ~ 1
        // 0 ~ 2
        // 0 ~ 3
        // 0 ~ n-1
        for (int end = 1; end < n; end++) {
            int newNumIndex = end;
            while (newNumIndex - 1 >= 0 && arr[newNumIndex - 1] > arr[newNumIndex]) {
                swap(arr, newNumIndex - 1, newNumIndex);
                newNumIndex--;
            }
        }
    }

    public static void insertSortV2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int end = 1; end < n; end++) {
            for (int pre = end - 1; pre >= 0 && arr[pre] > arr[pre + 1]; pre--) {
                swap(arr, pre, pre + 1);
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
