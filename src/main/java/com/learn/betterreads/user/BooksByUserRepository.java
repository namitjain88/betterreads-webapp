package com.learn.betterreads.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksByUserRepository extends CassandraRepository<BooksByUser, String> {

    public Slice<BooksByUser> findAllById(String userId, Pageable pageable);
}
