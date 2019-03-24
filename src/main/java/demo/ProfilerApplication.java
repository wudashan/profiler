package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations= {"classpath:application-demo.xml"})
public class ProfilerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfilerApplication.class, args);
    }

}
