package com.example.android.booklistapp;

/**
 * Created by Vladi on 21.7.17.
 */

public class Book {

    private String mTitle;
    private String mAuthors;
    private String mUrl;

    // Constructing a new Book object
    // title is the name of the book
    // authors is an author of the book object
    // url is the website URL to find more details about the book
    public Book(String title, String authors, String url) {
        mTitle = title;
        mAuthors = authors;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    /**
     * Returns the website URL to find more information about the book.
     */
    public String getUrl() {
        return mUrl;
    }
}