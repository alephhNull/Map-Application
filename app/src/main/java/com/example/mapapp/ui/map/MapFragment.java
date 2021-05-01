package com.example.mapapp.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mapapp.MainActivity;
import com.example.mapapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import kotlin.Suppress;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private ThreadPoolExecutor threadPoolExecutor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().add(this, "map").commit();
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        View fragmentlayout = inflater.inflate(R.layout.fragment_map, container, false);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        mapView = fragmentlayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        threadPoolExecutor.execute(() -> mapView.getMapAsync(this));
        return fragmentlayout;
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
    }



    public void goToUsersLocation() {
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        double lat = locationComponent.getLastKnownLocation().getLatitude();
        double lng = locationComponent.getLastKnownLocation().getLongitude();
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15)
                .tilt(20)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build()
            );
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            FloatingActionButton myLocation = getActivity().findViewById(R.id.floatingActionButton6);
            myLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToUsersLocation();
                }
            });
            goToUsersLocation();
            mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {

                @Override
                public boolean onMapLongClick(@NonNull LatLng point) {
                    double clickLat = point.getLatitude();
                    double clickLong = point.getLongitude();



                    BottomSheetDialog bottomSheet = new BottomSheetDialog("Save Location (" + clickLat + "," + clickLong + ")");
                    bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
                    return true;
                }
            });
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }
}