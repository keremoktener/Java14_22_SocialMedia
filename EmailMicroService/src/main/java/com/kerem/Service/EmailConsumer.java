package com.kerem.Service;

import com.kerem.Dto.Request.ActivationCodeEmailRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;


    @RabbitListener(queues = "q.C")
    public void receiveRegistrationEmail(ActivationCodeEmailRequestDto dto) {
        emailService.sendActivationCode(dto.getToEmail(), dto.getActivationCode());
    }
}
