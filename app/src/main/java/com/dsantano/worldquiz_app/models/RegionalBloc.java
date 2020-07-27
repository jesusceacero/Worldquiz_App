package com.dsantano.worldquiz_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class RegionalBloc {

    @SerializedName("acronym")
    @Expose
    public String acronym;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("otherAcronyms")
    @Expose
    public List<Object> otherAcronyms = null;
    @SerializedName("otherNames")
    @Expose
    public List<Object> otherNames = null;

}
