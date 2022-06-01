package com.leon.initialize_gis.activities;

import static com.leon.initialize_gis.helpers.Constants.EXIT_POSITION;
import static com.leon.initialize_gis.helpers.Constants.EXPORT_FRAGMENT;
import static com.leon.initialize_gis.helpers.Constants.HOME_FRAGMENT;
import static com.leon.initialize_gis.helpers.Constants.POINT_FRAGMENT;
import static com.leon.initialize_gis.helpers.Constants.POSITION;
import static com.leon.initialize_gis.helpers.Constants.REPORT_FRAGMENT;
import static com.leon.initialize_gis.helpers.MyApplication.getContext;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.adapters.RecyclerItemClickListener;
import com.leon.initialize_gis.base_items.BaseActivity;
import com.leon.initialize_gis.databinding.ActivityMainBinding;
import com.leon.initialize_gis.fragments.LocationPointFragment;
import com.leon.initialize_gis.fragments.ExportFragment;
import com.leon.initialize_gis.fragments.HomeFragment;
import com.leon.initialize_gis.fragments.ReportFragment;
import com.leon.initialize_gis.helpers.Constants;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;


    @Override
    protected void initialize() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        final View childLayout = binding.getRoot();
        final ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        displayView(HOME_FRAGMENT);
        setOnDrawerItemClick();
    }

    public void displayView(int position) {
        final String tag = Integer.toString(position);
        if (getFragmentManager().findFragmentByTag(tag) != null && getFragmentManager().findFragmentByTag(tag).isVisible()) {
            return;
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit,
                R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.replace(binding.containerBody.getId(), getFragment(position), tag);
        if (position != HOME_FRAGMENT) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case REPORT_FRAGMENT:
                setTitle(R.string.reports);
                return ReportFragment.newInstance();
            case POINT_FRAGMENT:
                setTitle(R.string.location);
                return LocationPointFragment.newInstance();
            case EXPORT_FRAGMENT:
                setTitle(R.string.export);
                return ExportFragment.newInstance();
            case HOME_FRAGMENT:
            default:
                setTitle(R.string.home);
                return HomeFragment.newInstance();
        }
    }

    private void setOnDrawerItemClick() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(),
                        recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if (position == EXIT_POSITION) {
                            Constants.exit = true;
                            finishAffinity();
                        } else if (POSITION != position) {
                            POSITION = position;
                            displayView(POSITION);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }
}