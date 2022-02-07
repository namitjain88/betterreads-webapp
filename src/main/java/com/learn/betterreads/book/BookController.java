package com.learn.betterreads.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    private final String COVER_IMAGE_ROOT_URL = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable String id, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String coverImageUrl = "/images/no-image.png";
            if (!CollectionUtils.isEmpty(book.getCoverIds())) {
                coverImageUrl = COVER_IMAGE_ROOT_URL + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImageUrl", coverImageUrl);
            model.addAttribute("book", book);
            return "book";
        }
        return "book-not-found";
    }
}
