package algorithm.primary.class02;

/**
 * 前缀和
 */
public class Code01_PreSum {

    public static void main(String[] args) {
        int[] arr = {7, 5, 4, -1, 6, -3, 9, 4, -5};
        RangeSum1 rs1 = new RangeSum1(arr);
        RangeSum2 rs2 = new RangeSum2(arr);

        int left = 3;
        int right = 5;

        System.out.println(rs1.rangeSum(left, right));
        System.out.println(rs2.rangeSum(left, right));

    }

    public static class RangeSum1 {

        private final int[] arr;

        public RangeSum1(int[] array) {
            arr = array;
        }

        public int rangeSum(int left, int right) {
            int sum = 0;
            for (int i = left; i <= right; i++) {
                sum += arr[i];
            }
            return sum;
        }

    }

    public static class RangeSum2 {

        private final int[] preSum;

        public RangeSum2(int[] array) {
            int n = array.length;
            preSum = new int[n];
            preSum[0] = array[0];
            for (int i = 1; i < n; i++) {
                preSum[i] = preSum[i - 1] + array[i];
            }
        }

        public int rangeSum(int left, int right) {
            return left == 0 ? preSum[right] : preSum[right] - preSum[left - 1];
        }

    }


}
