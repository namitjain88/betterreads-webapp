package com.learn.betterreads.home;

import com.learn.betterreads.user.BooksByUser;
import com.learn.betterreads.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    private final String COVER_IMAGE_ROOT_URL = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/")
    public String getHomePage(@AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || principal.getAttribute("login") == null) {
            return "index";
        }

        String userId = principal.getAttribute("login");
        Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 100));
        List<BooksByUser> books = booksSlice.getContent();
        books = books.stream().distinct().map(book -> {
            String coverImageUrl = "/images/no-image.png";
            if (!CollectionUtils.isEmpty(book.getCoverIds())) {
                coverImageUrl = COVER_IMAGE_ROOT_URL + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverImageUrl(coverImageUrl);
            return book;
        }).collect(Collectors.toList());

        model.addAttribute("books", books);

        return "home";
    }
}
