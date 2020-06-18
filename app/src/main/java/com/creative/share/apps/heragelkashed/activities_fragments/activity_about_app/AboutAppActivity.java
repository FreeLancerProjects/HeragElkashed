package com.creative.share.apps.heragelkashed.activities_fragments.activity_about_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.BuildConfig;
import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.heragelkashed.databinding.ActivityAboutAppBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AboutModel;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAppActivity extends AppCompatActivity implements Listeners.BackListener ,Listeners.AboutListener{
    private ActivityAboutAppBinding binding;
    private String lang;
    private AboutModel aboutModel;





    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_app);
        initView();
    }




    private void initView()
    {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setAboutListener(this);
        binding.tvVersion.setText(String.format("%s %s","Version : ", BuildConfig.VERSION_NAME));
        getAppData();






    }

    private void getAppData() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .aboutApp()
                    .enqueue(new Callback<AboutModel>() {
                        @Override
                        public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                aboutModel = response.body();
                            }else
                            {

                                if (response.code() == 500) {
                                    Toast.makeText(AboutAppActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(AboutAppActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AboutModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(AboutAppActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(AboutAppActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void back() {
        finish();
    }


    @Override
    public void call() {

        if (aboutModel!=null&&!aboutModel.getPhone_number().isEmpty()&&!aboutModel.getPhone_number().equals("#"))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+aboutModel.getPhone_number()));
            startActivity(intent);
        }else
            {
                Toast.makeText(this,getString(R.string.no_phone), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void sms() {

        if (aboutModel!=null&&!aboutModel.getPhone_number().isEmpty()&&!aboutModel.getPhone_number().equals("#"))
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+aboutModel.getPhone_number()));
            startActivity(intent);
        }else
        {
            Toast.makeText(this,getString(R.string.no_phone), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void email() {


        if (aboutModel!=null&&!aboutModel.getContact_email().isEmpty()&&!aboutModel.getContact_email().equals("#"))
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{aboutModel.getContact_email()});
            PackageManager pm = getPackageManager();
            List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                    best = info;

                }
            }

            if (best != null) {
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

            }

            startActivity(intent);
        }else
        {
            Toast.makeText(this, R.string.no_email, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void website() {
        if (aboutModel!=null&&!aboutModel.getSite_link().isEmpty()&&!aboutModel.getSite_link().equals("#"))
        {
            Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(aboutModel.getSite_link()));
            startActivity(intent);
        }else
        {
            Toast.makeText(this, R.string.no_website, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void terms() {

        if (aboutModel!=null&&!aboutModel.getTerms().isEmpty()&&!aboutModel.getTerms().equals("#"))
        {
            Intent intent= new Intent(this, TermsActivity.class);
            intent.putExtra("data",aboutModel);
            startActivity(intent);
        }else
            {
                Toast.makeText(this, R.string.no_terms, Toast.LENGTH_SHORT).show();
            }
    }
}
