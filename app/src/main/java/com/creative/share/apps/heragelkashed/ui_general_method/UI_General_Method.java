package com.creative.share.apps.heragelkashed.ui_general_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UI_General_Method {

    @BindingAdapter("error")
    public static void setErrorUi(View view, String error)
    {
        if (view instanceof EditText)
        {
            EditText editText = (EditText) view;
            editText.setError(error);

        }else if (view instanceof TextView)
        {
            TextView textView = (TextView) view;
            textView.setError(error);

        }
    }

    @BindingAdapter("adImage")
    public static void adImage(View view,String endPoint)
    {
        if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {
                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_ADS+endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("subCategoryImage")
    public static void subCategoryImage(RoundedImageView roundedImageView,String endPoint)
    {
        if (endPoint!=null)
        {
            Picasso.with(roundedImageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_ADS_CATEGORY+endPoint)).fit().into(roundedImageView);
        }
    }

    @BindingAdapter("subCategoryIcon")
    public static void subCategoryIcon(View view,String endPoint)
    {
        if (view instanceof ImageView)
        {
            if (endPoint!=null)
            {
                ImageView imageView = (ImageView) view;

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_ADS_CATEGORY+endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("avatarUser")
    public static void avatarUser(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).placeholder(R.drawable.ic_user).fit().into(imageView);
            }else
            {
                Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).fit().into(imageView);
            }else
                {
                    Log.e("fff","fff");
                    Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

                }
        }

    }

    @BindingAdapter("chatImage")
    public static void chatImage(RoundedImageView roundedImageView,String endPoint)
    {
        if (endPoint!=null)
        {

            Picasso.with(roundedImageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_CHAT+endPoint)).fit().into(roundedImageView);
        }

    }
    @BindingAdapter("avatarCompany")
    public static void avatarCompany(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView circleImageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(circleImageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).fit().into(circleImageView);
            }
        }

    }


    @BindingAdapter({"date"})
    public static void displayTime(TextView textView,long time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String sTime = dateFormat.format(new Date(time*1000));
        textView.setText(sTime);
    }










}
