package com.learn.betterreads.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table(value = "books_by_user")
@Data
@EqualsAndHashCode(of = {"id", "bookId"})
public class BooksByUser {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @PrimaryKeyColumn(name = "book_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String bookId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID timeUuid;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    @Column(value = "reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    @Column(value = "book_title")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String bookTitle;

    @Column(value = "cover_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> coverIds;

    @Column(value = "author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;

    @Column(value = "rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int rating;

    @Transient
    private String coverImageUrl;
}
