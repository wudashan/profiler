package demo.repository;

import org.springframework.stereotype.Service;
import util.ThreadUtils;

@Service
public class DataRepository {

    public void update() {
        ThreadUtils.randomSleep();
    }

    public void query() {
        ThreadUtils.randomSleep();
    }

}
