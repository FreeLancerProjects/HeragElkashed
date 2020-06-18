package com.creative.share.apps.heragelkashed.notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_chat.ChatActivity;
import com.creative.share.apps.heragelkashed.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.heragelkashed.models.ChatUserModel;
import com.creative.share.apps.heragelkashed.models.MessageModel;
import com.creative.share.apps.heragelkashed.models.NotificationActionModel;
import com.creative.share.apps.heragelkashed.preferences.Preferences;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.newInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> map = remoteMessage.getData();

        for (String key:map.keySet())
        {
            Log.e("key",key+"    value "+map.get(key));
        }

        if (getSession().equals(Tags.session_login))
        {
            if (map.get("to_user_id")!=null)
            {
                int to_id = Integer.parseInt(map.get("to_user_id"));

                Log.e("dddd",getCurrentUser_id()+"__");
                if (getCurrentUser_id()==to_id)
                {
                    manageNotification(map);
                }
            }else
                {
                    int to_id = Integer.parseInt(map.get("adv_owner"));

                    Log.e("dddd",getCurrentUser_id()+"__");
                    if (getCurrentUser_id()==to_id)
                    {
                        manageNotification(map);
                    }
                }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNewNotificationVersion(map);
        }else
            {
                createOldNotificationVersion(map);

            }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type!=null&&not_type.equals("chat"))
        {
            String file_link="";
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            int msg_id = Integer.parseInt(map.get("id"));
            int room_id = Integer.parseInt(map.get("room_id"));
            int from_user_id = Integer.parseInt(map.get("from_user_id"));
            int to_user_id = Integer.parseInt(map.get("to_user_id"));
            int type = Integer.parseInt(map.get("type"));

            if (type==2)
            {
                file_link = map.get("file_link");
            }

            int date = Integer.parseInt(map.get("date"));
            int isRead = Integer.parseInt(map.get("is_read"));

            String message = map.get("message");
            String from_user_name = map.get("from_user_name");
            String from_user_email = map.get("from_user_email");
            String from_user_phone = map.get("from_user_phone");
            String from_user_phone_code = map.get("from_user_phone_code");
            String from_user_avatar = map.get("from_user_avatar");

            String to_user_name = map.get("to_user_name");
            String to_user_email = map.get("to_user_email");
            String to_user_phone = map.get("to_user_phone");
            String to_user_avatar = map.get("to_user_avatar");
            String to_user_phone_code = map.get("to_user_phone_code");

            String file = map.get(file_link);



            MessageModel messageModel = new MessageModel(msg_id,room_id,from_user_id,to_user_id,type,message,file,date,isRead,from_user_name,from_user_email,from_user_phone_code,from_user_phone,from_user_avatar,to_user_name,to_user_email,to_user_phone_code,to_user_phone,to_user_avatar);




            if (current_class.equals("com.creative.share.apps.heragelawal.activities_fragments.activity_chat.ChatActivity"))
            {

                int chat_user_id = getChatUser_id();

                if (chat_user_id==from_user_id)
                {
                    EventBus.getDefault().post(messageModel);
                }else
                    {
                        LoadChatImage(messageModel,sound_Path,1);
                    }


            }else
                {
                    EventBus.getDefault().post(messageModel);

                    LoadChatImage(messageModel,sound_Path,1);

                }

        }else
        {

            int to_id = Integer.parseInt(map.get("adv_owner"));
            String title = map.get("title");
            String content = map.get("content");

            NotificationActionModel notificationActionModel = new NotificationActionModel(to_id,title,content);
            sendNotification_VersionNew(notificationActionModel,sound_Path);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map)
    {


        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type!=null&&not_type.equals("chat"))
        {
            String file_link="";
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String current_class =activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            int msg_id = Integer.parseInt(map.get("id"));
            int room_id = Integer.parseInt(map.get("room_id"));
            int from_user_id = Integer.parseInt(map.get("from_user_id"));
            int to_user_id = Integer.parseInt(map.get("to_user_id"));
            int type = Integer.parseInt(map.get("type"));

            if (type==2)
            {
                file_link = map.get("file_link");
            }

            int date = Integer.parseInt(map.get("date"));
            int isRead = Integer.parseInt(map.get("is_read"));

            String message = map.get("message");
            String from_user_name = map.get("from_user_name");
            String from_user_email = map.get("from_user_email");
            String from_user_phone = map.get("from_user_phone");
            String from_user_phone_code = map.get("from_user_phone_code");
            String from_user_avatar = map.get("from_user_avatar");

            String to_user_name = map.get("to_user_name");
            String to_user_email = map.get("to_user_email");
            String to_user_phone = map.get("to_user_phone");
            String to_user_avatar = map.get("to_user_avatar");
            String to_user_phone_code = map.get("to_user_phone_code");

            String file = map.get(file_link);



            MessageModel messageModel = new MessageModel(msg_id,room_id,from_user_id,to_user_id,type,message,file,date,isRead,from_user_name,from_user_email,from_user_phone_code,from_user_phone,from_user_avatar,to_user_name,to_user_email,to_user_phone_code,to_user_phone,to_user_avatar);

            if (current_class.equals("com.creative.share.apps.heragelawal.activities_fragments.activity_chat.ChatActivity"))
            {

                int chat_user_id = getChatUser_id();

                if (chat_user_id==from_user_id)
                {
                    EventBus.getDefault().post(messageModel);
                }else
                {
                    LoadChatImage(messageModel,sound_Path,1);
                }


            }else
            {
                EventBus.getDefault().post(messageModel);

                LoadChatImage(messageModel,sound_Path,1);

            }

        }else
        {

            int to_id = Integer.parseInt(map.get("adv_owner"));
            String title = map.get("title");
            String content = map.get("content");

            NotificationActionModel notificationActionModel = new NotificationActionModel(to_id,title,content);
            sendNotification_VersionOld(notificationActionModel,sound_Path);
        }

    }


    private void LoadChatImage(MessageModel messageModel, String sound_path,int type) {

        Log.e("load_image","dddddddddddddddddddddd");

        Target target = new Target() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Log.e("1","1");
                if (type==1)
                {
                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);

                }else
                {
                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);

                }
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

                Log.e("2","2");

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo2);

                if (type==1)
                {
                    sendChatNotification_VersionNew(messageModel,sound_path,bitmap);

                }else
                {
                    sendChatNotification_VersionOld(messageModel,sound_path,bitmap);

                }

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        new Handler(Looper.getMainLooper()).postDelayed(() -> Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_AVATAR+messageModel.getFrom_user_avatar())).into(target),100);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendChatNotification_VersionNew(MessageModel messageModel, String sound_path,Bitmap bitmap) {


        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(messageModel.getFrom_user_name());
        builder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(messageModel.getFrom_user_name(),messageModel.getFrom_user_avatar(),messageModel.getFrom_user_id(),messageModel.getRoom_id(),messageModel.getFrom_user_phone_code(),messageModel.getFrom_user_phone());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        if (messageModel.getMessage_type()==1)
        {
            builder.setContentText(messageModel.getMessage());

        }else
        {
            builder.setContentText(getString(R.string.img_sent));

        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(12352, builder.build());
        }


    }

    private void sendChatNotification_VersionOld(MessageModel messageModel, String sound_path, Bitmap bitmap) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(messageModel.getFrom_user_name());

        Intent intent = new Intent(this, ChatActivity.class);
        ChatUserModel chatUserModel = new ChatUserModel(messageModel.getFrom_user_name(),messageModel.getFrom_user_avatar(),messageModel.getFrom_user_id(),messageModel.getRoom_id(),messageModel.getFrom_user_phone_code(),messageModel.getFrom_user_phone());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("chat_user_data", chatUserModel);
        intent.putExtra("from_fire", true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        if (messageModel.getMessage_type()==1)
        {
            builder.setContentText(messageModel.getMessage());

        }else
        {
            builder.setContentText(getString(R.string.img_sent));

        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(12352, builder.build());
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification_VersionNew(NotificationActionModel notificationActionModel, String sound_path) {


        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(notificationActionModel.getTitle());

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        builder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("not",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        builder.setContentText(notificationActionModel.getContent());


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(12352, builder.build());
        }


    }

    private void sendNotification_VersionOld(NotificationActionModel notificationActionModel, String sound_path) {


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(notificationActionModel.getTitle());

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("not",true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        builder.setContentText(notificationActionModel.getContent());


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(12352, builder.build());
        }


    }


    private int getCurrentUser_id()
    {
        return preferences.getUserData(this).getId();
    }

    private int getChatUser_id()
    {
        if (preferences.getChatUserData(this)!=null)
        {
            return preferences.getChatUserData(this).getId();

        }else
            {
                return -1;

            }
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }


}
