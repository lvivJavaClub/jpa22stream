package ua.lviv.javaclub.jpa22stream.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.lviv.javaclub.jpa22stream.infrastructure.database.entity.Member;

@Repository
public interface MemberRepository extends
        JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member> {
}
