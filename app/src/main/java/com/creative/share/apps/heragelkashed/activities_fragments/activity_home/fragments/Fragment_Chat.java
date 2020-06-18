package com.creative.share.apps.heragelkashed.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_chat.ChatActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.adapter.Rooms_Adapter;
import com.creative.share.apps.heragelkashed.databinding.FragmentChatBinding;
import com.creative.share.apps.heragelkashed.models.ChatUserModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.models.UserRoomModelData;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Chat extends Fragment {
    private HomeActivity activity;
    private FragmentChatBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private Rooms_Adapter adapter;
    private LinearLayoutManager manager;
    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private boolean isLoading = false;
    private int current_page = 1;

    public static Fragment_Chat newInstance() {
        return new Fragment_Chat();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);


        initView();
        return binding.getRoot();
    }

    private void initView() {
        userRoomModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        adapter = new Rooms_Adapter(activity,userRoomModelList,this);
        binding.recView.setAdapter(adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_items = adapter.getItemCount();
                    int lastItemPos = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    if (total_items>=6&&(lastItemPos==total_items-2)&&!isLoading)
                    {
                        isLoading = true;
                        userRoomModelList.add(null);
                        adapter.notifyItemInserted(userRoomModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);
                    }

                }
            }
        });
        getRooms();

    }

    public void getRooms()
    {
        userModel = preferences.getUserData(activity);

        try {
            Api.getService(Tags.base_url)
                    .getRooms(userModel.getId(),1)
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                userRoomModelList.clear();
                                userRoomModelList.addAll(response.body().getData());
                                if (userRoomModelList.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoConversation.setVisibility(View.GONE);
                                }else
                                {
                                    binding.tvNoConversation.setVisibility(View.VISIBLE);

                                }
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){

                            }
                        }
                    });
        }catch (Exception e){
            Log.e("eeee",e.getMessage()+"__");
            binding.progBar.setVisibility(View.GONE);
        }
    }



    private void loadMore(int page)
    {
        try {

            Api.getService(Tags.base_url)
                    .getRooms(userModel.getId(),page)
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            userRoomModelList.remove(userRoomModelList.size()-1);
                            adapter.notifyItemRemoved(userRoomModelList.size()-1);
                            isLoading = false;

                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                            {
                                userRoomModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getCurrent_page();
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                if (userRoomModelList.get(userRoomModelList.size()-1)==null)
                                {
                                    userRoomModelList.remove(userRoomModelList.size()-1);
                                    adapter.notifyItemRemoved(userRoomModelList.size()-1);
                                    isLoading = false;
                                }


                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
        }
    }

    public void setItemData(UserRoomModelData.UserRoomModel userRoomModel, int adapterPosition) {

        userRoomModel.setMy_message_unread_count(0);
        userRoomModelList.set(adapterPosition,userRoomModel);
        adapter.notifyItemChanged(adapterPosition);

        ChatUserModel chatUserModel = new ChatUserModel(userRoomModel.getOther_user_name(),userRoomModel.getOther_user_avatar(),userRoomModel.getSecond_user_id(),userRoomModel.getId(),userRoomModel.getOther_user_phone_code(),userRoomModel.getOther_user_phone());
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chat_user_data",chatUserModel);
        startActivityForResult(intent,1000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("new_msg"))
            {
                getRooms();
            }
        }
    }
}
