package com.creative.share.apps.heragelkashed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.SpinnerDataRowBinding;
import com.creative.share.apps.heragelkashed.models.FormDataModel;

import java.util.List;

public class SpinnerDataChildAdapter extends BaseAdapter {

    private List<FormDataModel.OptionModel> list;
    private Context context;

    public SpinnerDataChildAdapter(List<FormDataModel.OptionModel> list, Context context) {
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
        @SuppressLint("ViewHolder") SpinnerDataRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_data_row,viewGroup,false);
        FormDataModel.OptionModel model = list.get(i);
        binding.setOptionModel(model);
        return binding.getRoot();
    }
}
