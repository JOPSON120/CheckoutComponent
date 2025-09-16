package checkout.component.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "checkout.component")
public class CheckoutComponent {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CheckoutComponent.class);
        application.run(args);
    }
}
