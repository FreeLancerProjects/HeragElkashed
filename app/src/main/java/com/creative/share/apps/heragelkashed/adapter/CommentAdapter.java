package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.CommentRowBinding;
import com.creative.share.apps.heragelkashed.models.AdModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {

    private Context context;
    private List<AdModel.CommentModel> commentModelList;


    public CommentAdapter(Context context, List<AdModel.CommentModel> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CommentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.comment_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AdModel.CommentModel commentModel = commentModelList.get(position);
        holder.binding.setCommentModel(commentModel);


    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CommentRowBinding binding;

        public MyHolder(@NonNull CommentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
