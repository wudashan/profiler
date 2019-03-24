package demo;

import demo.facade.DemoFacade;
import demo.message.MessageComponent;
import demo.service.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import profiler.Profiler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfilerApplicationTests {

    @Autowired
    private DataService dataService;

    @Autowired
    private MessageComponent messageComponent;

    @Autowired
    private DemoFacade demoFacade;

    @Test
    public void profilerTest() {

        // 重置调用树
        Profiler.reset();
        // 初始化调用树
        Profiler.init("测试用例");

        try {
            // 业务代码
            doBusiness();
        } finally {
            // 结束调用树
            Profiler.exit();
            // 打印调用树结果
            System.out.println(Profiler.dump());
            // 重置调用树
            Profiler.reset();
        }

    }

    private void doBusiness() {

        // 数据库操作
        dataService.queryAndUpdate();

        // 消息发送
        messageComponent.sendMessage();

        // RPC调用
        demoFacade.invoke();

    }

}
