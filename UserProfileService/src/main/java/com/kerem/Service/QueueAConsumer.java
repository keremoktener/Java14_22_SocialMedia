package com.kerem.Service;

import com.kerem.Constant.Status;
import com.kerem.Dto.Request.UserProfileSaveRequestDto;
import com.kerem.Entity.UserProfile;
import com.kerem.Mapper.UserProfileMapper;
import com.kerem.Repository.UserProfileRepository;
import com.kerem.exceptions.ErrorType;
import com.kerem.exceptions.UserProfileMicroServiceException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class QueueAConsumer {
    private final UserProfileRepository userProfileRepository;


    @RabbitListener(queues = "q.A")
    public void recieveMessageFromQueueA(@RequestBody UserProfileSaveRequestDto dto) {
        UserProfile userProfile = UserProfileMapper.INSTANCE.userProfileSaveRequestDtoToUserProfile(dto);
        userProfileRepository.save(userProfile);
    }

    @RabbitListener(queues = "q.B")
    public void recieveAuthIdFromQueueB(@RequestBody Long id){
        UserProfile userProfile = userProfileRepository.findByAuthId(id)
                .orElseThrow(() -> new UserProfileMicroServiceException(ErrorType.KULLANICI_NOT_FOUND));

        userProfile.setStatus(Status.ACTIVE);
        userProfileRepository.save(userProfile);
    }


}
