package com.creative.share.apps.heragelkashed.activities_fragments.activity_chat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.adapter.ChatAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivityChatBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.ChatUserModel;
import com.creative.share.apps.heragelkashed.models.MessageDataModel;
import com.creative.share.apps.heragelkashed.models.MessageModel;
import com.creative.share.apps.heragelkashed.models.UserModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.remote.Api;
import com.creative.share.apps.heragelkashed.share.Common;
import com.creative.share.apps.heragelkashed.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private LinearLayoutManager manager;
    private List<MessageModel> messageModelList;
    private ChatAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private ChatUserModel chatUserModel;
    private boolean isFromHome = true;
    private boolean canSendTyping = true;
    private int current_page = 1;
    private boolean isLoading = false;
    private MediaPlayer mp;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1;
    private boolean hasNewMsg = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("from_fire")) {
                isFromHome = false;
            }

            chatUserModel = (ChatUserModel) intent.getSerializableExtra("chat_user_data");


        }
    }
    private void initView()
    {
        EventBus.getDefault().register(this);

        messageModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setChatUser(chatUserModel);
        preferences.create_update_ChatUserData(this,chatUserModel);
        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        binding.recView.setLayoutManager(manager);
        adapter = new ChatAdapter(messageModelList, userModel.getId(), chatUserModel.getImage(), this);
        binding.recView.setAdapter(adapter);
        binding.recView.setHasFixedSize(true);
        binding.recView.setItemViewCacheSize(20);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = adapter.getItemCount();

                    if (lastItemPos == (total_items - 2) && !isLoading) {
                        isLoading = true;
                        messageModelList.add(0, null);
                        adapter.notifyItemInserted(0);
                        int next_page = current_page + 1;
                        loadMore(next_page);


                    }
                }
            }
        });

        removeNotificationFromBackGround();
        getChatMessages();

        binding.imageCall.setOnClickListener((v)->{
            String phone = chatUserModel.getPhone_code()+chatUserModel.getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
            startActivity(intent);

        });

        binding.edtMsgContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (canSendTyping)
                {
                    canSendTyping = false;
                    sendTyping();
                }
            }
        });

        binding.imageSelectPic.setOnClickListener((v) -> checkPermission());
        binding.imageSend.setOnClickListener((v) -> {
            String msg = binding.edtMsgContent.getText().toString().trim();
            if (!msg.isEmpty()) {
                canSendTyping = true;
                binding.edtMsgContent.setText("");
                sendMessage(msg);
            }

        });


        Log.e("user_id",userModel.getId()+"__"+"chat"+chatUserModel.getId()+"___");

    }

    private void removeNotificationFromBackGround() {

        new Handler()
                .postDelayed(() -> {
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (manager!=null)
                    {
                        manager.cancel(12352);
                    }
                },100);
    }

    private void sendMessage(String message)
    {
        try {

            long date = Calendar.getInstance().getTimeInMillis()/1000;

            Api.getService(Tags.base_url)
                    .sendChatMessage(chatUserModel.getRoom_id(), userModel.getId(), chatUserModel.getId(), 1, message, date)
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                hasNewMsg =  true;
                                Log.e("ddd",response.body().getMessage_type()+"__");
                                messageModelList.add(response.body());
                                adapter.notifyDataSetChanged();
                                scrollToLastPosition();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }
    private void uploadImage(Uri uri)
    {


        try {

            long date = Calendar.getInstance().getTimeInMillis()/1000;
            RequestBody room_id_part = Common.getRequestBodyText(String.valueOf(chatUserModel.getRoom_id()));
            RequestBody chat_user_id_part = Common.getRequestBodyText(String.valueOf(chatUserModel.getId()));
            RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
            RequestBody msg_type_part = Common.getRequestBodyText("2");
            RequestBody msg_part = Common.getRequestBodyText("");
            RequestBody date_part = Common.getRequestBodyText(String.valueOf(date));
            MultipartBody.Part image_part = Common.getMultiPart(this,uri,"file_link");


            Api.getService(Tags.base_url)
                    .sendChatImage(room_id_part,user_id_part,chat_user_id_part,msg_type_part,msg_part,date_part,image_part)
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                messageModelList.add(response.body());
                                adapter.notifyDataSetChanged();
                                scrollToLastPosition();

                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }

    }

    private void sendTyping() {

    }
    private void getChatMessages() {
        try {


            Api.getService(Tags.base_url)
                    .getRoomMessages(userModel.getId(), chatUserModel.getRoom_id(), 1)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                chatUserModel = new ChatUserModel(response.body().getRoom().getOther_user_name(), response.body().getRoom().getOther_user_avatar(), response.body().getRoom().getSecond_user_id(), response.body().getRoom().getId(),response.body().getRoom().getOther_user_phone_code(),response.body().getRoom().getOther_user_phone());
                                preferences.create_update_ChatUserData(ChatActivity.this,chatUserModel);

                                messageModelList.clear();
                                messageModelList.addAll(response.body().getMessages().getData());
                                adapter.notifyDataSetChanged();
                                scrollToLastPosition();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void loadMore(int next_page) {
        try {

            Api.getService(Tags.base_url)
                    .getRoomMessages(userModel.getId(), chatUserModel.getRoom_id(), next_page)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            isLoading = false;
                            messageModelList.remove(0);
                            adapter.notifyItemRemoved(0);
                            if (response.isSuccessful() && response.body() != null) {

                                current_page = response.body().getMessages().getCurrent_page();
                                messageModelList.addAll(0, response.body().getMessages().getData());
                                adapter.notifyItemRangeInserted(0, response.body().getMessages().getData().size());


                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                isLoading = false;

                                if (messageModelList.get(0) == null) {
                                    messageModelList.remove(0);
                                    adapter.notifyItemRemoved(0);
                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void scrollToLastPosition()
    {

        new Handler()
                .postDelayed(() -> binding.recView.scrollToPosition(messageModelList.size()-1),10);
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, read_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_perm}, read_req);
        } else {
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == read_req && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uploadImage(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == read_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Access pictures denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage()
    {
        Intent intent = new Intent();

        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_GET_CONTENT);


        }

        startActivityForResult(intent, read_req);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel messageModel)
    {
        messageModelList.add(messageModel);
        scrollToLastPosition();
    }


    @Override
    public void back() {

        Back();
    }

    private void Back() {
        if (isFromHome) {
            if (hasNewMsg)
            {
                Intent intent = getIntent();
                if (intent!=null&&hasNewMsg)
                {

                    intent.putExtra("new_msg",true);
                    setResult(RESULT_OK,intent);
                }
            }
            finish();


        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearChatUserData(this);
    }
}
