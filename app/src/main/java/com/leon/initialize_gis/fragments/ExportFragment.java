package com.leon.initialize_gis.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.databinding.FragmentExportBinding;
import com.leon.initialize_gis.utils.BackUp;


public class ExportFragment extends Fragment {
    private FragmentExportBinding binding;

    public static ExportFragment newInstance() {
        return new ExportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExportBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.buttonUpload.setOnClickListener(view -> {
            new BackUp(requireActivity()).execute(requireActivity());
        });
    }
}