package com.kerem.Mapper;

import com.kerem.Dto.Request.PostSaveRequestDto;
import com.kerem.Dto.Response.PostFindAllResponseDto;
import com.kerem.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostSaveRequestDto postToPostDto(Post post);
    Post dtoToPost(PostSaveRequestDto postSaveRequestDto);
    PostFindAllResponseDto postToFindAllPost(Post post);
}
