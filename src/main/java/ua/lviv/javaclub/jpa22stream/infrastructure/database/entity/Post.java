package ua.lviv.javaclub.jpa22stream.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
@ToString
public class Post implements Serializable {

    private static final String GEN_ID = "post_gen";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GEN_ID)
    @SequenceGenerator(name = GEN_ID, sequenceName = "post_id_seq", allocationSize = 1)
    private Long id;
    private String content;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime ts;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public Post(final Long id, final String content, final Member member, final LocalDateTime ts) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.ts = ts;
    }

}
