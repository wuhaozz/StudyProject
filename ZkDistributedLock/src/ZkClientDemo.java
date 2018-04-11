import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.io.InputStream;

public class ZkClientDemo {

    public static void main(String[] args) throws InterruptedException, IOException {
        ZkClient zkClient = new ZkClient("localhost:2181", 5000, 5000, new MyZkSerializer());

        zkClient.subscribeDataChanges("/super", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("变更节点为：" + s + "，变更数据为：" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("删除的节点为：" + s);
            }

        });

//        zkClient.createPersistent("/super", "123");
//        Thread.sleep(3000);
//        zkClient.writeData("/super", "456", -1);
//        Thread.sleep(1000);
//        zkClient.createPersistent("/super/c1", "789"); //不会被监控到
//        zkClient.deleteRecursive("/super");

        System.in.read();


    }

}
