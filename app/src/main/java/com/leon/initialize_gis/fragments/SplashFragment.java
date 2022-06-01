package com.leon.initialize_gis.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.activities.StartActivity;
import com.leon.initialize_gis.databinding.FragmentSplashBinding;


public class SplashFragment extends Fragment {
    private FragmentSplashBinding binding;
    private Callback startActivity;

    public SplashFragment() {
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        startSplash();
        return binding.getRoot();
    }

    private void startSplash() {
        binding.imageViewSplashScreen.setImageResource(R.drawable.img_splash);
        binding.shimmerViewContainer.startShimmer();
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity.splashLoaded();
                }
            }
        };
        timerThread.start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof StartActivity)
            startActivity = (StartActivity) context;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface Callback {
        void splashLoaded();
    }
}