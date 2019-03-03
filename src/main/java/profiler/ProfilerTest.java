package profiler;

public class ProfilerTest {


    public static void main(String[] args) throws InterruptedException {
        Profiler.reset();
        Profiler.init();

        Profiler.enter("1");

        Profiler.enter("1.1");
        Thread.sleep(10);
        Profiler.enter("1.1.1");
        Thread.sleep(5);
        Profiler.exit(); // exit for 1.1.1
        Profiler.exit(); // exit for 1.1

        Profiler.enter("1.2");
        Thread.sleep(20);
        Profiler.exit(); // exit for 1.2

        Profiler.enter("1.3");
        Thread.sleep(30);
        Profiler.exit(); // exit for 1.3

        Profiler.exit(); // exit for 1

        Profiler.enter("2");
        Thread.sleep(40);
        Profiler.exit(); // exit for 2
        System.out.println(Profiler.dump());
        Profiler.reset();

    }

}
