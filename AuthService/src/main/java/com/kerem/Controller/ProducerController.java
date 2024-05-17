package com.kerem.Controller;

import com.kerem.Dto.Request.AuthRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/registerWithRabbit")
    public String convertAndSend(@RequestBody AuthRegisterRequestDto dto) {
        rabbitTemplate.convertAndSend("exchange.direct", "routing.A", dto);
        return "With using routing.A the Dto has been sent to the RabbitMQ Direct Exchange";
    }

    @PostMapping("/activateWithRabbit")
    public void activateAccountWithRabbit(Long id){
        rabbitTemplate.convertAndSend("exchange.direct", "routing.B", id);
    }
}
