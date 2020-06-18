package com.creative.share.apps.heragelkashed.models;

import java.io.Serializable;
import java.util.List;

public class ReportReasonDataModel implements Serializable {
    private List<ReportReasonModel> data;

    public List<ReportReasonModel> getData() {
        return data;
    }

    public class ReportReasonModel implements Serializable {

        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
