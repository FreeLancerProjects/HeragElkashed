package com.creative.share.apps.heragelkashed.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.share.Common;


public class AddCompanyModel extends BaseObservable {

    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String details;
    private int cat_id;
    private double lat;
    private double lng;
    private String address;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_details = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();



    public AddCompanyModel() {
        this.name = "";
        notifyPropertyChanged(BR.name);
        this.phone_code = "";
        notifyPropertyChanged(BR.phone_code);
        this.phone = "";
        notifyPropertyChanged(BR.phone);

        this.email = "";
        notifyPropertyChanged(BR.email);

        this.details = "";
        notifyPropertyChanged(BR.details);

        this.cat_id = 0;
        notifyPropertyChanged(BR.cat_id);

        this.lat = 0.0;
        notifyPropertyChanged(BR.lat);

        this.lng = 0.0;
        notifyPropertyChanged(BR.lng);

        this.address = "";
        notifyPropertyChanged(BR.address);
    }

    public AddCompanyModel(String name, String phone_code, String phone, String email, String details, int cat_id, double lat, double lng, String address) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

        this.email = email;
        notifyPropertyChanged(BR.email);

        this.details = details;
        notifyPropertyChanged(BR.details);

        this.cat_id = cat_id;
        notifyPropertyChanged(BR.cat_id);

        this.lat = lat;
        notifyPropertyChanged(BR.lat);

        this.lng = lng;
        notifyPropertyChanged(BR.lng);

        this.address = address;
        notifyPropertyChanged(BR.address);


    }

    public boolean isDataValid(Context context, View view)
    {
        if (!TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !TextUtils.isEmpty(details)&&
                !TextUtils.isEmpty(address)&&
                cat_id!=0

        )
        {

            Common.CloseKeyBoard(context,view);
            error_name.set(null);
            error_address.set(null);
            error_details.set(null);
            error_phone.set(null);
            error_phone_code.set(null);

            return true;
        }else
            {

                if (TextUtils.isEmpty(name))
                {
                    error_name.set(context.getString(R.string.field_req));
                }else
                    {
                        error_name.set(null);

                    }

                if (TextUtils.isEmpty(email))
                {
                    error_email.set(context.getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    error_email.set(context.getString(R.string.inv_email));
                }else
                {
                    error_email.set(null);

                }

                if (TextUtils.isEmpty(phone_code))
                {
                    error_phone_code.set(context.getString(R.string.field_req));
                }else
                {
                    error_phone_code.set(null);

                }

                if (TextUtils.isEmpty(phone))
                {
                    error_phone.set(context.getString(R.string.field_req));
                }else
                {
                    error_phone.set(null);

                }


                if (TextUtils.isEmpty(address))
                {
                    error_address.set(context.getString(R.string.field_req));
                }else
                {
                    error_address.set(null);

                }

                if (TextUtils.isEmpty(details))
                {
                    error_details.set(context.getString(R.string.field_req));
                }else
                {
                    error_details.set(null);

                }

                if (cat_id==0)
                {
                    Toast.makeText(context,context.getString(R.string.ch_cat), Toast.LENGTH_SHORT).show();
                }

                return false;
            }
    }

    @Bindable
    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_cod) {
        this.phone_code = phone_cod;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyPropertyChanged(BR.details);

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

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }
}
