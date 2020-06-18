package com.creative.share.apps.heragelkashed.activities_fragments.activity_filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.heragelkashed.R;
import com.creative.share.apps.heragelkashed.adapter.SpinnerDataChildAdapter;
import com.creative.share.apps.heragelkashed.databinding.ActivityFilterBinding;
import com.creative.share.apps.heragelkashed.databinding.SpinnerDataChildBinding;
import com.creative.share.apps.heragelkashed.databinding.TextDataChildBinding;
import com.creative.share.apps.heragelkashed.interfaces.Listeners;
import com.creative.share.apps.heragelkashed.language.LanguageHelper;
import com.creative.share.apps.heragelkashed.models.FormDataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FilterActivity extends AppCompatActivity implements Listeners.BackListener{

    private ActivityFilterBinding binding;
    private String lang;
    private List<FormDataModel.FormModel> formModelList;
    private List<LinearLayout> containers;
    private List<FormDataModel.OptionModel> data = new ArrayList<>();
    private List<Integer> errorTags = new ArrayList<>();


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            formModelList = (List<FormDataModel.FormModel>) intent.getSerializableExtra("data");
        }
    }

    private void initView() {
        containers = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);

        binding.btnDone.setOnClickListener((v)->{

            if(checkData())
            {
                Intent intent = getIntent();
                if (intent!=null)
                {
                    intent.putExtra("data", (Serializable) data);
                    setResult(RESULT_OK,intent);
                }

                finish();
            }


        });
        updateUi();

    }

    private void updateUi() {


        for (FormDataModel.FormModel formModel:formModelList)
        {

            if (formModel.getType().equals("select"))
            {
                SpinnerDataChildBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.spinner_data_child,null,false);
                rowBinding.tvTitle.setText(formModel.getTitle());
                binding.llContainer.addView(rowBinding.getRoot());
                rowBinding.spinnerData.setTag(formModel.getDefinition_id());
                LinearLayout linearLayout = (LinearLayout) rowBinding.getRoot();
                linearLayout.setTag(formModel.getDefinition_id());
                containers.add(linearLayout);

                List<FormDataModel.OptionModel> list = new ArrayList<>();
                list.add(new FormDataModel.OptionModel(getString(R.string.ch),0));
                list.addAll(formModel.getOptions());
                SpinnerDataChildAdapter adapter = new SpinnerDataChildAdapter(list,this);
                rowBinding.spinnerData.setAdapter(adapter);


            }else if (formModel.getType().equals("text"))
            {
                TextDataChildBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.text_data_child,null,false);
                rowBinding.tvTitle.setText(formModel.getTitle());
                rowBinding.edtData.setTag(formModel.getDefinition_id());

                LinearLayout linearLayout = (LinearLayout) rowBinding.getRoot();
                linearLayout.setTag(formModel.getDefinition_id());
                binding.llContainer.addView(linearLayout);

                containers.add(linearLayout);

            }
        }

        binding.llContainer.requestLayout();
    }

    @Override
    public void back()
    {
        finish();
    }

    private boolean checkData() {

        errorTags.clear();
        data.clear();
        for (LinearLayout linearLayout :containers)
        {
            if (linearLayout.findViewById(R.id.spinnerData)!=null)
            {
                Spinner spinner = linearLayout.findViewById(R.id.spinnerData);
                FormDataModel.OptionModel optionModel = (FormDataModel.OptionModel) spinner.getSelectedItem();
                optionModel.setType("select");
                int pos = spinner.getSelectedItemPosition();
                if (pos!=0)
                {
                    optionModel.setDefinition_id((int)linearLayout.getTag());
                    data.add(optionModel);
                }else
                {
                    errorTags.add((int)linearLayout.getTag());
                }

            }else if (linearLayout.findViewById(R.id.edtData)!=null)
            {
                EditText editText = linearLayout.findViewById(R.id.edtData);
                String text = editText.getText().toString().trim();
                FormDataModel.OptionModel optionModel =new FormDataModel.OptionModel(text,0);
                optionModel.setType("text");

                if (!TextUtils.isEmpty(text))
                {
                    optionModel.setDefinition_id((int)linearLayout.getTag());
                    data.add(optionModel);
                }else
                {
                    errorTags.add((int)linearLayout.getTag());
                }


            }
        }

        if (errorTags.size()==0)
        {
            return true;
        }else
            {
                for (int tag :errorTags)
                {
                    for (LinearLayout linearLayout :containers)
                    {
                        if ((int)linearLayout.getTag()==tag)
                        {

                            if (linearLayout.findViewById(R.id.spinnerData)!=null)
                            {
                                TextView textView = linearLayout.findViewById(R.id.tvTitle);

                                Toast.makeText(this, "إختر "+textView.getText().toString(), Toast.LENGTH_SHORT).show();
                            }else if (linearLayout.findViewById(R.id.edtData)!=null)
                            {
                                EditText editText = linearLayout.findViewById(R.id.edtData);
                                editText.setError(getString(R.string.field_req));
                            }

                        }
                    }
                }

                return false;
            }
    }
}
