package ua.lviv.javaclub.jpa22stream.domain.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    public static final String
            MEMBER_DTO_NAME = "MemberDto",
            MEMBER_DTO_NAMED_QUERY = "MemberDtoQuery";

    private Long id;
    private String name;
    private Boolean disabled;

    public MemberDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
        this.disabled = false;
    }
}
