package com.leon.initialize_gis.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.databinding.FragmentLocationPointBinding;


public class LocationPointFragment extends Fragment {
    private FragmentLocationPointBinding binding;

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

    }
}