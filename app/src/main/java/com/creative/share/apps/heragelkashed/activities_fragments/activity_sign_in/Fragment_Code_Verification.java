package com.creative.share.apps.heragelkashed.activities_fragments.activity_sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
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
import com.creative.share.apps.heragelkashed.databinding.FragmentCodeVerificationBinding;
import com.creative.share.apps.heragelkashed.models.LoginModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Code_Verification extends Fragment {
    private static final String TAG = "DATA";
    private SignInActivity activity;
    private FragmentCodeVerificationBinding binding;
    private CountDownTimer timer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private Preferences preferences;
    private UserModel userModel;
    private boolean canResend;
    private String lang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {

        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        mAuth = FirebaseAuth.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.btnConfirm.setOnClickListener(v -> checkData());

        binding.btnResend.setOnClickListener(v -> {

            if (canResend) {
                sendSmsCode();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);

           // Log.e("user_id", userModel.getId() + "__");
        }
        Log.e("ldll",userModel.getMobile_number());

        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
            if (!code.isEmpty()) {
                binding.edtCode.setError(null);
                Common.CloseKeyBoard(activity, binding.edtCode);
                checkValidCode(code);
            } else {
                binding.edtCode.setError(getString(R.string.field_req));
            }

        });
        sendSmsCode();
        startTimer();
    }

    private void sendSmsCode() {

        startTimer();

        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                verificationId = verification_id;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(activity, e.getMessage());
                } else {
                    Common.CreateDialogAlert(activity, getString(R.string.failed));

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        userModel.getMobile_code().replace("+","00")+userModel.getMobile_number(),
                        60,
                        TimeUnit.SECONDS,
                        activity,
                        mCallBack

                );


    }

    private void startTimer() {
        binding.btnResend.setEnabled(false);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.btnResend.setText(time);
                canResend = false;

            }

            @Override
            public void onFinish() {
                binding.btnResend.setText("00:00");
                binding.btnResend.setEnabled(true);
                canResend = true;

            }
        };

        timer.start();
    }


    private void checkValidCode(String code) {

        if (verificationId != null) {
            Log.e("1", "1");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        login();
//                        preferences.create_update_userData(activity, userModel);
//                        preferences.createSession(activity, Tags.session_login);
//
//                        if (!activity.isOut) {
//                            Intent intent = new Intent(activity, HomeActivity.class);
//                            startActivity(intent);
//                        }
//
//                        activity.finish();
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                    Common.CreateDialogAlert(activity, e.getMessage());
                } else {
                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("2", "2");
            //  login();
        }

    }

    private void checkData() {
        String code = binding.edtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            Common.CloseKeyBoard(activity, binding.edtCode);
            //ValidateCode(code);
            checkValidCode(code);
        } else {
            binding.edtCode.setError(getString(R.string.field_req));
        }
    }

//    private void ValidateCode(String code) {
//        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//
//        try {
//
//            Api.getService(Tags.base_url)
//                    .confirmCode(userModel.getId(), code)
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful() && response.body() != null) {
//                                preferences.create_update_userData(activity, response.body());
//                                preferences.createSession(activity, Tags.session_login);
//
//                                if (!activity.isOut) {
//                                    Intent intent = new Intent(activity, HomeActivity.class);
//                                    startActivity(intent);
//                                }
//
//                                activity.finish();
//
//                            } else {
//
//                                if (response.code() == 422) {
//                                    Toast.makeText(activity, "Error Validation", Toast.LENGTH_SHORT).show();
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                } else if (response.code() == 401) {
//                                    Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error", response.code() + "_" + response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                                Log.e("rrr", e.getMessage() + "_");
//
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            dialog.dismiss();
//            Log.e("dddd", e.getMessage() + "_");
//        }
//    }

//    private void startCounter() {
//        countDownTimer = new CountDownTimer(60000, 1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                canResend = false;
//
//                int AllSeconds = (int) (millisUntilFinished / 1000);
//                int seconds = AllSeconds % 60;
//
//
//                binding.btnResend.setText("00:" + seconds);
//            }
//
//            @Override
//            public void onFinish() {
//                canResend = true;
//                binding.btnConfirm.setText(getString(R.string.resend));
//            }
//        }.start();
//    }

//    private void reSendSMSCode() {
//        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//            Api.getService(Tags.base_url)
//                    .resendCode(userModel.getId())
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//
//                            dialog.dismiss();
//
//                            if (response.isSuccessful()) {
//                                startTimer();
//
//                            } else {
//                                try {
//                                    Log.e("error_code", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                if (response.code() == 422) {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage() != null) {
//                                    Log.e("error", t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
//                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                                Log.e("Exe", e.getMessage() + "__");
//                                dialog.dismiss();
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            dialog.dismiss();
//            Log.e("ddd", e.getMessage() + "__");
//        }
//
//    }
private void login() {

    ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
    dialog.setCancelable(false);
    dialog.show();
    try {

        Api.getService(Tags.base_url)
                .login(userModel.getMobile_code(),userModel.getMobile_number(),1)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            preferences.create_update_userData(activity,response.body());
                            preferences.createSession(activity, Tags.session_login);

                            if (!activity.isOut)
                            {
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                            }


                            activity.finish();

                        }else
                        {

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            }else if (response.code()==401)
                            {
//                                try {
//                                   // UserModel userModel = new Gson().fromJson(response.errorBody().string(),UserModel.class);
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }

                            }else if (response.code()==402)
                            {
                                Toast.makeText(activity, R.string.blokced, Toast.LENGTH_SHORT).show();

                            }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage()!=null)
                            {
                                Log.e("error",t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                {
                                    Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }catch (Exception e){}
                    }
                });
    }catch (Exception e){
        dialog.dismiss();

    }
}


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
