package com.example.mapapp.ui.bookmark;

import android.widget.Filter;

import com.example.mapapp.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class BookmarkFilter extends Filter {

    private ArrayList<BookmarkItem> bookmarkItems;
    private BookmarkAdapter bookmarkAdapter;

    public BookmarkFilter(ArrayList<BookmarkItem> bookmarkItems, BookmarkAdapter bookmarkAdapter) {
        this.bookmarkItems = bookmarkItems;
        this.bookmarkAdapter = bookmarkAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        ArrayList<BookmarkItem> filteredBookmarkItems = new ArrayList<>();
        if (charSequence == null || charSequence.length() == 0) {
            filteredBookmarkItems = bookmarkItems;
        } else {
            for (BookmarkItem bookmarkItem : bookmarkItems) {
                if (bookmarkItem.getName().toUpperCase().contains(charSequence))
                    filteredBookmarkItems.add(bookmarkItem);
            }
        }
        FilterResults filterResults = new FilterResults();
        filterResults.values = filteredBookmarkItems;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        bookmarkAdapter.setData((ArrayList<BookmarkItem>) filterResults.values);
        bookmarkAdapter.notifyDataSetChanged();
    }
}
