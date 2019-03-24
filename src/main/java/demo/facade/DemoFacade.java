package demo.facade;

import org.springframework.stereotype.Service;
import util.ThreadUtils;

@Service
public class DemoFacade {

    public void invoke() {
        ThreadUtils.randomSleep();
    }

}
