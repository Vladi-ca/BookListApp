package com.example.android.booklistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vladi on 21.7.17.
 */

/*
* {@link BookAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link Book} objects.
* */
public class BookAdapter extends ArrayAdapter<Book> {
    /**
     * @param context The current context. Used to inflate the layout file.
     * @param books   A List of Book objects to display in a list
     */

    public BookAdapter(Context context, List<Book> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        BookAdapter.ViewHolder holder;

        if (listItemView != null) {

            holder = (ViewHolder) listItemView.getTag();
        } else {

            // it will populate book_list_item
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // will get a title of the book
        holder.titleTextView.setText(currentBook.getTitle());


        // Find the TextView with view ID author and hide it, if it is empty
        if (currentBook.getAuthors().isEmpty()) holder.authorTextView.setVisibility(View.GONE);
        else {
            holder.authorTextView.setText(currentBook.getAuthors());
            holder.authorTextView.setVisibility(View.VISIBLE);
        }
        return listItemView;
    }


    // For the possibility of using the Butter knife library I add a dependency and synced project with gradle
    static class ViewHolder {
        @InjectView(R.id.title)
        TextView titleTextView;
        @InjectView(R.id.authors)
        TextView authorTextView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
