package com.creative.share.apps.heragelkashed.activities_fragments.activity_video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.databinding.ActivityVideoBinding;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;

import java.util.Locale;

import io.paperdb.Paper;

public class VideoActivity extends AppCompatActivity {
    private ActivityVideoBinding binding;
    private String uri;
    private MediaController mediaController;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        getDataFromIntent();
        initView();
    }

    private void initView() {
        mediaController = new MediaController(this);
        binding.videoView.setVideoURI(Uri.parse(uri));
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.start();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("uri"))
        {
            uri = intent.getStringExtra("uri");
        }
    }
}
