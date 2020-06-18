package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_slider_details.SliderDetailsActivity;
import com.creative.share.apps.heragelkashed.databinding.ReportReasonRowBinding;
import com.creative.share.apps.heragelkashed.models.ReportReasonDataModel;

import java.util.List;

public class ReportReasonAdapter extends RecyclerView.Adapter<ReportReasonAdapter.MyHolder> {

    private Context context;
    private List<ReportReasonDataModel.ReportReasonModel> reportReasonModelList;
    private SliderDetailsActivity activity;
    private int selected_pos = -1;

    public ReportReasonAdapter(Context context, List<ReportReasonDataModel.ReportReasonModel> reportReasonModelList) {
        this.context = context;
        this.reportReasonModelList = reportReasonModelList;
        this.activity = (SliderDetailsActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ReportReasonRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.report_reason_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ReportReasonDataModel.ReportReasonModel reasonModel = reportReasonModelList.get(position);
        holder.binding.setReasonModel(reasonModel);


        if (selected_pos==position)
        {
            holder.binding.rb.setChecked(true);
        }else
        {
            holder.binding.rb.setChecked(false);

        }
        holder.itemView.setOnClickListener(view -> {
            selected_pos = holder.getAdapterPosition();
            ReportReasonDataModel.ReportReasonModel reasonModel1 = reportReasonModelList.get(holder.getAdapterPosition());
            activity.setReasonItem(reasonModel1);
            notifyDataSetChanged();


        });




    }

    @Override
    public int getItemCount() {
        return reportReasonModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ReportReasonRowBinding binding;

        public MyHolder(@NonNull ReportReasonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
