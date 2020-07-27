package com.dsantano.worldquiz_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Translations {

    @SerializedName("de")
    @Expose
    public String de;
    @SerializedName("es")
    @Expose
    public String es;
    @SerializedName("fr")
    @Expose
    public String fr;
    @SerializedName("ja")
    @Expose
    public String ja;
    @SerializedName("it")
    @Expose
    public String it;
    @SerializedName("br")
    @Expose
    public String br;
    @SerializedName("pt")
    @Expose
    public String pt;
    @SerializedName("nl")
    @Expose
    public String nl;
    @SerializedName("hr")
    @Expose
    public String hr;
    @SerializedName("fa")
    @Expose
    public String fa;
}
