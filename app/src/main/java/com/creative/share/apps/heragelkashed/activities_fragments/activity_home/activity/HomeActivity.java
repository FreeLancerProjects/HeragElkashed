package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_about_app.AboutAppActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_add_ads.AddAdsActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_add_company.AddCompanyActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Chat;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Favourite;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_My_Adversiment;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.fragment_company.Fragment_Companies_Feature;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_notification.NotificationActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_search.SearchActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sign_in.SignInActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_slider_details.SliderDetailsActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_category.SubCategoryActivity;
import com.creative.share.apps.heragelkashed.adapter.MainCategoryNavParentAdapter;
import com.creative.share.apps.heragelkashed.databinding.DialogLanguageBinding;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.MessageModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private AHBottomNavigation ahBottomNav;
    private String lang;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Companies_Feature fragment_companiesFeature;
    private Fragment_My_Adversiment fragment_my_adversiment;
    private Fragment_Favourite fragment_favourite;
    private Fragment_Chat fragment_chat;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView arrow1, arrow2, arrow3, arrow4, arrow5;
    private LinearLayout llChangeLanguage,llAddAd;
    private RecyclerView recView;
    private ProgressBar progBar;
    private TextView tvNoAds;
    private ImageView imageSearch,imageNotification;
    private Button btnAddAd, btnAddCompany;
    private LinearLayout llLogout,llShare,llAbout;
    private List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList;
    private MainCategoryNavParentAdapter adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ad_id")) {
            int ad_id = intent.getIntExtra("ad_id", 0);
            Intent intent2 = new Intent(this, SliderDetailsActivity.class);
            intent2.putExtra("ad_id", ad_id);
            startActivity(intent2);
        }else if (intent != null && intent.hasExtra("not"))
        {
            removeNotificationFromBackGround();
            Intent intent3 = new Intent(this, NotificationActivity.class);
            startActivity(intent3);

        }


    }


    private void initView()
    {
        EventBus.getDefault().register(this);
        fragmentManager = getSupportFragmentManager();
        mainCategoryModelList = new ArrayList<>();

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        toolbar = findViewById(R.id.toolbar);
        ahBottomNav = findViewById(R.id.ah_bottom_nav);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /////////////////////////////////////////////////////
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        arrow3 = findViewById(R.id.arrow3);
        arrow4 = findViewById(R.id.arrow4);
        arrow5 = findViewById(R.id.arrow5);

        if (lang.equals("ar")) {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);

        }

        btnAddAd = findViewById(R.id.btnAddAd);
        btnAddCompany = findViewById(R.id.btnAddCompany);
        llChangeLanguage = findViewById(R.id.llChangeLanguage);
        llAddAd = findViewById(R.id.llAddAd);

        llLogout = findViewById(R.id.llLogout);
        llShare = findViewById(R.id.llShare);
        llAbout = findViewById(R.id.llAbout);

        progBar = findViewById(R.id.progBar);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);

        recView = findViewById(R.id.recView);
        tvNoAds = findViewById(R.id.tvNoAds);
        imageSearch = findViewById(R.id.imageSearch);
        imageNotification = findViewById(R.id.imageNotification);


        recView.setNestedScrollingEnabled(true);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainCategoryNavParentAdapter(this, mainCategoryModelList);
        recView.setAdapter(adapter);
        setUpBottomNavigation();

        imageSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        imageNotification.setOnClickListener(view -> {
            userModel = preferences.getUserData(this);

            if (userModel==null)
            {
                navigateToSignInActivity2();
            }else
            {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            }

        });

        llChangeLanguage.setOnClickListener(view -> CreateLangDialog());


        llLogout.setOnClickListener(view -> logOut());

        llAbout.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
        });

        llShare.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);
            String share_text = getString(R.string.share_text)+"\n"+"https://play.google.com/store/apps/details?id="+getPackageName();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,share_text);
            startActivity(sendIntent);

        });

        btnAddCompany.setOnClickListener(view -> {
            userModel = preferences.getUserData(this);
            if (userModel==null)
            {
                navigateToSignInActivity2();
            }else
                {
                    Intent intent = new Intent(this, AddCompanyActivity.class);
                    startActivity(intent);
                }
        });

        btnAddAd.setOnClickListener(view -> {
            btnAddAd.setEnabled(false);
            userModel = preferences.getUserData(this);
            if (userModel==null)
            {
                navigateToSignInActivity2();
            }else
            {
                Intent intent = new Intent(this, AddAdsActivity.class);
                startActivityForResult(intent,1);
            }
            btnAddAd.setEnabled(true);

        });

        llAddAd.setOnClickListener(view -> {
            drawer.closeDrawer(GravityCompat.START);
            userModel = preferences.getUserData(this);
            if (userModel==null)
            {
                navigateToSignInActivity2();
            }else
            {
                Intent intent = new Intent(this, AddAdsActivity.class);
                startActivityForResult(intent,1);
            }

        });

        if (userModel!=null)
        {
            updateToken();
        }
        removeNotificationFromBackGround();
        getMainCategory();


    }

    private void removeNotificationFromBackGround() {

        new Handler()
                .postDelayed(() -> {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (manager!=null)
                    {
                        manager.cancel(12352);
                    }
                },100);
    }

    private void updateToken() {

        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        String token = task.getResult().getToken();

                        try {

                            Api.getService(Tags.base_url)
                                    .updateToken(userModel.getId(),token,1)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful() && response.body() != null )
                                            {
                                                Log.e("token","updated successfully");
                                            } else {
                                                try {

                                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("error", t.getMessage());
                                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {


                        }

                    }
                });
    }

    private void logOut() {
        drawer.closeDrawer(GravityCompat.START);
        if (userModel==null)
        {
            navigateToSignInActivity();
        }else
            {
                preferences.clear(this);
                navigateToSignInActivity();
            }
    }

    private void getMainCategory() {

        try {

            Api.getService(Tags.base_url)
                    .getMainCategory()
                    .enqueue(new Callback<MainCategoryDataModel>() {
                        @Override
                        public void onResponse(Call<MainCategoryDataModel> call, Response<MainCategoryDataModel> response) {
                            progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                mainCategoryModelList.clear();
                                mainCategoryModelList.addAll(response.body().getData());
                                if (fragment_main != null && fragment_main.isAdded()) {
                                    fragment_main.getMainCategory(response.body().getData(), 200);
                                }
                                if (mainCategoryModelList.size() > 0) {
                                    tvNoAds.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    tvNoAds.setVisibility(View.VISIBLE);

                                }

                            } else {
                                if (fragment_main != null && fragment_main.isAdded()) {
                                    fragment_main.getMainCategory(null, response.code());
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MainCategoryDataModel> call, Throwable t) {
                            try {
                                if (fragment_main != null && fragment_main.isAdded()) {
                                    fragment_main.getMainCategory(null, 0);
                                }
                                progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {


        }
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.Categories), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.companies), R.drawable.compay);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_Advdersiment), R.drawable.ic_user);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.favourite), R.drawable.ic_favorite);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.chat), R.drawable.ic_chat);

        ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ahBottomNav.setTitleTextSizeInSp(14, 12);
        ahBottomNav.setForceTint(true);
        ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
        ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));

        ahBottomNav.addItem(item1);
        ahBottomNav.addItem(item2);
        ahBottomNav.addItem(item3);
        ahBottomNav.addItem(item4);
        ahBottomNav.addItem(item5);

        ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    DisplayFragmentMain();
                    break;
                case 1:

                    DisplayFragmentCompanies();
                    break;
                case 2:
                    userModel = preferences.getUserData(this);

                    if (userModel==null)
                    {
                        navigateToSignInActivity2();
                    }else
                        {
                            DisplayFragmentMyAds();

                        }


                    break;
                case 3:

                    userModel = preferences.getUserData(this);

                    if (userModel==null)
                    {
                        navigateToSignInActivity2();
                    }else
                    {
                        DisplayFragmentFavourite();

                    }

                    break;
                case 4:
                    userModel = preferences.getUserData(this);

                    if (userModel==null)
                    {
                        navigateToSignInActivity2();
                    }else
                    {
                        DisplayFragmentChat();

                    }
                    break;

            }
            return false;
        });

        ahBottomNav.setCurrentItem(0, false);
        DisplayFragmentMain();


    }

    public void DisplayFragmentMain() {

        btnAddAd.setVisibility(View.VISIBLE);
        btnAddCompany.setVisibility(View.GONE);
        if (fragment_main == null) {
            fragment_main = Fragment_Main.newInstance();
        }
        if (fragment_companiesFeature != null && fragment_companiesFeature.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_companiesFeature).commit();
        }
        if (fragment_my_adversiment != null && fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_my_adversiment).commit();
        }
        if (fragment_favourite != null && fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favourite).commit();
        }
        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        ahBottomNav.setCurrentItem(0, false);

    }

    public void DisplayFragmentCompanies() {
        btnAddAd.setVisibility(View.GONE);
        btnAddCompany.setVisibility(View.VISIBLE);
        if (fragment_companiesFeature == null) {
            fragment_companiesFeature = Fragment_Companies_Feature.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_my_adversiment != null && fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_my_adversiment).commit();
        }
        if (fragment_favourite != null && fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favourite).commit();
        }
        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_companiesFeature.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_companiesFeature).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_companiesFeature, "fragment_companies_feature").addToBackStack("fragment_companies_feature").commit();

        }
        ahBottomNav.setCurrentItem(1, false);


    }

    public void DisplayFragmentMyAds() {
        btnAddAd.setVisibility(View.VISIBLE);
        btnAddCompany.setVisibility(View.GONE);

        if (fragment_my_adversiment == null) {
            fragment_my_adversiment = Fragment_My_Adversiment.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_companiesFeature != null && fragment_companiesFeature.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_companiesFeature).commit();
        }
        if (fragment_favourite != null && fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favourite).commit();
        }
        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_my_adversiment).commit();
            fragment_my_adversiment.getMyAds();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_my_adversiment, "fragment_my_adversiment").addToBackStack("fragment_my_adversiment").commit();

        }
        ahBottomNav.setCurrentItem(2, false);


    }

    public void DisplayFragmentFavourite() {
        btnAddAd.setVisibility(View.VISIBLE);
        btnAddCompany.setVisibility(View.GONE);
        if (fragment_favourite == null) {
            fragment_favourite = Fragment_Favourite.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_my_adversiment != null && fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_my_adversiment).commit();
        }
        if (fragment_companiesFeature != null && fragment_companiesFeature.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_companiesFeature).commit();
        }
        if (fragment_chat != null && fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_chat).commit();
        }
        if (fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_favourite).commit();
            fragment_favourite.getFavoriteAds();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_favourite, "fragment_favourite").addToBackStack("fragment_favourite").commit();

        }
        ahBottomNav.setCurrentItem(3, false);


    }

    public void DisplayFragmentChat() {
        btnAddAd.setVisibility(View.VISIBLE);
        btnAddCompany.setVisibility(View.GONE);

        if (fragment_chat == null) {
            fragment_chat = Fragment_Chat.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_my_adversiment != null && fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_my_adversiment).commit();
        }
        if (fragment_companiesFeature != null && fragment_companiesFeature.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_companiesFeature).commit();
        }
        if (fragment_favourite != null && fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favourite).commit();
        }
        if (fragment_chat.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_chat).commit();
            fragment_chat.getRooms();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_chat, "fragment_chat").addToBackStack("fragment_chat").commit();

        }
        ahBottomNav.setCurrentItem(4, false);


    }

    private void navigateToSignInActivity2()
    {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("out",true);
        startActivity(intent);
    }

    private void CreateLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("ar"), 1000);
        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> refreshActivity("en"), 1000);
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    private void refreshActivity(String lang) {
        drawer.closeDrawer(GravityCompat.START);
        new Handler()
                .postDelayed(() -> {
                    preferences.selectedLanguage(HomeActivity.this, lang);
                    Paper.book().write("lang", lang);
                    LanguageHelper.setNewLocale(HomeActivity.this, lang);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel messageModel)
    {
        if (fragment_chat!=null&&fragment_chat.isAdded())
        {
            fragment_chat.getRooms();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
                if (userModel == null) {
                    navigateToSignInActivity();
                } else {
                    finish();
                }
            } else {
                DisplayFragmentMain();
            }
        }
    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


    public void setSubCategoryItem(MainCategoryDataModel.SubCategoryModel subCategoryModel) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("sub_id", subCategoryModel.getId());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            DisplayFragmentMyAds();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}
