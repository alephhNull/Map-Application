package com.example.mapapp.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView placeText;
        public TextView placeName;
        public TextView placeType;
        private Context context;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.placeText = itemView.findViewById(R.id.placeText);
            this.placeName = itemView.findViewById(R.id.placeName);
            this.placeType = itemView.findViewById(R.id.placeType);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View locationView = inflater.inflate(R.layout.item_location, parent, false);
        return new ViewHolder(context, locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Todo
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
