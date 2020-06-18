package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class FormDataModel implements Serializable {

    private List<TypeModel> types;
    private List<FormModel> form;
    private List<CityModel> cities;
    private List<SubCategory> sub_cat;

    public List<TypeModel> getTypes() {
        return types;
    }

    public List<FormModel> getForm() {
        return form;
    }

    public List<CityModel> getCities() {
        return cities;
    }

    public List<SubCategory> getSub_cat() {
        return sub_cat;
    }

    public static class TypeModel implements Serializable
    {
        private int type_id;
        private String title;

        public int getType_id() {
            return type_id;
        }

        public String getTitle() {
            return title;
        }

        public TypeModel(int type_id, String title) {
            this.type_id = type_id;
            this.title = title;
        }
    }

    public  class FormModel implements Serializable
    {
        private int definition_id;
        private String title;
        private String type;
        private List<OptionModel> options;

        public int getDefinition_id() {
            return definition_id;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public List<OptionModel> getOptions() {
            return options;
        }
    }

    public static class CityModel implements Serializable
    {
        private int city_id;
        private String ar_name;

        public int getCity_id() {
            return city_id;
        }

        public String getAr_name() {
            return ar_name;
        }

        public CityModel(int city_id, String ar_name) {
            this.city_id = city_id;
            this.ar_name = ar_name;
        }
    }

    public static class SubCategory implements Serializable
    {
        private int sub_id;
        private String title;

        public int getSub_id() {
            return sub_id;
        }

        public String getTitle() {
            return title;
        }

        public SubCategory(int sub_id, String title) {
            this.sub_id = sub_id;
            this.title = title;
        }
    }


    public static class OptionModel implements Serializable
    {
        private String option_title;
        private int option_id;
        private int definition_id;
        private String type;


        public OptionModel(String option_title, int option_id) {
            this.option_title = option_title;
            this.option_id = option_id;
        }

        public String getOption_title() {
            return option_title;
        }

        public int getOption_id() {
            return option_id;
        }

        public int getDefinition_id() {
            return definition_id;
        }

        public String getType() {
            return type;
        }

        public void setOption_title(String option_title) {
            this.option_title = option_title;
        }

        public void setOption_id(int option_id) {
            this.option_id = option_id;
        }

        public void setDefinition_id(int definition_id) {
            this.definition_id = definition_id;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

