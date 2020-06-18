package com.creative.share.apps.heragelkashed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.ChatImageLeftRowBinding;
import com.creative.share.apps.heragelkashed.databinding.ChatImageRightRowBinding;
import com.creative.share.apps.heragelkashed.databinding.ChatMessageLeftRowBinding;
import com.creative.share.apps.heragelkashed.databinding.ChatMessageRightRowBinding;
import com.creative.share.apps.heragelkashed.databinding.LoadMoreRowBinding;
import com.creative.share.apps.heragelkashed.databinding.TypingRowBinding;
import com.creative.share.apps.heragelkashed.models.MessageModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_IMAGE_LEFT = 3;
    private final int ITEM_IMAGE_RIGHT = 4;
    private final int ITEM_LOADMORE = 5;



    private List<MessageModel> messageModelList;
    private int current_user_id;
    private String chat_user_image;
    private Context context;

    public ChatAdapter(List<MessageModel> messageModelList, int current_user_id, String chat_user_image, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.chat_user_image = chat_user_image;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_LEFT) {

            ChatMessageLeftRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_message_left_row, parent, false);
            return new MsgLeftHolder(binding);

        } else if (viewType == ITEM_MESSAGE_RIGHT) {

            ChatMessageRightRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_message_right_row, parent, false);
            return new MsgRightHolder(binding);

        } else if (viewType == ITEM_IMAGE_LEFT) {

            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_image_left_row, parent, false);
            return new ImageLeftHolder(binding);

        } else if (viewType == ITEM_IMAGE_RIGHT) {

            ChatImageRightRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_image_right_row, parent, false);
            return new ImageRightHolder(binding);

        }else if (viewType == ITEM_LOADMORE) {

            LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);

        } else {
            TypingRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.typing_row, parent, false);
            return new TypingHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MsgLeftHolder) {

            MsgLeftHolder msgLeftHolder = (MsgLeftHolder) holder;
            MessageModel messageModel = messageModelList.get(msgLeftHolder.getAdapterPosition());
            msgLeftHolder.binding.setMessageModel(messageModel);
            msgLeftHolder.binding.setEndPoint(chat_user_image);

        } else if (holder instanceof MsgRightHolder) {
            MsgRightHolder msgRightHolder = (MsgRightHolder) holder;
            MessageModel messageModel = messageModelList.get(msgRightHolder.getAdapterPosition());

            msgRightHolder.binding.setMessageModel(messageModel);


        } else if (holder instanceof ImageLeftHolder) {
            ImageLeftHolder imageLeftHolder = (ImageLeftHolder) holder;
            MessageModel messageModel = messageModelList.get(imageLeftHolder.getAdapterPosition());
            imageLeftHolder.binding.setMessageModel(messageModel);
            imageLeftHolder.binding.setEndPoint(chat_user_image);


        } else if (holder instanceof ImageRightHolder) {
            ImageRightHolder imageRightHolder = (ImageRightHolder) holder;
            MessageModel messageModel = messageModelList.get(imageRightHolder.getAdapterPosition());
            imageRightHolder.binding.setMessageModel(messageModel);


        } else if (holder instanceof TypingHolder) {
            TypingHolder typingHolder = (TypingHolder) holder;


        }else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder typingHolder = (LoadMoreHolder) holder;


        }


    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MsgLeftHolder extends RecyclerView.ViewHolder {

        private ChatMessageLeftRowBinding binding;

        public MsgLeftHolder(ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public class MsgRightHolder extends RecyclerView.ViewHolder {
        private ChatMessageRightRowBinding binding;

        public MsgRightHolder(ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class ImageLeftHolder extends RecyclerView.ViewHolder {
        private ChatImageLeftRowBinding binding;

        public ImageLeftHolder(ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    public class ImageRightHolder extends RecyclerView.ViewHolder {
        private ChatImageRightRowBinding binding;

        public ImageRightHolder(ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public class TypingHolder extends RecyclerView.ViewHolder {
        private TypingRowBinding binding;

        public TypingHolder(TypingRowBinding binding) {
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
        MessageModel messageModel = messageModelList.get(position);

       if (messageModel == null) {

            return ITEM_LOADMORE;

        } else {


            if (messageModel.getTo_user_id() == current_user_id) {
                if (messageModel.getMessage_type() == 1) {
                    return ITEM_MESSAGE_LEFT;
                } else {
                    return ITEM_IMAGE_LEFT;
                }
            } else {


                if (messageModel.getMessage_type() == 1) {
                    return ITEM_MESSAGE_RIGHT;
                } else {
                    return ITEM_IMAGE_RIGHT;
                }
            }
        }


    }


}
