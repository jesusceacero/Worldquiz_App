package com.dsantano.worldquiz_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Language {

    @SerializedName("iso639_1")
    @Expose
    public String iso6391;
    @SerializedName("iso639_2")
    @Expose
    public String iso6392;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("nativeName")
    @Expose
    public String nativeName;

}
