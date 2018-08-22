package com.great3.smartactivatedcollegewebbook;

/**
 * Created by Glenn on 1/22/2018.
 */


//#e8a81d
public class CardDetails {

    private String title;
    private String desc;
    private String image;


    public CardDetails() {

    }

    public CardDetails(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
