package ua.lviv.javaclub.jpa22stream.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
@ToString
public class Comment implements Serializable {

    private static final String GEN_ID = "comment_gen";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GEN_ID)
    @SequenceGenerator(name = GEN_ID, sequenceName = "comment_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;
    private String content;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime ts;

    public Comment(final Long id, final Post post, final String content, final Member member, final LocalDateTime ts) {
        this.id = id;
        this.post = post;
        this.content = content;
        this.member = member;
        this.ts = ts;
    }

}
