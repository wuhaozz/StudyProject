public class SingletonTest {

    public static void main(String[] args) {
        Color.print();
    }

}

enum Color {
    RED(1), GREEN(2), BLUE(3);
    private int code;

    Color(int code) {
        this.code = code;
        System.out.println("调用构造函数：" + code);
    }

    public static void print() {
        System.out.println("调用枚举类静态方法");
    }


}