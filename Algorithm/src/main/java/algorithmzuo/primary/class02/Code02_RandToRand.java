package algorithmzuo.primary.class02;

/**
 * 把 1~5等概率随机的函数f1() 改造成 1~7 等概率随机函数
 */
public class Code02_RandToRand {

    public static void main(String[] args) {
        int testTimes = 10000000;

        int count = 0;

        for (int i = 0; i < testTimes; i++) {
            if (f2() == 0) {
                count++;
            }
        }
        System.out.println((double) count / (double) testTimes);

        System.out.println("================");

        int[] counts = new int[10];
        for (int i = 0; i < testTimes; i++) {
            int num = f1();
            counts[num]++;
        }
        for (int i = 0; i < counts.length; i++) {
            System.out.println(i + "这个数，出现了" + counts[i] + "次");
        }

        System.out.println("================");

        counts = new int[10];
        for (int i = 0; i < testTimes; i++) {
            int num = g();
            counts[num]++;
        }
        for (int i = 0; i < counts.length; i++) {
            System.out.println(i + "这个数，出现了" + counts[i] + "次");
        }
    }

    // 等概率返回 1~5
    public static int f1() {
        return (int) (Math.random() * 5) + 1;
    }

    // 随机机制：只能用f1()
    // 等概率返回0和1
    public static int f2() {
        int ans = 0;
        do {
            ans = f1();
        } while (ans == 3);
        return ans < 3 ? 0 : 1;
    }


    // 得到 000 ~ 111 做到等概率
    public static int f3() {
        return (f2() << 2) + (f2() << 1) + f2();
    }

    // 得到 0 ~ 6 做到等概率
    public static int f4() {
        int ans = 0;
        do {
            ans = f3();
        } while (ans == 7);
        return ans;
    }

    public static int g() {
        return f4() + 1;
    }


    // 你只能知道，x()会以固定概率返回0和1，但是x的内容，你看不到！
    public static int x() {
        return Math.random() < 0.84 ? 0 : 1;
    }

    // 把x()改造成等概率返回0和1
    public static int y() {
        int ans = 0;
        do {
            ans = x();
        } while (ans == x());
        return ans;
    }

}
