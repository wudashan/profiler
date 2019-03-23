package profiler;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 性能埋点工具测试类
 */
public class ProfilerTest {


    public static void main(String[] args) throws InterruptedException {

        // 重置调用树
        Profiler.reset();
        // 初始化调用树
        Profiler.init("Main主流程");

        try {
            // PRC调用
            callRpcInterface();
            // 数据库操作
            saveData2Db();
            // 消息发送
            sendMessage();
        } finally {
            // 结束调用树
            Profiler.exit();
            // 打印调用树结果
            System.out.println(Profiler.dump());
            // 重置调用树
            Profiler.reset();
        }

    }

    private static void saveData2Db() throws InterruptedException {
        Profiler.enter("模拟保存数据到数据库");
        Thread.sleep(RandomUtils.nextInt(100));
        doSomeThing();
        doSomeThing();
        Profiler.exit();
    }

    private static void sendMessage() throws InterruptedException {
        Profiler.enter("模拟发送消息");
        Thread.sleep(RandomUtils.nextInt(100));
        doSomeThing();
        doSomeThing();
        Profiler.exit();
    }

    private static void callRpcInterface() throws InterruptedException {
        Profiler.enter("模拟调用RPC接口");
        Thread.sleep(RandomUtils.nextInt(100));
        Profiler.exit();
    }

    private static void doSomeThing() throws InterruptedException {
        Profiler.enter("模拟操作");
        Thread.sleep(RandomUtils.nextInt(100));
        Profiler.exit();
    }

}
