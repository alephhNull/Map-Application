package com.example.mapapp.ui.bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
import com.example.mapapp.ui.map.Location;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private ArrayList<Location> data;
    private LayoutInflater inflater;
    private OnBookmarkListener listener;
    private Context context;

    public BookmarkAdapter(ArrayList<Location> data, Context context, OnBookmarkListener listener) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.bookmark_row, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = data.get(position);

        holder.nameTxtView.setText(location.getName());
        holder.latTxtView.setText(String.valueOf(location.getLatitude()));
        holder.longTxtView.setText(String.valueOf(location.getLongtitude()));
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:add on click listener for delete btn
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTxtView;
        TextView latTxtView;
        TextView longTxtView;
        ImageButton deleteBtn;
        OnBookmarkListener onBookmarkListener;

        public ViewHolder(@NonNull View itemView, OnBookmarkListener onBookmarkListener) {
            super(itemView);
            nameTxtView = itemView.findViewById(R.id.bookmark_row_name);
            latTxtView = itemView.findViewById(R.id.bookmark_row_lat);
            longTxtView = itemView.findViewById(R.id.bookmark_row_long);
            deleteBtn = itemView.findViewById(R.id.bookmark_row_delete_btn);
            this.onBookmarkListener = onBookmarkListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBookmarkListener.onClick(getAdapterPosition());
        }
    }

    public interface OnBookmarkListener {
        void onClick(int position);
    }


}
