package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.fragment_company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.databinding.FragmentAdversimentBinding;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Adversiment extends Fragment {
    private HomeActivity activity;
    private FragmentAdversimentBinding binding;
   private Preferences preferences;
   private UserModel userModel;
    private String lang;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_adversiment, container, false);


        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.setLang(lang);




        binding.recType.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false));
        binding.recType.setVisibility(View.VISIBLE);
        binding.recStores.setLayoutManager(new GridLayoutManager(activity,1));
        binding.recStores.setVisibility(View.VISIBLE);



    }



    public static Fragment_Adversiment newInstance() {
        return new Fragment_Adversiment();
    }


}
