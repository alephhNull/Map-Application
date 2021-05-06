package com.example.mapapp.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
import com.example.mapapp.database.DatabaseManager;
import com.example.mapapp.ui.bookmark.BookmarkItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private DatabaseManager dbManager;
    private static MapboxMap mapboxMap;
    public static BookmarkItem currentLocation;
    private MapView mapView;
    private FloatingActionButton myLocation;
    public ThreadPoolExecutor threadPoolExecutor;
    private EditText searchBar;
    private RecyclerView suggestedLocations;
    private ImageButton searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View fragmentlayout = inflater.inflate(R.layout.fragment_map, container, false);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        mapView = fragmentlayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        threadPoolExecutor.execute(() -> mapView.getMapAsync(this));
        dbManager.setTask(1);
        threadPoolExecutor.execute(dbManager);
        return fragmentlayout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dbManager = DatabaseManager.getInstance(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        myLocation = getActivity().findViewById(R.id.floatingActionButton6);
        suggestedLocations = getActivity().findViewById(R.id.recycler);
        suggestedLocations.setAdapter(new LocationAdapter());
        suggestedLocations.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestedLocations.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        searchButton = getActivity().findViewById(R.id.imageButton3);
        searchBar = getActivity().findViewById(R.id.searchLocation);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0)
                    performSearch();
                else {
                    suggestedLocations.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchBar.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                }
                return false;
            }

        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.clearFocus();
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            }
        });
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUsersLocation();
            }
        });
    }

    public void performSearch() {
        suggestedLocations.setVisibility(View.VISIBLE);
        API_Interface searchService = RetrofitClient.getRetrofitInstance().create(API_Interface.class);
        Call<API_Response> call = searchService.getResults(searchBar.getText().toString(), "sk.eyJ1IjoibWV0aHVzYWxlaCIsImEiOiJja254NXlqeGQxM2pxMnBuam1xbXZjYnZjIn0.VMRVou-KyyslhJefIYI3Cg");
        threadPoolExecutor.execute(() -> call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, Response<API_Response> response) {
                assert response.body() != null;
                API_Response api_response = response.body();
                LocationAdapter locationAdapter = (LocationAdapter) suggestedLocations.getAdapter();
                locationAdapter.setSuggestedLocations((ArrayList<Location>) api_response.getResults());
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        }));
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
        MapFragment.mapboxMap = mapboxMap;
        LocationAdapter locationAdapter = (LocationAdapter) suggestedLocations.getAdapter();
        locationAdapter.setMapboxMap(mapboxMap);
        String uri;
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            uri = "mapbox://styles/mapbox/dark-v10";
        else
            uri = "mapbox://styles/mapbox/streets-v11";
        mapboxMap.setStyle(new Style.Builder().fromUri(uri),
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
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
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
            if (currentLocation == null)
                goToUsersLocation();
            else
                zoomOnBookmarkedLocation();
            mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {

                @Override
                public boolean onMapLongClick(@NonNull LatLng point) {
                    double clickLat = point.getLatitude();
                    double clickLong = point.getLongitude();
                    if (mapboxMap.getMarkers().size() == 0) {
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(point));
                    } else
                        mapboxMap.getMarkers().get(0).setPosition(point);
                    BottomSheetDialog bottomSheet = new BottomSheetDialog(clickLat, clickLong, threadPoolExecutor);
                    bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
                    if (mapboxMap.getMarkers().size() == 0) {
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(point));
                    } else {
                        Marker marker = mapboxMap.getMarkers().get(0);
                        marker.setPosition(point);
                    }
                    return true;
                }
            });
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    public static void zoomOnBookmarkedLocation () {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongtitude());
        String name = currentLocation.getName();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .tilt(20)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);

        if (mapboxMap.getMarkers().size() == 0) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(latLng));
        } else {
            Marker marker = mapboxMap.getMarkers().get(0);
            marker.setPosition(latLng);
            marker.setTitle(name);
        }
        currentLocation = null;
    }
}