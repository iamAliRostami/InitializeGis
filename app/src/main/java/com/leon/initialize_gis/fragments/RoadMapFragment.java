package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.enums.BundleEnum.LATITUDE;
import static com.leon.initialize_gis.enums.BundleEnum.LONGITUDE;
import static com.leon.initialize_gis.enums.MapType.VECTOR;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.leon.initialize_gis.R;
import com.leon.initialize_gis.databinding.FragmentRoadMapBinding;
import com.leon.initialize_gis.utils.gis.GoogleMapLayer;

public class RoadMapFragment extends DialogFragment {
    private FragmentRoadMapBinding binding;
    private double latitude;
    private double longitude;

    public static RoadMapFragment newInstance(double longitude, double latitude) {
        final RoadMapFragment fragment = new RoadMapFragment();
        final Bundle args = new Bundle();
        args.putDouble(LONGITUDE.getValue(), longitude);
        args.putDouble(LATITUDE.getValue(), latitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(LATITUDE.getValue());
            longitude = getArguments().getDouble(LONGITUDE.getValue());
            getArguments().clear();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoadMapBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        initializeBaseMap();
    }

    private void initializeBaseMap() {
        binding.mapView.setMap(new ArcGISMap());
        binding.mapView.getMap().getBasemap().getBaseLayers().add(new GoogleMapLayer().createLayer(VECTOR));
        binding.mapView.setViewpoint(new Viewpoint(latitude, longitude, 3600));
        requireActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
        addPoint();
    }

    private void addPoint() {
        final GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        final BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(requireContext(),
                R.drawable.img_marker);
        final PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        final Point graphicPoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        final Graphic graphic = new Graphic(graphicPoint, symbol);
        graphicsOverlay.getGraphics().add(graphic);
        binding.mapView.getGraphicsOverlays().add(graphicsOverlay);
    }


    @Override
    public void onResume() {
        if (getDialog() != null) {
            final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}