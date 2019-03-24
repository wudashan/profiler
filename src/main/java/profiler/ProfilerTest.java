package profiler;

import util.ThreadUtils;

/**
 * 性能埋点工具测试类
 */
public class ProfilerTest {


    public static void main(String[] args) {

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

    private static void saveData2Db() {
        Profiler.enter("模拟保存数据到数据库");
        ThreadUtils.randomSleep();
        doSomeThing();
        doSomeThing();
        Profiler.exit();
    }

    private static void sendMessage() {
        Profiler.enter("模拟发送消息");
        ThreadUtils.randomSleep();
        doSomeThing();
        doSomeThing();
        Profiler.exit();
    }

    private static void callRpcInterface() {
        Profiler.enter("模拟调用RPC接口");
        ThreadUtils.randomSleep();
        Profiler.exit();
    }

    private static void doSomeThing() {
        Profiler.enter("模拟操作");
        ThreadUtils.randomSleep();
        Profiler.exit();
    }

}
