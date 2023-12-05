package ua.lviv.javaclub.jpa22stream.infrastructure.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.AvailableHints;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.CommentWithMemberNameDto;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.MemberDto;
import ua.lviv.javaclub.jpa22stream.domain.model.dto.PostWithMemberNameDto;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Comment;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Member;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Post;
import ua.lviv.javaclub.jpa22stream.infrastructure.mapper.BoardMapper;
import ua.lviv.javaclub.jpa22stream.infrastructure.repository.CommentRepository;
import ua.lviv.javaclub.jpa22stream.infrastructure.repository.MemberRepository;
import ua.lviv.javaclub.jpa22stream.infrastructure.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ua.lviv.javaclub.jpa22stream.domain.model.dto.MemberDto.MEMBER_DTO_NAMED_QUERY;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardMapper boardMapper;

    @GetMapping("/clear")
    @Transactional
    public void clearData() {
        log.info("DB clear begin");
        commentRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
        log.info("DB clear end");
    }

    @GetMapping("/members")
    public List<MemberDto> getMembers() {
        return memberRepository.findAll().stream()
                .map(boardMapper::toMemberDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/members/stream/{count}")
    public List<MemberDto> getMembersAtream(@PathVariable("count") Long count) {
        return entityManager.createQuery(
                        """
                                SELECT id, name, disabled
                                FROM Member
                                ORDER BY id DESC
                                """,
                        Member.class)
                .getResultStream()
                .limit(count)
                .map(boardMapper::toMemberDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/posts/stream/{count}")
    public List<PostWithMemberNameDto> getPostsWithMemberName(@PathVariable("count") Long count) {
        return entityManager.createQuery(
                        """
                                SELECT id, content, member, ts
                                FROM Post
                                ORDER BY id DESC
                                """,
                        Post.class)
                .getResultStream()
                .limit(count)
                .map(boardMapper::toPostDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/comments/stream/{count}")
    public List<CommentWithMemberNameDto> getComments(@PathVariable("count") Long count) {
        return entityManager.createQuery(
                        """
                                SELECT id, post, content, member, ts
                                FROM Comment
                                ORDER BY id DESC
                                """,
                        Comment.class)
                .getResultStream()
                .limit(count)
                .map(boardMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/comments/stream/explain-hint/{count}")
    public String getCommentsPlanHint(@PathVariable("count") Long count) {
        final List<Object> explanation = entityManager.createNativeQuery(
                        """
                                EXPLAIN ANALYZE SELECT id, post_id, content, member_id, ts
                                FROM comments
                                ORDER BY id DESC
                                """)
                .setHint(AvailableHints.HINT_FETCH_SIZE, (int) count.longValue())
                .unwrap(Query.class)
                .getResultList();
        return explanation.stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @GetMapping("/comments/stream/explain-maxresult/{count}")
    public String getCommentsPlanMaxResult(@PathVariable("count") Long count) {
        final List<Object> explanation = entityManager.createNativeQuery(
                        """
                                EXPLAIN ANALYZE SELECT id, post_id, content, member_id, ts
                                FROM comments
                                ORDER BY id DESC
                                """)
                .setMaxResults((int) count.longValue())
                .unwrap(Query.class)
                .getResultList();
        return explanation.stream()
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    // Different approaches to map a projection queries to a DTO

    // JPA - Tuple and JPQL
    @GetMapping("members/jpa/tuple-jpql")
    public List<MemberDto> getMembersJpaTupleJpql() {
        return entityManager.createQuery(
                """
                        SELECT id, name, disabled
                        FROM Member
                        """
        ).getResultList();
    }

    // JPA - Constructor expression and JPQL
    @GetMapping("members/jpa/constexp-jpql")
    public List<MemberDto> getMembersJpaConstructorExpressionJpql() {
        return entityManager.createQuery(
                """
                        SELECT new ua.lviv.javaclub.jpa22stream.domain.model.dto.MemberDto(id, name)
                        FROM Member
                        """
        ).getResultList();
    }

    // JPA - Tuple and native SQL
    @GetMapping("members/jpa/tuple-native-sql")
    public List<MemberDto> getMembersJpaTupleNativeSql() {
        List<Tuple> tuples = entityManager.createNativeQuery(
                """
                        SELECT id, name, disabled
                        FROM members
                        """,
                Tuple.class
        ).getResultList();
        return tuples.stream()
                .map(obj -> {
                    final Tuple tuple = (Tuple) obj;
                    final Member member = new Member(
                            (Long) tuple.get("id"),
                            (String) tuple.get("name"),
                            (Boolean) tuple.get("disabled")
                    );
                    return boardMapper.toMemberDto(member);
                })
                .collect(Collectors.toList());
    }

    // ConstructorResult and named native SQL
    @GetMapping("members/jpa/constructor-result")
    public List<MemberDto> getMembersConstructorResultNativeSql() {
        return entityManager.createNamedQuery(MEMBER_DTO_NAMED_QUERY)
                .getResultList();
    }

    // Hibernate - ResultTransformer and JPQL
    @GetMapping("members/hibernate/result-transformer-jpql")
    public List<MemberDto> getMembersHibernateResultTransformarJpql() {
        return entityManager.createQuery(
                        """
                                SELECT id, name
                                FROM Member
                                """
                )
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(MemberDto.class))
                .getResultList();
    }

    // Hibernate - ResultTransformer and native SQL
    @GetMapping("members/hibernate/result-transformer-native-sql")
    public List<MemberDto> getMembersHibernateResultTransformarNativeSql() {
        return entityManager.createNativeQuery(
                        """
                                SELECT id, name
                                FROM members
                                """
                )
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(MemberDto.class))
                .getResultList();
    }

}
