package com.learn.betterreads.userbooks;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table(value = "book_by_userid_bookId")
@Data
public class UserBooks {

    @PrimaryKey
    private UserBooksPrimaryKey key;

    @Column(value = "started_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startedDate;

    @Column(value = "completed_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate completedDate;

    @Column(value = "reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    @CassandraType(type = CassandraType.Name.INT)
    private int rating;
}
