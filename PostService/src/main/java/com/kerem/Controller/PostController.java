package com.kerem.Controller;

import com.kerem.Constant.EndPoints;
import com.kerem.Dto.Request.PostSaveRequestDto;
import com.kerem.Dto.Response.PostFindAllResponseDto;
import com.kerem.Entity.Post;
import com.kerem.Service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndPoints.POST)
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(EndPoints.SAVE)
    public ResponseEntity<String> publishPost(String token, PostSaveRequestDto postSaveRequestDto){
        postService.save(token, postSaveRequestDto);
        return ResponseEntity.ok("Post kaydedildi.");
    }

    @GetMapping(EndPoints.FINDALLBYID)
    public ResponseEntity<List<PostFindAllResponseDto>> findAllById(String token){
        List<PostFindAllResponseDto> allPostsById = postService.findAllById(token);
        if (allPostsById.isEmpty()){
            System.out.println("Bu kullanıcıya ait post yoktur.");
        }
        return ResponseEntity.ok(allPostsById);
    }
    
    @GetMapping(EndPoints.FINDALL)
    public ResponseEntity<List<PostFindAllResponseDto>> findAll(){
        List<PostFindAllResponseDto> allPosts = postService.findAll();
        return ResponseEntity.ok(allPosts);
    }

    @DeleteMapping(EndPoints.DELETE)
    public ResponseEntity<String> deletePost(Long id){
        postService.delete(id);
        return ResponseEntity.ok("Post silindi");
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity<String> updatePost(Long id){
        postService.updatePost(id);
        return ResponseEntity.ok("Post güncellendi");
    }

}
