package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.initialize_gis.adapters.UsersPointsAdapter;
import com.leon.initialize_gis.databinding.FragmentReportBinding;
import com.leon.initialize_gis.tables.UsersPoints;

import java.util.ArrayList;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;

    public ReportFragment() {
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        final ArrayList<UsersPoints> usersPoints = new ArrayList<>(getApplicationComponent()
                .MyDatabase().usersPointDao().getUsersPoints());
        if (usersPoints.isEmpty()) {
            emptyAdapter(true);
        } else {
            initializeAdapter(usersPoints);
            setOnTextSearchChangedListener();
        }
    }

    private void initializeAdapter(final ArrayList<UsersPoints> usersPoints) {
        final UsersPointsAdapter adapter = new UsersPointsAdapter(usersPoints, requireContext());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewUsersPoints.setLayoutManager(linearLayoutManager);
        binding.recyclerViewUsersPoints.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0)
                    emptyAdapter(false);
                else {
                    binding.textViewNotFound.setVisibility(View.GONE);
                    binding.recyclerViewUsersPoints.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void emptyAdapter(final boolean isEmpty) {
        binding.textViewNotFound.setVisibility(View.VISIBLE);
        binding.recyclerViewUsersPoints.setVisibility(View.GONE);
        if (isEmpty)
            binding.linearLayoutTitle.setVisibility(View.GONE);
    }

    private void setOnTextSearchChangedListener() {
        if (binding.layout.getEditText() != null)
            binding.layout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (binding.recyclerViewUsersPoints.getAdapter() != null) {
                        final String search = binding.layout.getEditText().getText().toString()
                                .toLowerCase(Locale.getDefault());
                        ((UsersPointsAdapter) binding.recyclerViewUsersPoints.getAdapter()).filter(search);
                    }
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}