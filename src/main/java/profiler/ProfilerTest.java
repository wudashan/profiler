package profiler;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 性能埋点工具测试类
 */
public class ProfilerTest {


    public static void main(String[] args) throws InterruptedException {

        Profiler.reset();
        Profiler.init("Main主流程");

        try {
            callRpcInterface();
            sendMessage();
            saveData2Db();
        } finally {
            Profiler.exit();
            System.out.println(Profiler.dump());
            Profiler.reset();
        }

    }

    private static void saveData2Db() throws InterruptedException {
        Profiler.enter("模拟保存数据到数据库");
        Thread.sleep(RandomUtils.nextInt(100));
        Profiler.exit();
    }

    private static void sendMessage() throws InterruptedException {
        Profiler.enter("模拟发送消息");
        Thread.sleep(RandomUtils.nextInt(100));
        Profiler.exit();
    }

    private static void callRpcInterface() throws InterruptedException {
        Profiler.enter("模拟调用RPC接口");
        Thread.sleep(RandomUtils.nextInt(100));
        Profiler.exit();
    }

}
