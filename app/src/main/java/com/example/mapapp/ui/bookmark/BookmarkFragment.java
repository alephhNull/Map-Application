package com.example.mapapp.ui.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mapapp.MainActivity;
import com.example.mapapp.R;
import com.example.mapapp.ui.map.Location;
import com.example.mapapp.ui.map.MapFragment;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.OnBookmarkListener {

    private ThreadPoolExecutor threadPoolExecutor;
    private BookmarkAdapter bookmarkAdapter;
    public UiHandler uiHandler;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.uiHandler = new UiHandler();
        this.uiHandler.setContext(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        RecyclerView recyclerView = root.findViewById(R.id.bookmark_recycler_view);
        SearchView searchView = root.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bookmarkAdapter.getFilter().filter(s.toUpperCase());
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.bookmarkAdapter = new BookmarkAdapter(MainActivity.locations, getContext(), this, threadPoolExecutor, uiHandler);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(bookmarkAdapter);
        return root;
    }

    @Override
    public void onClick(int position) {
        BookmarkItem location = MainActivity.locations.get(position);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        MapFragment.currentLocation = location;
        navController.popBackStack(R.id.navigation_map, false);
    }
    public static boolean zoom = false;

    public class UiHandler extends Handler {
        private WeakReference<Context> mWeakRefContext;

        public void setContext(Context context) {
            this.mWeakRefContext = new WeakReference<Context>(context);
        }

        public WeakReference<Context> getmWeakRefContext() {
            return mWeakRefContext;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "Couldn't reach server", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "item deleted", Toast.LENGTH_SHORT).show();
                        bookmarkAdapter.setData(MainActivity.locations);
                        bookmarkAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "history deleted", Toast.LENGTH_SHORT).show();
                        bookmarkAdapter.setData(MainActivity.locations);
                        bookmarkAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}