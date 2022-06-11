package com.leon.initialize_gis.fragments;

import static com.leon.initialize_gis.enums.SharedReferenceKeys.PASSWORD;
import static com.leon.initialize_gis.enums.SharedReferenceKeys.USERNAME;
import static com.leon.initialize_gis.helpers.MyApplication.checkLicense;
import static com.leon.initialize_gis.helpers.MyApplication.getAndroidVersion;
import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;
import static com.leon.initialize_gis.helpers.MyApplication.setActivityComponent;
import static com.leon.initialize_gis.utils.Crypto.decrypt;
import static com.leon.initialize_gis.utils.Crypto.encrypt;
import static com.leon.initialize_gis.utils.PermissionManager.checkNetwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.leon.initialize_gis.BuildConfig;
import com.leon.initialize_gis.R;
import com.leon.initialize_gis.activities.MainActivity;
import com.leon.initialize_gis.databinding.FragmentLoginBinding;
import com.leon.initialize_gis.helpers.Constants;
import com.leon.initialize_gis.interfaces.ISharedPreference;
import com.leon.initialize_gis.utils.CustomToast;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private String username, password;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        setActivityComponent(requireActivity());
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        if (!checkNetwork(requireContext()))
            new CustomToast().warning(getString(R.string.turn_internet_on), Toast.LENGTH_LONG);

        binding.textViewVersion.setText(getString(R.string.version).concat(" ").concat(getAndroidVersion())
                .concat(" *** ").concat(BuildConfig.VERSION_NAME));
        loadPreference();
        binding.imageViewPassword.setImageResource(R.drawable.img_password);
        binding.imageViewPerson.setImageResource(R.drawable.img_profile);
        binding.imageViewUsername.setImageResource(R.drawable.img_user);

        setOnButtonLoginClickListener();
        setOnImageViewPasswordClickListener();
        setEditTextUsernameOnFocusChangeListener();
        setEditTextPasswordOnFocusChangeListener();
    }

    private void setEditTextUsernameOnFocusChangeListener() {
        binding.editTextUsername.setOnFocusChangeListener((view, b) -> {
            binding.editTextUsername.setHint("");
            if (b) {
                binding.linearLayoutUsername.setBackground(ContextCompat
                        .getDrawable(requireContext(), R.drawable.border_black_2));
                binding.editTextPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            } else {
                binding.linearLayoutUsername.setBackground(ContextCompat
                        .getDrawable(requireContext(), R.drawable.border_gray_2));
                binding.editTextPassword.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2));
            }
        });
    }

    private void setEditTextPasswordOnFocusChangeListener() {
        binding.editTextPassword.setOnFocusChangeListener((view, b) -> {
            binding.editTextPassword.setHint("");
            if (b) {
                binding.linearLayoutPassword.setBackground(ContextCompat
                        .getDrawable(requireContext(), R.drawable.border_black_2));
                binding.editTextPassword.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.black));
            } else {
                binding.linearLayoutPassword.setBackground(ContextCompat.getDrawable(requireContext(),
                        R.drawable.border_gray_2));
                binding.editTextPassword.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.gray_2));
            }
        });
    }

    private void setOnImageViewPasswordClickListener() {
        binding.imageViewPassword.setOnClickListener(view -> {
            if (binding.editTextPassword.getInputType() != InputType.TYPE_CLASS_TEXT) {
                binding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            } else
                binding.editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });
    }

    private void setOnButtonLoginClickListener() {
        binding.buttonLogin.setOnClickListener(v -> attempt());
    }

    private void attempt() {
        boolean cancel = false;
        if (binding.editTextUsername.getText().length() < 1) {
            binding.editTextUsername.setError(getString(R.string.error_empty));
            binding.editTextPassword.requestFocus();
            cancel = true;
        }
        if (!cancel && binding.editTextPassword.getText().length() < 1) {
            binding.editTextPassword.setError(getString(R.string.error_empty));
            binding.editTextPassword.requestFocus();
            cancel = true;
        }
        if (!cancel && !checkLicense()) {
            new CustomToast().warning(getString(R.string.expired_trial));
            cancel = true;
        }
        if (!cancel) {
            username = binding.editTextUsername.getText().toString();
            password = binding.editTextPassword.getText().toString();
            if (username.equals(Constants.USERNAME) && password.equals(Constants.PASSWORD)) {
                if (binding.checkBoxSave.isChecked())
                    savePreference();
                final Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else new CustomToast().warning(getString(R.string.user_not_found));
        }
    }

    private void savePreference() {
        final ISharedPreference sharedPreference = getApplicationComponent().SharedPreferenceModel();
        sharedPreference.putData(USERNAME.getValue(), username);
        sharedPreference.putData(PASSWORD.getValue(), encrypt(password));
    }

    private void loadPreference() {
        ISharedPreference sharedPreference = getApplicationComponent().SharedPreferenceModel();
        if (sharedPreference.checkIsNotEmpty(USERNAME.getValue()) &&
                sharedPreference.checkIsNotEmpty(PASSWORD.getValue())) {
            binding.editTextUsername.setText(sharedPreference.getStringData(
                    USERNAME.getValue()));
            binding.editTextPassword.setText(decrypt(sharedPreference
                    .getStringData(PASSWORD.getValue())));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}