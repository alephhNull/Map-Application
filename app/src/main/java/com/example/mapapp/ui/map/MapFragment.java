package com.example.mapapp.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private FloatingActionButton myLocation;
    private ThreadPoolExecutor threadPoolExecutor;
    private EditText searchBar;
    private RecyclerView suggestedLocations;
    private ImageButton searchButton;

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
    public void onResume() {
        super.onResume();
        myLocation = getActivity().findViewById(R.id.floatingActionButton6);
        suggestedLocations = getActivity().findViewById(R.id.recycler);
        searchButton = getActivity().findViewById(R.id.imageButton3);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUsersLocation();
            }
        });
        searchBar = getActivity().findViewById(R.id.searchLocation);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    public void performSearch() {
        searchBar.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
        suggestedLocations.setVisibility(View.VISIBLE);
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
        if (locationComponent.getLastKnownLocation() != null) {
            double lat = locationComponent.getLastKnownLocation().getLatitude();
            double lng = locationComponent.getLastKnownLocation().getLongitude();
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng))
                    .zoom(15)
                    .tilt(20)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
        }
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