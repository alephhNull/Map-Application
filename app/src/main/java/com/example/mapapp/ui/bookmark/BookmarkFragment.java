package com.example.mapapp.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mapapp.MainActivity;
import com.example.mapapp.R;
import com.example.mapapp.ui.map.MapFragment;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.OnBookmarkListener {

    private ThreadPoolExecutor threadPoolExecutor;
    private BookmarkAdapter bookmarkAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        RecyclerView recyclerView = root.findViewById(R.id.bookmark_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.bookmarkAdapter = new BookmarkAdapter(MainActivity.locations, getContext(), this);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(bookmarkAdapter);
        return root;
    }

    @Override
    public void onClick(int position) {
        BookmarkItem location = MainActivity.locations.get(position);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("map")).commit();
        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("bookmark")).commit();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentByTag("map");
        mapFragment.zoomOnBookmarkedLocation(location);
    }
}