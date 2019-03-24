package util;

import org.apache.commons.lang.math.RandomUtils;

public class ThreadUtils {

    private ThreadUtils() {
    }

    public static void randomSleep() {
        try {
            Thread.sleep(RandomUtils.nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
