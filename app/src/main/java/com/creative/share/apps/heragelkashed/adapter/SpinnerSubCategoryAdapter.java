package com.creative.share.apps.heragelkashed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.SpinnerDeptRowBinding;
import com.creative.share.apps.heragelkashed.models.SubCategoryDataModel;

import java.util.List;

public class SpinnerSubCategoryAdapter extends BaseAdapter {

    private List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList;
    private Context context;

    public SpinnerSubCategoryAdapter(List<SubCategoryDataModel.SubCategoryModel> subCategoryModelList, Context context) {
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
        @SuppressLint("ViewHolder") SpinnerDeptRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_dept_row,viewGroup,false);
        SubCategoryDataModel.SubCategoryModel subCategoryModel = subCategoryModelList.get(i);
        binding.setSubCategoryModel(subCategoryModel);
        return binding.getRoot();
    }
}
