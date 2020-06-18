package com.creative.share.apps.heragelkashed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.SpinnerCityRowBinding;
import com.creative.share.apps.heragelkashed.models.FormDataModel;

import java.util.List;

public class SpinnerCityAdapter extends BaseAdapter {

    private List<FormDataModel.CityModel> list;
    private Context context;

    public SpinnerCityAdapter(List<FormDataModel.CityModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerCityRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_city_row,viewGroup,false);
        FormDataModel.CityModel model = list.get(i);
        binding.setCityModel(model);
        return binding.getRoot();
    }
}
