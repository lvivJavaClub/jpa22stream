package ua.lviv.javaclub.jpa22stream.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.MemberDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members")
@ToString
@NamedNativeQuery(
        name = MemberDto.MEMBER_DTO_NAMED_QUERY,
        query = """
                    SELECT id, name
                    FROM members
                """,
        resultSetMapping = MemberDto.MEMBER_DTO_NAME
)
@SqlResultSetMapping(
        name = MemberDto.MEMBER_DTO_NAME,
        classes = @ConstructorResult(
                targetClass = MemberDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "name")
                }
        )
)
public class Member implements Serializable {

    private static final String GEN_ID = "member_gen";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GEN_ID)
    @SequenceGenerator(name = GEN_ID, sequenceName = "member_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private Boolean disabled;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public Member(final Long id, final String name, final Boolean disabled) {
        this.id = id;
        this.name = name;
        this.disabled = disabled;
    }

}
