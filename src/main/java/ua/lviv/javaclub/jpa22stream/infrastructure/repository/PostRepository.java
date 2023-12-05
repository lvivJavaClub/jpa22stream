package ua.lviv.javaclub.jpa22stream.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Post;

@Repository
public interface PostRepository extends
        JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post> {
}
