package demo.service;

import demo.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.ThreadUtils;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public void queryAndUpdate() {
        dataRepository.query();
        ThreadUtils.randomSleep();
        dataRepository.update();
    }

}
