package com.creative.share.apps.heragelkashed.activities_fragments.activity_add_ads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_add_company.FragmentMapTouchListener;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_filter.FilterActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_video.VideoActivity;
import com.creative.share.apps.heragelkashed.adapter.CityAdapter;
import com.creative.share.apps.heragelkashed.adapter.SliderAddAdAdapter;
import com.creative.share.apps.heragelkashed.adapter.SpinnerSubCategoryAdapter;
import com.creative.share.apps.heragelkashed.adapter.SpinnerSubSubCategoryFormAdapter;
import com.creative.share.apps.heragelkashed.adapter.SpinnerTypeAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivityAddAdsBinding;
import com.creative.share.apps.heragelkashed.databinding.DialogAddAddSelectImageBinding;
import com.creative.share.apps.heragelkashed.databinding.DialogCitiesBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdImageVideoModel;
import com.creative.share.apps.heragelkashed.models.AddAdModel;
import com.creative.share.apps.heragelkashed.models.FormDataModel;
import com.creative.share.apps.heragelkashed.models.PlaceGeocodeData;
import com.creative.share.apps.heragelkashed.models.PlaceMapDetailsData;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Listeners.BackListener {
    private ActivityAddAdsBinding binding;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String camera_perm = Manifest.permission.CAMERA;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int read_req = 1;
    private final int camera_req = 2;
    private final int video_req = 3;

    private final int loc_req = 1225;
    private GoogleMap mMap;
    private double lat, lng;
    private Marker marker;
    private final float zoom = 15.6f;
    private String lang;
    private FragmentMapTouchListener fragment;
    private List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private List<FormDataModel.SubCategory> subSubCategoryModelList;
    private List<FormDataModel.TypeModel> typeModelList;
    private List<FormDataModel.CityModel> cityModelList, cityModelListSearch;

    private SpinnerSubCategoryAdapter adapter;
    private CityAdapter cityAdapter;
    private SpinnerTypeAdapter typeAdapter;
    private SpinnerSubSubCategoryFormAdapter subSubCategoryFormAdapter;
    private boolean isFirstTime = true;
    private UserModel userModel;
    private Preferences preferences;
    private List<AdImageVideoModel> adImageVideoModelList;
    private SliderAddAdAdapter sliderAddAdAdapter;
    private View view;
    private Button btnGallery, btnVideo, btnCancel;
    private BottomSheetBehavior behavior;
    private List<FormDataModel.FormModel> formModelList;
    private List<FormDataModel.OptionModel> optionModelList;
    private AddAdModel addAdModel;
    private AlertDialog dialogCity;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ads);
        initView();
    }

    private void initView() {
        addAdModel = new AddAdModel();
        optionModelList = new ArrayList<>();
        formModelList = new ArrayList<>();
        cityModelListSearch = new ArrayList<>();
        adImageVideoModelList = new ArrayList<>();
        subSubCategoryModelList = new ArrayList<>();
        typeModelList = new ArrayList<>();
        cityModelList = new ArrayList<>();
        subCategoryModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setAddAdModel(addAdModel);
        binding.setBackListener(this);
        binding.selectImage.setOnClickListener(view -> CreateSelectImageDialog());

        binding.tabLayout.setupWithViewPager(binding.pager);

        //////////////////////////////////////////////////////
        view = findViewById(R.id.view);
        behavior = BottomSheetBehavior.from(view);

        btnGallery = findViewById(R.id.btnGallery);
        btnVideo = findViewById(R.id.btnVideo);
        btnCancel = findViewById(R.id.btnCancel);


        btnGallery.setOnClickListener(view -> checkReadPermission(read_req));

        btnVideo.setOnClickListener(view -> checkReadPermission(video_req));


        btnCancel.setOnClickListener(view -> closeSheet());

        binding.llAddPhoto.setOnClickListener(view -> CreateSelectImageDialog());

        binding.llDeletePhoto.setOnClickListener(view -> {

            adImageVideoModelList.remove(binding.pager.getCurrentItem());
            addAdModel.setImages(adImageVideoModelList);
            binding.setAddAdModel(addAdModel);
            if (adImageVideoModelList.size() > 0) {
                sliderAddAdAdapter = new SliderAddAdAdapter(this, adImageVideoModelList);
                binding.pager.setAdapter(sliderAddAdAdapter);

            } else {
                binding.selectImageContainer.setVisibility(View.VISIBLE);
                binding.tabContainer.setVisibility(View.GONE);
            }

        });

        binding.llFilter.setOnClickListener(view -> {
            if (formModelList.size() > 0) {
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra("data", (Serializable) formModelList);
                startActivityForResult(intent, 4);

            }
        });
        //////////////////////////////////////////////////////


        binding.imageSearch.setOnClickListener(view -> {
            String query = binding.edtAddress.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                binding.edtAddress.setError(null);
                Search(query);
            } else {
                binding.edtAddress.setError(getString(R.string.field_req));

            }
        });
        adapter = new SpinnerSubCategoryAdapter(subCategoryModelList, this);
        binding.spinnerCategory.setAdapter(adapter);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    clearSpinnerData();
                    addAdModel.setMain_cat_id(0);
                    addAdModel.setCat_id(0);
                    binding.setAddAdModel(addAdModel);

                } else {
                    int cat_id = subCategoryModelList.get(i).getId();

                    addAdModel.setMain_cat_id(subCategoryModelList.get(i).getParent_id());
                    addAdModel.setCat_id(cat_id);
                    binding.setAddAdModel(addAdModel);
                    getFormData(cat_id);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    addAdModel.setAd_type_id(0);
                    binding.setAddAdModel(addAdModel);
                } else {
                    addAdModel.setAd_type_id(typeModelList.get(i).getType_id());
                    binding.setAddAdModel(addAdModel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.llCity.setOnClickListener(view1 -> CreateCityAlert());
/*
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==0)
                {
                    addAdModel.setCity_id(0);
                    binding.setAddAdModel(addAdModel);
                }else
                {
                    addAdModel.setCity_id(cityModelList.get(i).getCity_id());
                    binding.setAddAdModel(addAdModel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

        binding.spinnerSubSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    addAdModel.setSub_cat_id(0);
                    binding.setAddAdModel(addAdModel);
                } else {
                    addAdModel.setSub_cat_id(subSubCategoryModelList.get(i).getSub_id());
                    binding.setAddAdModel(addAdModel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnAddAd.setOnClickListener((v) -> {

            if (addAdModel.isDataValid(this)) {
                addAd();
            }

        });

        new Handler().postDelayed(() -> {
            initMap();
            CheckPermission();
            getSubCategory();
        }, 300);

    }


    private void CreateCityAlert() {
        dialogCity = new AlertDialog.Builder(this)
                .create();

        cityAdapter = new CityAdapter(this, cityModelListSearch);

        DialogCitiesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_cities, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(cityAdapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                if (query.length() > 0) {
                    List<FormDataModel.CityModel> cityModels = searchCity(query);
                    cityModelListSearch.clear();
                    cityModelListSearch.addAll(cityModels);
                    cityAdapter.notifyDataSetChanged();
                } else {
                    cityModelListSearch.clear();
                    cityModelListSearch.addAll(cityModelList);
                    cityAdapter.notifyDataSetChanged();

                }

            }
        });


        dialogCity.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialogCity.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialogCity.setCanceledOnTouchOutside(false);
        dialogCity.setView(binding.getRoot());
        dialogCity.show();
    }

    private List<FormDataModel.CityModel> searchCity(String query) {
        List<FormDataModel.CityModel> cityModelList = new ArrayList<>();

        for (FormDataModel.CityModel cityModel : this.cityModelList) {

            if (cityModel.getAr_name().contains(query)) {
                cityModelList.add(cityModel);
            }
        }

        return cityModelList;
    }

    public void setCityModel(FormDataModel.CityModel cityModel) {
        if (dialogCity != null) {
            dialogCity.dismiss();
        }
        if (cityModel.getCity_id() == 0) {
            binding.tvCity.setText("");

        } else {
            binding.tvCity.setText(cityModel.getAr_name());

        }
        addAdModel.setCity_id(cityModel.getCity_id());
    }

    private void addAd() {

        String video_uri = isListHasVideo();

        if (video_uri.isEmpty()) {
            addAdWithoutVideo();
        } else {
            addAdWithVideo(video_uri);
        }

    }


    private String isListHasVideo() {
        String video_uri = "";
        for (int i = 0; i < adImageVideoModelList.size(); i++) {
            AdImageVideoModel adImageVideoModel = adImageVideoModelList.get(i);
            if (adImageVideoModel.isVideo()) {
                video_uri = adImageVideoModel.getUri();
                adImageVideoModelList.remove(i);
                addAdModel.setImages(adImageVideoModelList);
                binding.setAddAdModel(addAdModel);
                return video_uri;
            }
        }
        return video_uri;
    }

    private void addAdWithVideo(String video_uri) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        RequestBody main_category_part = Common.getRequestBodyText(String.valueOf(addAdModel.getMain_cat_id()));
        RequestBody cat_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getCat_id()));
        RequestBody sub_cat_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getSub_cat_id()));
        RequestBody type_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getAd_type_id()));
        RequestBody city_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getCity_id()));
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody ad_name_part = Common.getRequestBodyText(addAdModel.getAd_name());
        RequestBody ad_details_part = Common.getRequestBodyText(addAdModel.getAd_details());
        RequestBody price_part = Common.getRequestBodyText(String.valueOf(addAdModel.getAd_price()));

        RequestBody ad_address_part = Common.getRequestBodyText(addAdModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(String.valueOf(addAdModel.getLat()));
        RequestBody lng_part = Common.getRequestBodyText(String.valueOf(addAdModel.getLng()));
        MultipartBody.Part video_part = Common.getMultiPartVideo(this, Uri.parse(video_uri), "ad_video");

        Map<String, RequestBody> map = new HashMap<>();

        for (int i = 0; i < addAdModel.getOptionModelList().size(); i++) {
            FormDataModel.OptionModel optionModel = addAdModel.getOptionModelList().get(i);

            map.put("forms_array[" + i + "][definition_id]", Common.getRequestBodyText(String.valueOf(optionModel.getDefinition_id())));
            map.put("forms_array[" + i + "][type]", Common.getRequestBodyText(optionModel.getType()));
            map.put("forms_array[" + i + "][option_title]", Common.getRequestBodyText(optionModel.getOption_title()));

        }


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .addAdsWithVideo(main_category_part, cat_id_part, sub_cat_id_part, user_id_part, ad_name_part, ad_details_part, type_id_part, city_id_part, price_part, ad_address_part, lat_part, lng_part, video_part, getImagesParts(), map)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddAdsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(RESULT_OK, intent);
                                }
                                finish();
                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
        }

    }

    private void addAdWithoutVideo() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        RequestBody main_category_part = Common.getRequestBodyText(String.valueOf(addAdModel.getMain_cat_id()));
        RequestBody cat_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getCat_id()));
        RequestBody sub_cat_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getSub_cat_id()));
        RequestBody type_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getAd_type_id()));
        RequestBody city_id_part = Common.getRequestBodyText(String.valueOf(addAdModel.getCity_id()));
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody ad_name_part = Common.getRequestBodyText(addAdModel.getAd_name());
        RequestBody ad_details_part = Common.getRequestBodyText(addAdModel.getAd_details());
        RequestBody price_part = Common.getRequestBodyText(String.valueOf(addAdModel.getAd_price()));

        RequestBody ad_address_part = Common.getRequestBodyText(addAdModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(String.valueOf(addAdModel.getLat()));
        RequestBody lng_part = Common.getRequestBodyText(String.valueOf(addAdModel.getLng()));

        Map<String, RequestBody> map = new HashMap<>();

        for (int i = 0; i < addAdModel.getOptionModelList().size(); i++) {
            FormDataModel.OptionModel optionModel = addAdModel.getOptionModelList().get(i);

            map.put("forms_array[" + i + "][definition_id]", Common.getRequestBodyText(String.valueOf(optionModel.getDefinition_id())));
            map.put("forms_array[" + i + "][type]", Common.getRequestBodyText(optionModel.getType()));
            map.put("forms_array[" + i + "][option_title]", Common.getRequestBodyText(optionModel.getOption_title()));

        }


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .addAdsWithoutVideo(main_category_part, cat_id_part, sub_cat_id_part, user_id_part, ad_name_part, ad_details_part, type_id_part, city_id_part, price_part, ad_address_part, lat_part, lng_part, getImagesParts(), map)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                            if (response.isSuccessful() && response.body() != null) {

                                Toast.makeText(AddAdsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                if (intent != null) {
                                    setResult(RESULT_OK, intent);
                                }
                                finish();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("dddd", e.getMessage());
        }
    }

    private List<MultipartBody.Part> getImagesParts() {
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (AdImageVideoModel adImageVideoModel : adImageVideoModelList) {
            MultipartBody.Part part = Common.getMultiPart(this, Uri.parse(adImageVideoModel.getUri()), "ad_images[]");
            parts.add(part);
        }

        return parts;
    }

    private void CreateSelectImageDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogAddAddSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_add_select_image, null, false);


        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        binding.imageGallery.setOnClickListener(view -> {
            dialog.dismiss();
            openSheet();
        });
        binding.imageCamera.setOnClickListener(view -> {
            dialog.dismiss();
            checkCameraPermission();

        });


        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void openSheet() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void closeSheet() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void getSubCategory() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getAllSubCategory()
                    .enqueue(new Callback<SubCategoryDataModel>() {
                        @Override
                        public void onResponse(Call<SubCategoryDataModel> call, Response<SubCategoryDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                subCategoryModelList.clear();
                                subCategoryModelList.add(new SubCategoryDataModel.SubCategoryModel(0, getString(R.string.ch_cat)));
                                subCategoryModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(AddAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SubCategoryDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
        }
    }

    private void getFormData(int cat_id) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getForms(cat_id)
                    .enqueue(new Callback<FormDataModel>() {
                        @Override
                        public void onResponse(Call<FormDataModel> call, Response<FormDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updateSpinnersAdapters(response.body());
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(AddAdsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(AddAdsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FormDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddAdsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAdsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
        }
    }

    private void updateSpinnersAdapters(FormDataModel body) {

        formModelList.clear();
        formModelList.addAll(body.getForm());
        if (body.getCities().size() > 0) {


            if (isFirstTime) {
                cityModelListSearch.clear();
                cityModelListSearch.add(new FormDataModel.CityModel(0, getString(R.string.ch_city)));
                cityModelList.clear();
                cityModelList.add(new FormDataModel.CityModel(0, getString(R.string.ch_city)));
                cityModelList.addAll(body.getCities());
                cityModelListSearch.addAll(body.getCities());


                //binding.spinnerCity.setAdapter(cityAdapter);

            }

        } else {

            if (cityAdapter == null) {
                   /* cityModelList.clear();
                    cityModelList.add(new FormDataModel.CityModel(0,getString(R.string.ch_city)));
*/
                cityModelListSearch.clear();
                cityModelListSearch.add(new FormDataModel.CityModel(0, getString(R.string.ch_city)));


                    /*cityAdapter = new SpinnerCityAdapter(cityModelList,this);
                    binding.spinnerCity.setAdapter(cityAdapter);*/

            }
        }


        if (body.getTypes().size() > 0) {


            if (isFirstTime) {
                typeModelList.clear();
                typeModelList.add(new FormDataModel.TypeModel(0, getString(R.string.ch_type)));
                typeModelList.addAll(body.getTypes());

                typeAdapter = new SpinnerTypeAdapter(typeModelList, this);
                binding.spinnerType.setAdapter(typeAdapter);

            }

        } else {

            if (isFirstTime) {
                typeModelList.clear();
                typeModelList.add(new FormDataModel.TypeModel(0, getString(R.string.ch_type)));
                typeAdapter = new SpinnerTypeAdapter(typeModelList, this);
                binding.spinnerType.setAdapter(typeAdapter);

            }
        }


        subSubCategoryModelList.clear();
        subSubCategoryModelList.add(new FormDataModel.SubCategory(0, getString(R.string.ch)));
        subSubCategoryModelList.addAll(body.getSub_cat());

        subSubCategoryFormAdapter = new SpinnerSubSubCategoryFormAdapter(subSubCategoryModelList, this);
        binding.spinnerSubSubCategory.setAdapter(subSubCategoryFormAdapter);

        isFirstTime = false;

    }

    private void clearSpinnerData() {
        Log.e("ddd", "ttt");
        cityModelList.clear();
        cityModelList.add(new FormDataModel.CityModel(0, getString(R.string.ch_city)));


        subSubCategoryModelList.clear();
        subSubCategoryModelList.add(new FormDataModel.SubCategory(0, getString(R.string.ch)));

        typeModelList.clear();
        typeModelList.add(new FormDataModel.TypeModel(0, getString(R.string.ch_type)));


        if (cityAdapter != null) {
            cityAdapter.notifyDataSetChanged();
        } else {
                /*cityAdapter = new SpinnerCityAdapter(cityModelList,this);
                binding.spinnerCity.setAdapter(cityAdapter);*/

        }

        if (subSubCategoryFormAdapter != null) {
            subSubCategoryFormAdapter.notifyDataSetChanged();
        } else {
            subSubCategoryFormAdapter = new SpinnerSubSubCategoryFormAdapter(subSubCategoryModelList, this);
            binding.spinnerSubSubCategory.setAdapter(subSubCategoryFormAdapter);
        }


        if (typeAdapter != null) {
            typeAdapter.notifyDataSetChanged();
        } else {
            typeAdapter = new SpinnerTypeAdapter(typeModelList, this);
            binding.spinnerType.setAdapter(typeAdapter);
        }
    }

    private void initMap() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

            mMap.setOnMapClickListener(latLng -> {
                marker.setPosition(latLng);
                lat = latLng.latitude;
                lng = latLng.longitude;
                getGeoData(lat, lng);
            });

            fragment.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));

        }
    }

    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        addAdModel.setLat(lat);
        addAdModel.setLng(lng);
        binding.setAddAdModel(addAdModel);

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }


    }

    private void getGeoData(final double lat, double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                String address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");

                                binding.edtAddress.setText(address);
                                addAdModel.setAddress(address);
                                binding.setAddAdModel(addAdModel);

                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void Search(String query) {

        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {
                                String address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                lat = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                                lng = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                                binding.edtAddress.setText(address);
                                addAdModel.setAddress(address);
                                binding.setAddAdModel(addAdModel);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(AddAdsActivity.this, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat, lng);
        getGeoData(lat, lng);

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == read_req || requestCode == video_req) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage(requestCode);

            } else {
                Toast.makeText(this, "Permission to access image denied", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == camera_req) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                selectImage(camera_req);

            } else {
                Toast.makeText(this, "Permission to access image denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startLocationUpdate();

        } else if (requestCode == read_req && resultCode == Activity.RESULT_OK && data != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();
            if(data.getData()!=null){
            Uri uri = data.getData();
            String path = Common.getImagePath(this, uri);
            adImageVideoModelList.add(new AdImageVideoModel(false, uri.toString()));

            addAdModel.setImages(adImageVideoModelList);
            binding.setAddAdModel(addAdModel);
            setAdapterData();
            closeSheet();}
            else{

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();

                    }
                    Log.v("MainActivity", "Selected Images" + mArrayUri.size());
                }

            }
            }

        } else if (requestCode == camera_req && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            String path = Common.getImagePath(this, getUriFromBitmap(bitmap));

            adImageVideoModelList.add(new AdImageVideoModel(false, getUriFromBitmap(bitmap).toString()));
            addAdModel.setImages(adImageVideoModelList);
            binding.setAddAdModel(addAdModel);

            setAdapterData();
            closeSheet();


        } else if (requestCode == video_req && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();

            String path = Common.getImagePath(this, uri);

            Log.e("vid", getVideoPos() + "MM");
            if (getVideoPos() == -1) {
                adImageVideoModelList.add(new AdImageVideoModel(true, uri.toString()));

            } else {
                adImageVideoModelList.set(getVideoPos(), new AdImageVideoModel(true, uri.toString()));

            }


            addAdModel.setImages(adImageVideoModelList);
            binding.setAddAdModel(addAdModel);

            setAdapterData();
            closeSheet();


        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("data")) {
                List<FormDataModel.OptionModel> optionModelList = (List<FormDataModel.OptionModel>) data.getSerializableExtra("data");

                this.optionModelList.addAll(optionModelList);

                addAdModel.setOptionModelList(this.optionModelList);
                binding.setAddAdModel(addAdModel);

                binding.tvFilter.setText(R.string.all_filter_ched);
            }
        }

    }

    private void setAdapterData() {

        sliderAddAdAdapter = new SliderAddAdAdapter(this, adImageVideoModelList);
        binding.pager.setAdapter(sliderAddAdAdapter);

        binding.selectImageContainer.setVisibility(View.GONE);
        binding.tabContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(5, 0, 5, 0);
            tab.requestLayout();
        }
    }

    private void checkReadPermission(int req) {
        if (ContextCompat.checkSelfPermission(this, read_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_perm}, req);
        } else {
            selectImage(req);
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_perm) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, write_perm) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{camera_perm, write_perm}, camera_req);
        } else {
            selectImage(camera_req);
        }
    }

    private void selectImage(int req) {

        Intent intent = new Intent();

        if (req == read_req) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            intent.setType("image/*");


        } else if (req == video_req) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");


        } else if (req == camera_req) {
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        startActivityForResult(intent, req);


    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        String uri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
        return Uri.parse(uri);
    }

    @Override
    public void back() {
        finish();
    }


    @Override
    public void onBackPressed() {

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            closeSheet();
        } else {
            super.onBackPressed();

        }


    }

    public void setVideoUri(Uri uri) {

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("uri", uri.toString());
        startActivity(intent);

    }

    private int getVideoPos() {
        int pos = -1;

        for (int i = 0; i < adImageVideoModelList.size(); i++) {
            AdImageVideoModel adImageVideoModel = adImageVideoModelList.get(i);

            if (adImageVideoModel.isVideo()) {
                pos = i;
                return pos;
            }
        }

        return pos;
    }


}
