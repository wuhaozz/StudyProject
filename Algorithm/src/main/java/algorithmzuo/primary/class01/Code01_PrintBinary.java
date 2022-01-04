package algorithmzuo.primary.class01;

/**
 * 打印二进制
 */
public class Code01_PrintBinary {

    public static void main(String[] args) {
        int a = 154564564;
        print(a);
        print(Integer.MIN_VALUE);
        print(Integer.MAX_VALUE);
    }

    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

}
