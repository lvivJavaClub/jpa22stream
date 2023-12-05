package ua.lviv.javaclub.jpa22stream.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.CommentWithMemberNameDto;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.MemberDto;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.PostWithMemberNameDto;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Comment;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Member;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Post;

@Mapper
public interface BoardMapper {

    MemberDto toMemberDto(final Member member);

    @Mapping(target = "memberName", source = "member.name")
    PostWithMemberNameDto toPostDto(final Post post);

    @Mapping(target = "memberName", source = "member.name")
    @Mapping(target = "postId", source = "post.id")
    CommentWithMemberNameDto toCommentDto(final Comment comment);

}
