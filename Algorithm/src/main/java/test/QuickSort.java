package test;

import java.util.Arrays;

/**
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] a = {58, 10, 36, 79, 45, 67, 112, 5, 77, 65};
        System.out.println(Arrays.toString(a));
        sort(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }

    /**
     * 快速排序实现
     *
     * @param a
     * @param low
     * @param high
     */
    public static void sort(int[] a, int low, int high) {

        if (low > high) {
            return;
        } else {
            //如果不止一个元素，继续划分两边递归排序下去
            int postion = partition(a, low, high);
            sort(a, low, postion - 1);
            sort(a, postion + 1, high);
        }

    }

    /**
     * 把数组分割成2块，返回基准值所在的位置
     *
     * @param a
     * @param low
     * @param high
     * @return
     */
    public static int partition(int[] a, int low, int high) {
        // 取第一个值当基准值
        int base = a[low];

        // low一旦等于high，就说明左右两个指针合并到了同一位置，可以结束此轮循环。
        while (low < high) {

            //从右边开始遍历，如果比基准值大，就继续向左走
            while (low < high && a[high] >= base) {
                high--;
            }

            //上面的while循环结束时，就说明当前的a[high]的值比基准值大，应与基准值进行交换，放到基准值左边去
            if (low < high) {
                swap(a, low, high);

                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值左边)，因此左边也要同时向右移动一位
                low++;
            }

            //从左边开始遍历，如果比基准值小，就继续向右走
            while (low < high && a[low] <= base) {
                low++;
            }

            //上面的while循环结束时，就说明当前的a[low]的值比基准值大，应与基准值进行交换，放到基准值右边去
            if (low < high) {
                swap(a, low, high);

                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此左边也要同时向左移动一位
                high--;
            }

        }

        //这里返回low或者high皆可，此时的start和end都为基准值所在的位置
        return high;
    }

    /**
     * 交换数组中2个值的位置
     *
     * @param a 数组
     * @param i 第一个值的下标
     * @param j 第二个值的下标
     */
    public static void swap(int[] a, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

}
