package com.leon.initialize_gis.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.adapters.SpinnerCustomAdapter;
import com.leon.initialize_gis.databinding.FragmentExportBinding;
import com.leon.initialize_gis.utils.BackUp;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;

public class ExportFragment extends Fragment implements View.OnClickListener {
    private FragmentExportBinding binding;
    private String[] items;

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
        initializeSpinner();
        binding.buttonUpload.setOnClickListener(this);
        binding.textViewStart.setOnClickListener(this);
        binding.textViewEnd.setOnClickListener(this);
    }

    private void initializeSpinner() {
        items = getResources().getStringArray(R.array.export_extension);
        final SpinnerCustomAdapter adapter = new SpinnerCustomAdapter(getActivity(), items);
        binding.spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.button_upload) {
            if (validTextView(binding.textViewStart) && validTextView(binding.textViewEnd))
                new BackUp(requireActivity(), binding.textViewStart.getText().toString(),
                        binding.textViewEnd.getText().toString(),
                        items[binding.spinner.getSelectedItemPosition()]).execute(requireActivity());
        } else if (id == R.id.text_view_start || id == R.id.text_view_end) {
            showDatePicker(view);
        }
    }

    private void showDatePicker(final View textView) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
        datePickerDialog.setDisableDaysAgo(false);
        datePickerDialog.setTextSizeTitle(10.0f);
        datePickerDialog.setTextSizeWeek(12.0f);
        datePickerDialog.setTextSizeDate(14.0f);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setOnSingleDateSelectedListener(date ->
                ((TextView) textView).setText(date.getPersianShortDate()));
        datePickerDialog.showDialog();
    }

    private boolean validTextView(final TextView textView) {
        if (textView.getText().toString().isEmpty()) {
            textView.setError(getString(R.string.error_empty));
            textView.requestFocus();
            return false;
        }
        return true;
    }
}