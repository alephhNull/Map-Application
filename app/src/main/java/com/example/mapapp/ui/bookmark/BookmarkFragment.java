package com.example.mapapp.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
import com.example.mapapp.ui.map.Location;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.OnBookmarkListener {

    private ArrayList<Location> locations = new ArrayList<>();
    private BookmarkAdapter bookmarkAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.bookmark_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.bookmarkAdapter = new BookmarkAdapter(locations, getContext(), this);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(bookmarkAdapter);
        return root;
    }

    @Override
    public void onClick(int position) {
        //TODO:add listener for each row in recycler view
    }
}