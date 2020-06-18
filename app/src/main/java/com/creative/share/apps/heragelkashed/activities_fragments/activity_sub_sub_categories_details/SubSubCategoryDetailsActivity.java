package com.creative.share.apps.heragelkashed.activities_fragments.activity_sub_sub_categories_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.heragelkashed.adapter.FragmentPagerAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivitySubSubCategoryDetailsBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.AdTypeDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubSubCategoryDetailsActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivitySubSubCategoryDetailsBinding binding;
    private String lang;
    private List<AdTypeDataModel.AdTypeModel> adTypeModelList = new ArrayList<>();
    private int sub_sub_id;
    private int ad_type_id;
    private int position;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titles;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_sub_category_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("ad_type_list")&&intent.hasExtra("sub_sub_id")&&intent.hasExtra("ad_type_id"))
        {
            adTypeModelList = (List<AdTypeDataModel.AdTypeModel>) intent.getSerializableExtra("ad_type_list");
            sub_sub_id = intent.getIntExtra("sub_sub_id",0);
            ad_type_id =  intent.getIntExtra("ad_type_id",0);
            position =  intent.getIntExtra("position",0);

        }


    }

    private void initView() {
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.tabLayout.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(adTypeModelList.size());
        for (int i =0;i<adTypeModelList.size();i++)
        {
            fragmentList.add(FragmentSubSubCategoryDetails.newInstance(sub_sub_id,adTypeModelList.get(i).getId()));
            titles.add(adTypeModelList.get(i).getTitle());

        }

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(fragmentList);
        pagerAdapter.AddTitle(titles);
        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setCurrentItem(position);

        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentSubSubCategoryDetails fragment = (FragmentSubSubCategoryDetails) pagerAdapter.getItem(position);
                if (fragment.getStyle()==1)
                {
                    binding.btnStyle.setText(getString(R.string.square));

                }else
                {
                    binding.btnStyle.setText(getString(R.string.list));


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.btnStyle.setOnClickListener(view -> {
            FragmentSubSubCategoryDetails fragment = (FragmentSubSubCategoryDetails) pagerAdapter.getItem(binding.pager.getCurrentItem());
            fragment.updateManager();
            if (fragment.getStyle()==1)
            {
                binding.btnStyle.setText(getString(R.string.square));

            }else
            {
                binding.btnStyle.setText(getString(R.string.list));


            }
        });

        binding.llMap.setOnClickListener(view -> {
            FragmentSubSubCategoryDetails fragmentSubSubCategoryDetails = (FragmentSubSubCategoryDetails) pagerAdapter.getItem(binding.pager.getCurrentItem());
            if (fragmentSubSubCategoryDetails.getAdModelList().size()>0)
            {
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("adData", (Serializable) fragmentSubSubCategoryDetails.getAdModelList());
                intent.putExtra("ad_type",adTypeModelList.get(binding.pager.getCurrentItem()).getTitle());
                startActivity(intent);
            }else
                {
                    Toast.makeText(SubSubCategoryDetailsActivity.this,getString(R.string.no_adversiment_found), Toast.LENGTH_SHORT).show();
                }
        });
    }

    @Override
    public void back() {
        finish();
    }
}
