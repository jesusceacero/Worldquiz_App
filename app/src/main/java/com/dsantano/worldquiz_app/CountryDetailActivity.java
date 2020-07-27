package com.dsantano.worldquiz_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsantano.worldquiz_app.models.Country;
import com.dsantano.worldquiz_app.models.UnsplashPhotosResult;
import com.dsantano.worldquiz_app.retrofit.generator.CountryGenerator;
import com.dsantano.worldquiz_app.retrofit.generator.UnsplashGenerator;
import com.dsantano.worldquiz_app.retrofit.services.CountryService;
import com.dsantano.worldquiz_app.retrofit.services.UnspashService;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryDetailActivity extends AppCompatActivity {

    String alpha, nameCountry;
    TextView nombre, capital, poblacion, region,money,languaje;
    ImageView foto, bandera, capitaI, moneyI, poblacionI, latI, regionI, languajeI;
    private CountryService service;
    UnspashService unspashService;
    Country c;
    SliderView sliderView;
    List<String> listCarrousel;
    SliderAdapterExample adapter;
    int numImgCarrousel = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        alpha = getIntent().getExtras().getString("alpha");
        capitaI = findViewById(R.id.imageViewCapital);
        moneyI = findViewById(R.id.imageViewMoney);
        languajeI = findViewById(R.id.imageViewLanguage);
        poblacionI = findViewById(R.id.imageViewPopulation);
        latI = findViewById(R.id.imageViewLocation);
        regionI = findViewById(R.id.imageViewRegion);

        nombre = findViewById(R.id.textViewNameDetail);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.capital)
                .centerCrop()
                .into(capitaI);
        capital = findViewById(R.id.CapitalDetailEdit);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.population)
                .centerCrop()
                .into(poblacionI);
        poblacion = findViewById(R.id.populationDetailEdit);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.location)
                .centerCrop()
                .into(latI);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.continent)
                .centerCrop()
                .into(regionI);
        region = findViewById(R.id.regionDetailEdit);
        bandera = findViewById(R.id.imageViewBanderaDetail);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.money)
                .centerCrop()
                .into(moneyI);
        money = findViewById(R.id.MoneyDetailEdit);
        Glide.with(CountryDetailActivity.this)
                .load(R.drawable.language)
                .centerCrop()
                .into(languajeI);
        languaje = findViewById(R.id.textViewLanguaje);


        service = CountryGenerator.createService(CountryService.class);
        unspashService = UnsplashGenerator.createService(UnspashService.class);

        Call<Country> call = service.getCountry(alpha);

        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                if (response.isSuccessful()) {
                    c = response.body();
                    nameCountry = response.body().getName();
                    nombre.setText(nameCountry);
                    capital.setText(response.body().getCapital());
                    if(response.body().getPopulation() >= 1000000) {
                        poblacion.setText((response.body().getPopulation() / 1000000) + "M");
                    } else if(response.body().getPopulation() >= 100000) {
                        poblacion.setText((response.body().getPopulation() / 100000) + "K");
                    } else if(response.body().getPopulation() >= 10000) {
                        poblacion.setText((response.body().getPopulation() / 10000) + "K");
                    } else if(response.body().getPopulation() >= 1000) {
                        poblacion.setText((response.body().getPopulation() / 1000) + "K");
                    } else {
                        poblacion.setText((response.body().getPopulation()));
                    }
                    region.setText(response.body().getRegion());
                    money.setText(response.body().getCurrencies().get(0).name + "");
                    languaje.setText(response.body().getLanguages().get(0).name + "");


                    Glide.with(CountryDetailActivity.this)
                            .load("https://www.countryflags.io/"+response.body().alpha2Code+"/flat/64.png")
                            .thumbnail(Glide.with(CountryDetailActivity.this).load(R.drawable.loading_gif).transform(new CircleCrop()))
                            .centerCrop()
                            .into(bandera);
                    new DowloadPhotosOfCountry().execute();
                } else {
                    Toast.makeText(CountryDetailActivity.this, "Error al realizar la petición", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(CountryDetailActivity.this, "Error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        latI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.es/maps/place/"+c.getName()));
                CountryDetailActivity.this.startActivity(webIntent);
            }
        });

    }

    public class DowloadPhotosOfCountry extends AsyncTask<Void, Void, UnsplashPhotosResult>{

        UnsplashPhotosResult result;
        @Override
        protected UnsplashPhotosResult doInBackground(Void... voids) {
            Call<UnsplashPhotosResult> callCountryPhotos = unspashService.getEspecificCountryPhotos(nameCountry);
            Response<UnsplashPhotosResult> responseCountryPhotos = null;
            try {
                responseCountryPhotos = callCountryPhotos.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseCountryPhotos.isSuccessful()) {
                result = responseCountryPhotos.body();
            }
            return result;
        }
        @Override
        protected void onPostExecute(UnsplashPhotosResult unsplashPhotosResult) {
            sliderView = findViewById(R.id.imageSlider);
            listCarrousel = new ArrayList<String>();
            if(unsplashPhotosResult.results.size() < numImgCarrousel){
                numImgCarrousel = unsplashPhotosResult.results.size();
            }
            for(int i=0; i<numImgCarrousel; i++) {
                listCarrousel.add(unsplashPhotosResult.results.get(i).urls.regular);
            }
            adapter = new SliderAdapterExample(CountryDetailActivity.this,listCarrousel);

            sliderView.setSliderAdapter(adapter);
            sliderView.setIndicatorAnimation(IndicatorAnimations.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();
        }
    }

}
