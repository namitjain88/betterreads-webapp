package com.learn.betterreads.userbooks;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
}
