package com.learn.betterreads.book;

import com.learn.betterreads.userbooks.UserBooks;
import com.learn.betterreads.userbooks.UserBooksPrimaryKey;
import com.learn.betterreads.userbooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBooksRepository userBooksRepository;

    private final String COVER_IMAGE_ROOT_URL = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable String id, Model model,
                          @AuthenticationPrincipal OAuth2User principal) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String coverImageUrl = "/images/no-image.png";
            if (!CollectionUtils.isEmpty(book.getCoverIds())) {
                coverImageUrl = COVER_IMAGE_ROOT_URL + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImageUrl", coverImageUrl);
            model.addAttribute("book", book);

            if (principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
                String loginId = principal.getAttribute("login");
                model.addAttribute("loginId", loginId);

                //retrieving user book tracking
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setUserId(loginId);
                key.setBookId(id);

                Optional<UserBooks> optionalUserBooks = userBooksRepository.findById(key);
                if (optionalUserBooks.isPresent()) {
                    model.addAttribute("userBooks", optionalUserBooks.get());
                } else {
                    model.addAttribute("userBooks", new UserBooks());
                }
            }
            return "book";
        }
        return "book-not-found";
    }
}
