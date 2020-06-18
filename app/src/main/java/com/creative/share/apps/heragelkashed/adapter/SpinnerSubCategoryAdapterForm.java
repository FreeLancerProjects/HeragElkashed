package com.creative.share.apps.heragelkashed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.SpinnerSubCategotyRowBinding;
import com.creative.share.apps.heragelkashed.models.FormDataModel;

import java.util.List;

public class SpinnerSubCategoryAdapterForm extends BaseAdapter {

    private List<FormDataModel.SubCategory> subCategoryModelList;
    private Context context;

    public SpinnerSubCategoryAdapterForm(List<FormDataModel.SubCategory> subCategoryModelList, Context context) {
        this.subCategoryModelList = subCategoryModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return subCategoryModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return subCategoryModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerSubCategotyRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_sub_categoty_row,viewGroup,false);
        FormDataModel.SubCategory subCategoryModel = subCategoryModelList.get(i);
        binding.setSubCategory(subCategoryModel);
        return binding.getRoot();
    }
}
