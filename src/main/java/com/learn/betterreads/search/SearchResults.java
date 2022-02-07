package com.learn.betterreads.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchResults {

    private int num_found;
    private List<SearchResultBook> docs;
}
