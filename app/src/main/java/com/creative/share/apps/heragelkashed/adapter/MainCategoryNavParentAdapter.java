package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.NavParentRowBinding;
import com.creative.share.apps.heragelkashed.models.MainCategoryDataModel;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MainCategoryNavParentAdapter extends RecyclerView.Adapter<MainCategoryNavParentAdapter.MyHolder> {

    private Context context;
    private List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList;
    private String lang;
    private int index=0;
    private int [] colors = {R.color.colorPrimary,R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5,R.color.color6,R.color.color7,R.color.color8,R.color.color9};

    public MainCategoryNavParentAdapter(Context context, List<MainCategoryDataModel.MainCategoryModel> mainCategoryModelList) {
        this.context = context;
        this.mainCategoryModelList = mainCategoryModelList;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NavParentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.nav_parent_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        MainCategoryDataModel.MainCategoryModel mainCategoryModel = mainCategoryModelList.get(position);
        holder.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.setMainCategoryModel(mainCategoryModel);
        SubCategoryNavChildAdapter adapter = new SubCategoryNavChildAdapter(context,mainCategoryModel.getSub_categories());
        holder.binding.recView.setAdapter(adapter);



        if (lang.equals("ar"))
        {
            holder.binding.arrow.setRotation(180.0f);
        }
        if (mainCategoryModel.getSub_categories().size()>0)
        {
            holder.binding.tvNoAds.setVisibility(View.GONE);
        }else
            {
                holder.binding.tvNoAds.setVisibility(View.VISIBLE);

            }

        holder.binding.cons.setOnClickListener(view -> {

            if (holder.binding.expandLayout.isExpanded())
            {
                if (lang.equals("ar"))
                {
                    holder.binding.arrow.animate().rotation(180).setDuration(400).setInterpolator(new LinearInterpolator()).start();

                }else
                    {
                        holder.binding.arrow.animate().rotation(0).setDuration(400).setInterpolator(new LinearInterpolator()).start();

                    }

                holder.binding.expandLayout.collapse(true);
            }else
                {
                    holder.binding.arrow.animate().rotation(90).setDuration(400).setInterpolator(new LinearInterpolator()).start();

                    holder.binding.expandLayout.expand(true);

                }
        });

        holder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.SRC_IN);


        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL_ADS_CATEGORY_ICON+mainCategoryModel.getIcon())).fit().into(holder.binding.icon, new Callback() {
            @Override
            public void onSuccess() {
                if (index<colors.length-1)
                {
                    holder.binding.icon.setCircleBackgroundColorResource(colors[index]);
                    index++;

                }else
                {

                    index =0;
                    holder.binding.icon.setCircleBackgroundColorResource(colors[0]);

                }
                holder.binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mainCategoryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private NavParentRowBinding binding;

        public MyHolder(@NonNull NavParentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
