package com.dsantano.worldquiz_app.fragments.map;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dsantano.worldquiz_app.CountryDetailActivity;
import com.dsantano.worldquiz_app.R;
import com.dsantano.worldquiz_app.models.Country;
import com.dsantano.worldquiz_app.models.CountryLocation;
import com.dsantano.worldquiz_app.models.MarkerClusterRenderer;
import com.dsantano.worldquiz_app.retrofit.generator.CountryGenerator;
import com.dsantano.worldquiz_app.retrofit.services.CountryService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Country> listaCountry;
    private CountryService service;
    private View v;
    private Marker m;
    private ClusterManager<CountryLocation> mClusterManager;
    private List<CountryLocation> items = new ArrayList<CountryLocation>();
    private ClusterManager<CountryLocation> clusterManager;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        setUpClusterManager(googleMap);

    }

    private void setUpClusterManager(GoogleMap googleMap){
        clusterManager = new ClusterManager<CountryLocation>(v.getContext(), googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);// 4
        clusterManager.setRenderer(new MarkerClusterRenderer(getActivity(), googleMap, clusterManager));
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                CountryLocation c = (CountryLocation) marker.getTag();
                Intent i = new Intent(getActivity(),
                        CountryDetailActivity.class);
                i.putExtra("alpha", c.getIsoCode());
                getActivity().startActivity(i);
            }
        });


        service = CountryGenerator.createService(CountryService.class);

        Call<List<Country>> call = service.allCountry();

        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful()) {
                    listaCountry = response.body();
                    for (final Country c : listaCountry){
                        if (!c.getLatlng().isEmpty()){
                            Glide.with(getContext())
                                    .asBitmap()
                                    .load("https://www.countryflags.io/"+c.alpha2Code+"/flat/64.png")
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            clusterManager.addItem(new CountryLocation(c.getName(), new LatLng(c.getLatlng().get(0), c.getLatlng().get(1)), c.alpha2Code,resource));
                                        }
                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        }
                    }
                } else {
                    Toast.makeText(v.getContext(), "Error al realizar la petición", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());
                Toast.makeText(v.getContext(), "Error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });
        clusterManager.cluster();  // 5
    }


}
