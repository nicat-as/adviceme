package com.uniso.equso.mapper;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.model.posts.SearchPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class PostMapper {

    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    @Mapping(target = "creator",expression = "java(UserMapper.INSTANCE.entityToUserInfo(postEntity.getCreator()))")
    @Mapping(target = "wallUser",expression = "java(UserMapper.INSTANCE.entityToUserInfo(postEntity.getWallUser()))")
    @Mapping(target = "category",expression = "java(CategoryMapper.INSTANCE.entityToCategoryDto(postEntity.getCategory()))")
    @Mapping(target = "comments",expression = "java(CommentMapper.INSTANCE.entityListToCommentResponseDtoList(postEntity.getComments()))")
    public abstract SearchPostResponse entityToSearchPostResponse(PostEntity postEntity);

}
