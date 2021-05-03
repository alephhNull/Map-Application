package com.example.mapapp.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Location> suggestedLocations;

    public void setSuggestedLocations(ArrayList<Location> suggestedLocations) {
        this.suggestedLocations = suggestedLocations;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView placeText;
        public TextView placeName;
        public TextView placeType;
        private Context context;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.placeText = itemView.findViewById(R.id.placeText);
            this.placeName = itemView.findViewById(R.id.placeName);
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
        holder.placeText.setText(suggestedLocations.get(position).getText());
        holder.placeName.setText(suggestedLocations.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(suggestedLocations == null)
            return 0;
        return suggestedLocations.size();
    }


}
