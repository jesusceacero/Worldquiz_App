package com.dsantano.worldquiz_app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Country {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("topLevelDomain")
    @Expose
    public List<String> topLevelDomain = null;
    @SerializedName("alpha2Code")
    @Expose
    public String alpha2Code;
    @SerializedName("alpha3Code")
    @Expose
    public String alpha3Code;
    @SerializedName("callingCodes")
    @Expose
    public List<String> callingCodes = null;
    @SerializedName("capital")
    @Expose
    public String capital;
    @SerializedName("altSpellings")
    @Expose
    public List<String> altSpellings = null;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("subregion")
    @Expose
    public String subregion;
    @SerializedName("population")
    @Expose
    public Integer population;
    @SerializedName("latlng")
    @Expose
    public List<Double> latlng = null;
    @SerializedName("demonym")
    @Expose
    public String demonym;
    @SerializedName("area")
    @Expose
    public Double area;
    @SerializedName("gini")
    @Expose
    public Double gini;
    @SerializedName("timezones")
    @Expose
    public List<String> timezones = null;
    @SerializedName("borders")
    @Expose
    public List<String> borders = null;
    @SerializedName("nativeName")
    @Expose
    public String nativeName;
    @SerializedName("numericCode")
    @Expose
    public String numericCode;
    @SerializedName("currencies")
    @Expose
    public List<Currency> currencies = null;
    @SerializedName("languages")
    @Expose
    public List<Language> languages = null;
    @SerializedName("translations")
    @Expose
    public Translations translations;
    @SerializedName("flag")
    @Expose
    public String flag;
    @SerializedName("regionalBlocs")
    @Expose
    public List<RegionalBloc> regionalBlocs = null;
    @SerializedName("cioc")
    @Expose
    public String cioc;

}
