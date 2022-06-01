package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.helpers.Constants.EXPORT_FRAGMENT;
import static com.leon.initialize_gis.helpers.Constants.POINT_FRAGMENT;
import static com.leon.initialize_gis.helpers.Constants.REPORT_FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.activities.MainActivity;
import com.leon.initialize_gis.databinding.FragmentHomeBinding;
import com.leon.initialize_gis.helpers.Constants;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {
        binding.cardViewExit.setOnClickListener(this);
        binding.cardViewExport.setOnClickListener(this);
        binding.cardViewLocation.setOnClickListener(this);
        binding.cardViewReport.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.card_view_exit) {
            Constants.exit = true;
            requireActivity().finishAffinity();
        } else if (id == R.id.card_view_export) {
            ((MainActivity) requireActivity()).displayView(EXPORT_FRAGMENT);
        } else if (id == R.id.card_view_location) {
            ((MainActivity) requireActivity()).displayView(POINT_FRAGMENT);
        } else if (id == R.id.card_view_report) {
            ((MainActivity) requireActivity()).displayView(REPORT_FRAGMENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}