package com.borisk58.websocketms;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

record GreetingRequest(String name) {}
record GreetingResponse(String message) {}

@Controller
class GreetingsController {
    @MessageExceptionHandler
    @SendTo("/topic/errors")
    String handleException(Exception e) {
        var message = "Something went wrong while processing the request: " + NestedExceptionUtils.getMostSpecificCause(e);
        System.out.println(message);
        return message;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/greetings")
    GreetingResponse greet(GreetingRequest request) throws InterruptedException {
        Assert.isTrue(Character.isUpperCase(request.name().charAt(0)), () -> "the name must start with a capital letter!");
        Thread.sleep(1000);
        return new GreetingResponse("Hello, " + request.name() + "!");
    }
}
