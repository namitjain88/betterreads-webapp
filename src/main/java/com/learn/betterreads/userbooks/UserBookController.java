package com.learn.betterreads.userbooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class UserBookController {

    @Autowired
    private UserBooksRepository userBooksRepository;

    @PostMapping(value = "/addUserBook")
    public ModelAndView addUserBook(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }

        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        key.setUserId(principal.getAttribute("login"));
        key.setBookId(formData.getFirst("bookId"));

        UserBooks userBooks = new UserBooks();
        userBooks.setKey(key);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooks.setRating(Integer.parseInt(formData.getFirst("rating")));

        userBooksRepository.save(userBooks);

        return new ModelAndView("redirect:/books/" + formData.getFirst("bookId"));
    }
}
