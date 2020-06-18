package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments.Fragment_Chat;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.databinding.RoomRowBinding;
import com.creative.share.apps.heragelkashed.models.UserRoomModelData;
import com.creative.share.apps.heragelkashed.share.Time_Ago;

import java.util.List;

public class Rooms_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Context context;
    private Fragment_Chat fragment;

    public Rooms_Adapter(Context context, List<UserRoomModelData.UserRoomModel> userRoomModelList, Fragment_Chat fragment) {

        this.userRoomModelList = userRoomModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            RoomRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.room_row,parent,false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more_row,parent,false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
            final MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setRoomModel(userRoomModel);
            myHolder.binding.tvDate.setText(Time_Ago.getTimeAgo(userRoomModel.getLast_message_date(),context));


            myHolder.itemView.setOnClickListener(v -> {
                UserRoomModelData.UserRoomModel userRoomModel1 = userRoomModelList.get(myHolder.getAdapterPosition());
                fragment.setItemData(userRoomModel1,myHolder.getAdapterPosition());
            });
        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return userRoomModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RoomRowBinding binding;
        public MyHolder(RoomRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreRowBinding binding;
        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
        if (userRoomModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
