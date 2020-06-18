package com.creative.share.apps.heragelkashed.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.heragelkashed.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddAdModel extends BaseObservable implements Serializable{
    private int main_cat_id;
    private int cat_id;
    private int sub_cat_id;
    private int ad_type_id;
    private int city_id;
    private List<FormDataModel.OptionModel> optionModelList = new ArrayList<>();
    private String ad_name;
    private String ad_price;
    private String ad_details;
    private String address;
    private double lat;
    private double lng;
    private List<AdImageVideoModel> images = new ArrayList<>();

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_details = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();


    public boolean isDataValid(Context context)
    {
        Log.e("main_cat_id",main_cat_id+"__");
        Log.e("cat_id",cat_id+"__");
        Log.e("ad_type_id",ad_type_id+"__");
        Log.e("city_id",city_id+"__");
        Log.e("optionModelList",optionModelList.size()+"__");
        Log.e("ad_name",ad_name+"__");
        Log.e("ad_price",ad_price+"__");
        Log.e("address",address+"__");
        Log.e("images",images.size()+"__");


        if (main_cat_id!=0&&
                cat_id!=0&&
                ad_type_id!=0&&
                city_id!=0&&
                optionModelList.size()>0&&
                !ad_name.isEmpty()&&
                !ad_details.isEmpty()&&
                !address.isEmpty()&&
                isListHasImage()&&
                images.size()>0
        )
        {

            error_name.set(null);
            //error_price.set(null);
            error_details.set(null);
            error_address.set(null);
            return true;
        }else
            {
                if (!isListHasImage())
                {
                    Toast.makeText(context,context.getString(R.string.ch_1_img), Toast.LENGTH_SHORT).show();
                }else
                if (images.size()==0)
                {
                    Toast.makeText(context, R.string.ch_1_img, Toast.LENGTH_SHORT).show();
                }


                if (cat_id==0)
                {
                    Toast.makeText(context, context.getString(R.string.ch_cat), Toast.LENGTH_SHORT).show();
                }

                if (ad_type_id == 0)
                {
                    Toast.makeText(context, context.getString(R.string.ch_type), Toast.LENGTH_SHORT).show();
                }

                if (city_id==0)
                {
                    Toast.makeText(context, context.getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
                }

                if (optionModelList.size()==0)
                {
                    Toast.makeText(context, R.string.ch_filters, Toast.LENGTH_SHORT).show();
                }




                if (ad_name.isEmpty())
                {
                    error_name.set(context.getString(R.string.field_req));

                }else
                    {
                        error_name.set(null);
                    }

                /*if (ad_price.isEmpty())
                {
                    error_price.set(context.getString(R.string.field_req));

                }else
                {
                    error_price.set(null);
                }*/

                if (ad_details.isEmpty())
                {
                    error_details.set(context.getString(R.string.field_req));

                }else
                {
                    error_details.set(null);
                }

                if (address.isEmpty())
                {
                    error_address.set(context.getString(R.string.field_req));

                }else
                {
                    error_address.set(null);
                }


                return false;
            }

    }

    private boolean isListHasImage()
    {
        for (AdImageVideoModel adImageVideoModel:images)
        {
            if (!adImageVideoModel.isVideo())
            {
                return true;
            }
        }

        return false;
    }
    public AddAdModel() {
        main_cat_id = 0;
        cat_id =0;
        sub_cat_id=0;
        ad_type_id=0;
        city_id =0;
        ad_name="";
        ad_details="";
        ad_price="";
        address ="";
        lat = 0.0;
        lng=0.0;
    }

    @Bindable
    public int getMain_cat_id() {
        return main_cat_id;
    }

    public void setMain_cat_id(int main_cat_id) {
        this.main_cat_id = main_cat_id;
        notifyPropertyChanged(BR.main_cat_id);
    }
    @Bindable
    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
        notifyPropertyChanged(BR.cat_id);

    }
    @Bindable
    public int getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(int sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
        notifyPropertyChanged(BR.sub_cat_id);

    }
    @Bindable
    public int getAd_type_id() {
        return ad_type_id;
    }

    public void setAd_type_id(int ad_type_id) {
        this.ad_type_id = ad_type_id;
        notifyPropertyChanged(BR.ad_type_id);

    }

    @Bindable
    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
        notifyPropertyChanged(BR.city_id);

    }

    public List<FormDataModel.OptionModel> getOptionModelList() {
        return optionModelList;
    }

    public void setOptionModelList(List<FormDataModel.OptionModel> optionModelList) {
        this.optionModelList = optionModelList;
    }

    @Bindable
    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
        notifyPropertyChanged(BR.ad_name);

    }

    @Bindable
    public String  getAd_price() {
        return ad_price;
    }

    public void setAd_price(String ad_price) {
        this.ad_price = ad_price;
        notifyPropertyChanged(BR.ad_price);

    }

    @Bindable
    public String getAd_details() {
        return ad_details;
    }

    public void setAd_details(String ad_details) {
        this.ad_details = ad_details;
        notifyPropertyChanged(BR.ad_details);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }

    @Bindable
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
        notifyPropertyChanged(BR.lat);

    }

    @Bindable
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
        notifyPropertyChanged(BR.lng);

    }


    public List<AdImageVideoModel> getImages() {
        return images;
    }

    public void setImages(List<AdImageVideoModel> images) {
        this.images = images;
    }
}
