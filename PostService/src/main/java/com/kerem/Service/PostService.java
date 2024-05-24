package com.kerem.Service;

import com.kerem.Dto.Request.PostSaveRequestDto;
import com.kerem.Dto.Response.PostFindAllResponseDto;
import com.kerem.Entity.Post;
import com.kerem.Manager.UserProfileManager;
import com.kerem.Mapper.PostMapper;
import com.kerem.Repository.PostRepository;
import com.kerem.Utility.JwtTokenManager;
import com.kerem.exceptions.ErrorType;
import com.kerem.exceptions.PostMicroServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserProfileManager userProfileManager;

    public void save(String token, PostSaveRequestDto postSaveRequestDto){
        Long idFromToken = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new PostMicroServiceException(ErrorType.INVALID_TOKEN));
        String userProfileId = userProfileManager.findIdByAuthId(idFromToken);


        if (!postSaveRequestDto.getUserProfileId().equals(userProfileId)){
            throw new PostMicroServiceException(ErrorType.USER_NOT_FOUND);
        } else {
            postSaveRequestDto.setUserProfileId(userProfileId);
            postRepository.save(PostMapper.INSTANCE.dtoToPost(postSaveRequestDto));
        }
    }

    public List<PostFindAllResponseDto> findAllById(String token){
        Long idFromToken = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new PostMicroServiceException(ErrorType.INVALID_TOKEN));
        String userProfileId = userProfileManager.findIdByAuthId(idFromToken);

        Optional<List<Post>> allById = postRepository.findAllById(userProfileId);
        if (allById.isEmpty()){
            return null;
        } else {
            List<Post> posts = allById.get();
            List<PostFindAllResponseDto> dtoPostList = new ArrayList<>();
            for (Post post : posts){
                PostFindAllResponseDto postFindAllResponseDto = PostMapper.INSTANCE.postToFindAllPost(post);
                dtoPostList.add(postFindAllResponseDto);
            }
            return dtoPostList;
        }
    }

    public List<PostFindAllResponseDto> findAll(){
        List<Post> all = postRepository.findAll();
        List<PostFindAllResponseDto> dtoPostList = new ArrayList<>();
        for (Post post : all){
            PostFindAllResponseDto postFindAllResponseDto = PostMapper.INSTANCE.postToFindAllPost(post);
            dtoPostList.add(postFindAllResponseDto);
        }
        return dtoPostList;
    }

    public void delete(Long id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()){
            throw new PostMicroServiceException(ErrorType.POST_NOT_FOUND);
        } else {
            postRepository.delete(post.get());
        }
    }

    public void updatePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()){
            throw new PostMicroServiceException(ErrorType.POST_NOT_FOUND);
        } else {
            PostSaveRequestDto postSaveRequestDto = PostMapper.INSTANCE.postToPostDto(post.get());
            postRepository.save(PostMapper.INSTANCE.dtoToPost(postSaveRequestDto));
        }
    }
}
