package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.enums.MapType.VECTOR;
import static com.leon.initialize_gis.helpers.Constants.TRIAL_NUMBER;
import static com.leon.initialize_gis.helpers.MyApplication.checkLicense;
import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;
import static com.leon.initialize_gis.helpers.MyApplication.getLocationTracker;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.google.android.material.snackbar.Snackbar;
import com.leon.initialize_gis.R;
import com.leon.initialize_gis.databinding.FragmentLocationPointBinding;
import com.leon.initialize_gis.tables.UsersPoints;
import com.leon.initialize_gis.utils.CustomToast;
import com.leon.initialize_gis.utils.gis.GoogleMapLayer;


public class LocationPointFragment extends Fragment implements View.OnClickListener {
    private FragmentLocationPointBinding binding;
    private int pointLayer = -1;
    private boolean isLong;
    private Point point;

    public LocationPointFragment() {
    }

    public static LocationPointFragment newInstance() {
        return new LocationPointFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationPointBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.fab.setOnClickListener(this);
        initializeMap();
    }

    private void initializeMap() {
        initializeBaseMap();
        binding.mapView.setMagnifierEnabled(true);
        binding.mapView.setCanMagnifierPanMap(true);
        onMapClickListener();
    }

    private void initializeBaseMap() {
        binding.mapView.setMap(new ArcGISMap());
        binding.mapView.getMap().getBasemap().getBaseLayers().add(new GoogleMapLayer().createLayer(VECTOR));
//        binding.mapView.getMap().getBasemap().getBaseLayers().add(new OsmMapLayer().createLayer());
        AsyncTask.execute(() -> {
            while (getLocationTracker(requireActivity()).getLocation() == null)
                binding.progressBar.setVisibility(View.VISIBLE);
            binding.mapView.setViewpoint(new Viewpoint(getLocationTracker(requireActivity()).getLatitude()
                    , getLocationTracker(requireActivity()).getLongitude(), 3600));
            requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onMapClickListener() {
        binding.mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(requireContext(), binding.mapView) {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                isLong = true;
            }

            @Override
            public boolean onUp(MotionEvent e) {
                if (isLong) {
                    if (pointLayer >= 0) {
                        binding.mapView.getGraphicsOverlays().remove(pointLayer);
                        pointLayer = -1;
                    }
                    point = mMapView.screenToLocation(new android.graphics.Point((int) e.getX(),
                            (int) e.getY()));
                    addPoint();
                }
                isLong = false;
                return super.onUp(e);
            }

        });
    }

    private void addPoint() {
        final GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        final BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(requireContext(),
                R.drawable.img_marker);
        final PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        final Graphic graphic = new Graphic(point, symbol);
        graphicsOverlay.getGraphics().add(graphic);
        binding.mapView.getGraphicsOverlays().add(graphicsOverlay);
        this.pointLayer = binding.mapView.getGraphicsOverlays().size() - 1;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.fab) {
            if (point == null) {
                Snackbar.make(view, getString(R.string.define_location), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (binding.layout.getVisibility() == View.GONE) {
                binding.layout.setVisibility(View.VISIBLE);
            } else if (binding.layout.getEditText() != null) {
                final String eshterak = binding.layout.getEditText().getText().toString();
                if (eshterak.isEmpty()) {
                    binding.layout.getEditText().setError(getString(R.string.error_empty));
                    binding.layout.getEditText().requestFocus();
                } else if (getApplicationComponent().MyDatabase().usersPointDao().eshterakPointsCounter(eshterak) > 0) {
                    binding.layout.getEditText().setError(getString(R.string.repeatitive_eshterak));
                    binding.layout.getEditText().requestFocus();
                } else if (!checkLicense()) {
                    new CustomToast().warning(getString(R.string.expired_trial));
                } else {
                    insertPoint(eshterak);
                    binding.layout.getEditText().setText("");
                    binding.layout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void insertPoint(final String eshterak) {
        final UsersPoints userPoints = new UsersPoints();
        userPoints.eshterak = eshterak;
        userPoints.x = point.getX();
        userPoints.y = point.getY();
        //TODO
        userPoints.date = eshterak;
        userPoints.locationDateTime = eshterak;
        userPoints.phoneDateTime = eshterak;
        getApplicationComponent().MyDatabase().usersPointDao().insertUsersPoint(userPoints);
        binding.mapView.getGraphicsOverlays().remove(pointLayer);
        pointLayer = -1;
        point = null;
        new CustomToast().success(getString(R.string.added_succeed));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}