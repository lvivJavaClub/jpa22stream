package ua.lviv.javaclub.jpa22stream.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Comment;

@Repository
public interface CommentRepository extends
        JpaRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {
}
