package com.creative.share.apps.heragelkashed.activities_fragments.activity_sign_in;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.databinding.DialogAlertBinding;
import com.creative.share.apps.heragelkashed.databinding.FragmentSignInBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.models.LoginModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.google.gson.Gson;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener, Listeners.ShowCountryDialogListener,Listeners.SkipListener, OnCountryPickerListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String lang;
    private CountryPicker countryPicker;
    private LoginModel loginModel;
    private Preferences preferences;

    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        preferences = Preferences.newInstance();
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setLoginModel(loginModel);
        binding.setLoginListener(this);
        binding.setSkipListener(this);
        binding.setShowCountryListener(this);
        binding.setLoginListener(this);
        createCountryDialog();

        if (activity.isOut)
        {
            binding.tvSkip.setVisibility(View.GONE);
        }




    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            if (countryPicker.getCountryFromSIM()!=null)
            {
                updatePhoneCode(countryPicker.getCountryFromSIM());
            }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
            {
                updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
            }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
            {
                updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
            }else
            {
                String code = "+966";
                binding.tvCode.setText(code);
                loginModel.setPhone_code(code.replace("+","00"));

            }
        }catch (Exception e)
        {
            String code = "+966";
            binding.tvCode.setText(code);
            loginModel.setPhone_code(code.replace("+","00"));
        }


    }




    @Override
    public void checkDataLogin() {

        if (loginModel.isDataValid(activity))
        {
            Common.CloseKeyBoard(activity,binding.edtPhone);
            UserModel userModel=new UserModel();
            userModel.setMobile_code(binding.tvCode.getText().toString());
            userModel.setMobile_number(binding.edtPhone.getText().toString());
            //Log.e("lllll",loginModel.getPhone());
            CreateDialogAlert(userModel);
            //login(loginModel);
        }
    }


    private  void CreateDialogAlert(UserModel userModel) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(getString(R.string.you_will_receive_4_digit));
        binding.btnCancel.setOnClickListener(v -> {

            dialog.dismiss();
            activity.displayFragmentVerificationCode(userModel);
        }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    @Override
    public void skip() {
        binding.tvSkip.setEnabled(false);
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();


    }


    @Override
    public void showDialog() {
        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
      updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        loginModel.setPhone_code(country.getDialCode().replace("+","00"));

    }




}
