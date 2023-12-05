package ua.lviv.javaclub.jpa22stream.infrastructure.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface BaseRepository<T> extends Repository<T, Long> {

    Iterable<T> findAllById(Iterable<Long> ids);

}
