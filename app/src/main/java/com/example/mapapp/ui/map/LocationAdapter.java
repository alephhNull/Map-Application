package com.example.mapapp.ui.map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Location> suggestedLocations;
    private MapboxMap mapboxMap;

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
            int position = getAdapterPosition();
            double lat = suggestedLocations.get(position).getLatitude();
            double lng = suggestedLocations.get(position).getLongtitude();
            LatLng latLng = new LatLng(lat, lng);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .tilt(20)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
            if (mapboxMap.getMarkers().size() == 0) {
                mapboxMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placeText.getText().toString()));
            } else
                mapboxMap.getMarkers().get(0).setPosition(latLng);
            Activity activity = (Activity) context;
            activity.findViewById(R.id.recycler).setVisibility(View.GONE);
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

    public void setMapboxMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }
}
