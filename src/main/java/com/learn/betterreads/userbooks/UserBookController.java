package com.learn.betterreads.userbooks;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.learn.betterreads.book.Book;
import com.learn.betterreads.book.BookRepository;
import com.learn.betterreads.user.BooksByUser;
import com.learn.betterreads.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserBookController {

    @Autowired
    private UserBooksRepository userBooksRepository;

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping(value = "/addUserBook")
    public ModelAndView addUserBook(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }

        //Retrieving userId and bookId fom principal and formData respectively
        String userId = principal.getAttribute("login");
        String bookId = formData.getFirst("bookId");

        //Retrieve book details using bookId to use in saving user book mapping table
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        // Creating user book tracking object (books_by_userid_bookid) and saving it do database
        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        key.setUserId(userId);
        key.setBookId(bookId);

        UserBooks userBooks = new UserBooks();
        userBooks.setKey(key);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooks.setRating(Integer.parseInt(formData.getFirst("rating")));

        userBooksRepository.save(userBooks);

        // Creating user book mapping object (books_by_user) and saving it do database
        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookTitle(book.getTitle());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(Integer.parseInt(formData.getFirst("rating")));
        booksByUser.setTimeUuid(Uuids.timeBased());

        booksByUserRepository.save(booksByUser);

        return new ModelAndView("redirect:/books/" + formData.getFirst("bookId"));
    }
}
