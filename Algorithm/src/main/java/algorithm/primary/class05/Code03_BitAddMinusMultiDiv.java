package algorithm.primary.class05;

// 测试链接：https://leetcode.com/problems/divide-two-integers
public class Code03_BitAddMinusMultiDiv {

    // 加法
    public static int add(int a, int b) {
        int sum = a;
        while (b != 0) { // 一直加到没有进位 => 无需在做加法了
            // 加法就是拆分成2部分：不带进位的加法值 和 进位值
            sum = a ^ b; // 不带进位的加法值
            b = (a & b) << 1; // 进位值
            a = sum;
        }
        return sum;
    }

    // 负数
    public static int negNum(int n) {
        // 负数 等同于 取反加一
        // -n = ~n + 1
        return add(~n, 1);
    }

    // 减法
    public static int minus(int a, int b) {
        // a - b 等同于 a + (-b)
        return add(a, negNum(b));
    }

    // 乘法
    public static int multi(int a, int b) {
        int res = 0;
        while (b != 0) { // b的位数没了
            if ((b & 1) != 0) { // b的当前位是1的话，做累加
                res = add(res, a);
            }
            a <<= 1; // a左移一位，为了弥补 b右移一位
            b >>>= 1; // 无符号右移，避免符号位
        }
        return res;
    }

    // 判断是否是负数
    public static boolean isNeg(int n) {
        return n < 0;
    }

    public static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a; // 干掉符号位
        int y = isNeg(b) ? negNum(b) : b; // 干掉符号位
        int res = 0;
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) { // 等价于 y << i 之后正好是x被y整数倍最大减去的值
                res |= (1 << i);
                x = minus(x, y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    // leetcode题解
    public static int divide(int a, int b) {
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
            return 1;
        } else if (b == Integer.MIN_VALUE) {
            return 0;
        } else if (a == Integer.MIN_VALUE) {
            if (b == negNum(1)) {
                return Integer.MAX_VALUE;
            } else {
                int c = div(add(a, 1), b);
                return add(c, div(minus(a, multi(c, b)), b));
            }
        } else {
            return div(a, b);
        }
    }

}