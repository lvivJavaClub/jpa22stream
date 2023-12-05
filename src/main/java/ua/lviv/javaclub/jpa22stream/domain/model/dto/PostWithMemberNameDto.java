package ua.lviv.javaclub.jpa22stream.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostWithMemberNameDto {

    private Long id;
    private String content;
    private String memberName;
    private LocalDateTime ts;

}
