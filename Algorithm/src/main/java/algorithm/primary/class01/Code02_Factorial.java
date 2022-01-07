package algorithm.primary.class01;

/**
 * 阶乘
 * <p>
 * 给定一个参数N,返回1!+2!+3!+...+N！的结果
 * N!=1!+2!+...+N!
 */
public class Code02_Factorial {

    public static void main(String[] args) {
        int n = 10;
        long f1 = f1(n);
        long f2 = f2(n);
        System.out.println(f1);
        System.out.println(f2);
    }

    public static long f1(int n) {
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans = ans + factorial(i);
        }
        return ans;
    }

    public static long factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static long f2(int n) {
        long ans = 0;
        long cur = 1;
        for (int i = 1; i <= n; i++) {
            cur = cur * i;
            ans = ans + cur;
        }
        return ans;
    }

}
