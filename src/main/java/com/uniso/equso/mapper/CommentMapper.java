package com.uniso.equso.mapper;

import com.uniso.equso.dao.entities.CommentEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.model.comments.CommentResponseDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class CommentMapper {
    public static final CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "creator", expression = "java(UserMapper.INSTANCE.entityToUserInfo(entity.getCreator()))")
    public abstract List<CommentResponseDto> entityListToCommentResponseDtoList(List<CommentEntity> entity);

    @BeforeMapping
    protected void filterComments(List<CommentEntity> comments, @MappingTarget List<CommentResponseDto> responseDtos) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getStatus() != Status.ACTIVE) {
                comments.remove(i);
            }
        }
    }

}
