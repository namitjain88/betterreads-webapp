package com.learn.betterreads.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final String COVER_IMAGE_ROOT_URL = "https://covers.openlibrary.org/b/id/";

    private WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build()).baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping("/search")
    public String getSearchResults(@RequestParam String query, Model model) {
        Mono<SearchResults> searchResultsMono = this.webClient.get()
                .uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResults.class);
        SearchResults searchResults = searchResultsMono.block();

        List<SearchResultBook> books = searchResults.getDocs().stream()
                .limit(10) //limiting search results to first 10 books
                .map(book -> {
                    book.setKey(book.getKey().replace("/works/", ""));
                    if (StringUtils.hasText(book.getCover_i())) {
                        book.setCover_i(COVER_IMAGE_ROOT_URL + book.getCover_i() + "-M.jpg");
                    } else {
                        book.setCover_i("/images/no-image.png");
                    }
                    return book;
                })
                .collect(Collectors.toList());
        model.addAttribute("searchResults", books);
        return "search";
    }
}
