package com.example.android.booklistapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * URL for google books data from the Google API
     */
    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isConnected(Context context) {

        // Creating ConnectivityManager and it gets network information
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // network is active ak nie je null or is connected or connecting
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Set a click listener on the search Button, to implement the search
        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the button is clicked on.
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();

                // Checks for internet connection
                // if network is available
                if (isConnected(context)) {

                    EditText searchEditTextView = (EditText) findViewById(R.id.search);
                    String searchInput = searchEditTextView.getText().toString();

                    // if search is empty the message will appear
                    if (searchInput.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Search for a book.", Toast.LENGTH_SHORT).show();

                        // if search is not empty the book url will be attempted
                    } else {
                        Toast.makeText(getApplicationContext(), "Searching for: " + searchInput, Toast.LENGTH_SHORT).show();
                        String url = BOOK_REQUEST_URL + searchInput;

                        // Starts the AsyncTask to fetch the book data
                        BookAsyncTask task = new BookAsyncTask();

                        // URL will be attempted
                        task.execute(url);
                    }

                } else {
                    //Provides feedback about no internet connection
                    Toast.makeText(MainActivity.this, "Please check your internet connection - No internet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the chosen book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of books in the response.
     */
    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        // UI is not updated about progress
        ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Searching");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> data) {
            // Clear the adapter of previous book data
            mAdapter.clear();
            progDailog.dismiss();
            // If there is a valid list of {@link Book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}


