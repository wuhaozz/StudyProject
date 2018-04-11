public class Singleton {

    //私有构造函数
    private Singleton() {
    }

    //单例对象
    private volatile static Singleton instance = null;


    public static Singleton getInstance() {
        if (null == instance) {
            synchronized (Singleton.class) {
                if (null == instance) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
