package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.enums.MapType.VECTOR;
import static com.leon.initialize_gis.helpers.MyApplication.checkLicense;
import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;
import static com.leon.initialize_gis.helpers.MyApplication.getLocationTracker;
import static com.leon.initialize_gis.utils.gis.GisTools.createGraphicPicturePoint;
import static com.leon.initialize_gis.utils.gis.GisTools.createGraphicTextPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.android.material.snackbar.Snackbar;
import com.leon.initialize_gis.R;
import com.leon.initialize_gis.databinding.FragmentLocationPointBinding;
import com.leon.initialize_gis.tables.UsersPoints;
import com.leon.initialize_gis.utils.CustomToast;
import com.leon.initialize_gis.utils.gis.GisTools;
import com.leon.initialize_gis.utils.gis.GoogleMapLayer;

public class LocationPointFragment extends Fragment implements View.OnClickListener {
    private FragmentLocationPointBinding binding;
    private int pointLayer = -1, currentLocationLayer;
    private boolean isLong;
    private Point graphicPoint;

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
        try {
            AsyncTask.execute(() -> {
                while (getLocationTracker(requireActivity()).getLocation() == null)
                    binding.progressBar.setVisibility(View.VISIBLE);
                showCurrentLocation(getLocationTracker(requireActivity()).getLatitude()
                        , getLocationTracker(requireActivity()).getLongitude());
                requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
            });
        } catch (Exception e) {
            new CustomToast().warning(e.toString());
        }
    }

    private void showCurrentLocation(final double latitude, final double longitude) {
        binding.mapView.setViewpoint(new Viewpoint(latitude, longitude, 3600));
        final GraphicsOverlay graphicOverlay = new GraphicsOverlay();
        graphicOverlay.getGraphics().add(createGraphicTextPoint(latitude, longitude, getString(R.string.your_place)));
        graphicOverlay.getGraphics().add(createGraphicPicturePoint(latitude, longitude,
                com.esri.arcgisruntime.R.drawable.arcgisruntime_location_display_compass_symbol));
        binding.mapView.getGraphicsOverlays().add(graphicOverlay);
        currentLocationLayer = binding.mapView.getGraphicsOverlays().size() - 1;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onMapClickListener() {
        binding.mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(requireContext(), binding.mapView) {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                isLong = true;
                binding.mapView.getGraphicsOverlays().get(currentLocationLayer).setVisible(false);
                if (pointLayer >= 0) {
                    binding.mapView.getGraphicsOverlays().remove(pointLayer);
                    pointLayer = -1;
                }
            }

            @Override
            public boolean onUp(MotionEvent e) {
                if (isLong) {
                    binding.mapView.getGraphicsOverlays().get(currentLocationLayer).setVisible(true);
                    graphicPoint = GisTools.getPoint(binding.mapView.screenToLocation(new android.graphics.Point((int) e.getX(),
                            (int) e.getY())));
                    addPoint();
                }
                isLong = false;
                return super.onUp(e);
            }
        });
    }

    private void addPoint() {
        final GraphicsOverlay graphicOverlay = new GraphicsOverlay();
        graphicOverlay.getGraphics().add(createGraphicPicturePoint(graphicPoint.getY(),
                graphicPoint.getX(), R.drawable.img_marker));
        binding.mapView.getGraphicsOverlays().add(graphicOverlay);
        pointLayer = binding.mapView.getGraphicsOverlays().size() - 1;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.fab) {
            if (graphicPoint == null) {
                Snackbar.make(view, getString(R.string.define_location), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (binding.layout.getEditText() != null) {
                if (binding.layout.getVisibility() == View.GONE) {
                    binding.layout.setVisibility(View.VISIBLE);
                    showKeyboard();
                    binding.layout.getEditText().requestFocus();
                } else {
                    final String eshterak = binding.layout.getEditText().getText().toString();
                    if (eshterak.isEmpty()) {
                        binding.layout.getEditText().setError(getString(R.string.error_empty));
                        binding.layout.getEditText().requestFocus();
                    } else if (getApplicationComponent().MyDatabase().usersPointDao().eshterakPointsCounter(eshterak) > 0) {
                        binding.layout.getEditText().setError(getString(R.string.repetitive_eshterak));
                        binding.layout.getEditText().requestFocus();
                    } else if (!checkLicense()) {
                        new CustomToast().warning(getString(R.string.expired_trial));
                    } else {
                        insertPoint(eshterak);
                        binding.layout.getEditText().setText("");
                        binding.layout.setVisibility(View.GONE);
                        binding.mapView.getGraphicsOverlays().remove(pointLayer);
                        pointLayer = -1;
                        graphicPoint = null;
                        hideKeyboard(view);
                    }
                }
            }
        }
    }

    private void hideKeyboard(final View view) {
        final InputMethodManager imm = (InputMethodManager) requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard() {
        final InputMethodManager imm = (InputMethodManager) requireContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    @SuppressLint("SimpleDateFormat")
    private void insertPoint(final String eshterak) {
        final UsersPoints userPoint = new UsersPoints();
        userPoint.eshterak = eshterak;
        userPoint.x = graphicPoint.getX();
        userPoint.y = graphicPoint.getY();
        userPoint.getDateInformation(getLocationTracker(requireActivity()));
        getApplicationComponent().MyDatabase().usersPointDao().insertUsersPoint(userPoint);
        new CustomToast().success(getString(R.string.added_succeed));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}