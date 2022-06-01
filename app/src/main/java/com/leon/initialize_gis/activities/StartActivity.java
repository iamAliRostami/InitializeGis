package com.leon.initialize_gis.activities;

import static com.leon.initialize_gis.helpers.Constants.GPS_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.databinding.ActivityStartBinding;
import com.leon.initialize_gis.fragments.LoginFragment;
import com.leon.initialize_gis.fragments.SplashFragment;

public class StartActivity extends AppCompatActivity implements SplashFragment.Callback {
    private ActivityStartBinding binding;
    private final int SPLASH_FRAGMENT = 0;
    private final int LOGIN_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize() {
        displayView(SPLASH_FRAGMENT);
    }

    private void displayView(int position, boolean... isLogin) {
        final String tag = Integer.toString(position);
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (isLogin.length == 0 && fragment != null && fragment.isVisible()) return;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit,
                R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.replace(binding.containerBody.getId(), getFragment(position), tag);
        if (position != SPLASH_FRAGMENT) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        runOnUiThread(() -> getFragmentManager().executePendingTransactions());
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case LOGIN_FRAGMENT:
                return LoginFragment.newInstance();
            case SPLASH_FRAGMENT:
            default:
                return SplashFragment.newInstance();
        }
    }

    @Override
    public void splashLoaded() {
        displayView(LOGIN_FRAGMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == GPS_CODE)
                displayView(LOGIN_FRAGMENT, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
    }
}