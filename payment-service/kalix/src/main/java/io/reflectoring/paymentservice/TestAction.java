package io.reflectoring.paymentservice;

import kalix.javasdk.action.Action;
import org.springframework.web.bind.annotation.GetMapping;

public class TestAction extends Action {

    @GetMapping("/hello")
    public Effect<String> test() {
        return effects().reply("hello");
    }

}
