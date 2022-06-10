package com.leon.initialize_gis.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.adapters.SpinnerCustomAdapter;
import com.leon.initialize_gis.databinding.FragmentExportBinding;
import com.leon.initialize_gis.utils.ExportExcel;
import com.leon.initialize_gis.utils.custom_dialog.LovelyTextInputDialog;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import lib.folderpicker.FolderPicker;

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
            if (validTextView(binding.textViewStart) && validTextView(binding.textViewEnd)) {
                final Intent intent = new Intent(requireContext(), FolderPicker.class);
                intent.putExtra("title", "پوشه ی خروجی (غیرسیستمی) را انتخاب کنید");
                intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                directoryPickerResultLauncher.launch(intent);
            }
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

    @SuppressLint("SimpleDateFormat")
    private final ActivityResultLauncher<Intent> directoryPickerResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            final String path = result.getData().getExtras().getString("data");
                            insertCustomName(path);
                        }
                    });

    private void insertCustomName(final String path) {
        @SuppressLint("SimpleDateFormat") final String child =
                (new SimpleDateFormat(getString(R.string.save_format_name_melli))).format(new Date());
        final LovelyTextInputDialog lovelyTextInputDialog = new LovelyTextInputDialog(requireContext());
        lovelyTextInputDialog/*.setTopColorRes(R.color.yellow)
                .setTopTitleColorRes(R.color.white)
                .setTopTitle(R.string.file_name)
                .setTitle()
                .setMessage()*/
                .setCancelable(false)
                .setInputFilter(R.string.error_empty, text ->
                        lovelyTextInputDialog.getEditTextNumber().getText().toString().isEmpty())
                .setInitialInput("UsersPoints_".concat(child))
                .setConfirmButton(R.string.confirm, text -> {
                    final InputMethodManager imm = (InputMethodManager) requireContext()
                            .getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    new ExportExcel(requireActivity(), binding.textViewStart.getText().toString(),
                            binding.textViewEnd.getText().toString(),
                            items[binding.spinner.getSelectedItemPosition()], path,
                            lovelyTextInputDialog.getEditTextNumber().getText().toString().concat("."))
                            .execute(requireActivity());
                })
                .setNegativeButton(R.string.close, v -> {
                    final InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                });
        lovelyTextInputDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}