package demo.message;

import org.springframework.stereotype.Service;
import util.ThreadUtils;

@Service
public class MessageComponent {

    public void sendMessage() {
        ThreadUtils.randomSleep();
    }

}
