package com.dsantano.worldquiz_app.fragments.country;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsantano.worldquiz_app.CountryNameComparator;
import com.dsantano.worldquiz_app.R;
import com.bumptech.glide.Glide;
import com.dsantano.worldquiz_app.Interfaces.ICountryListener;
import com.dsantano.worldquiz_app.models.Country;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MycountryRecyclerViewAdapter extends RecyclerView.Adapter<MycountryRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Country> mValues;
    private final List<Country> mValuesAll;
    private int layout;
    private Context ctx;
    private boolean order;


    public MycountryRecyclerViewAdapter(Context ctx, int layout, List<Country> items, boolean order) {
        this.mValues = items;
        this.ctx = ctx;
        this.layout = layout;
        this.mValuesAll = new ArrayList<>(mValues);
        this.order = order;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String nombreF = holder.mItem.getName();
        if (nombreF.length() > 20){
            holder.nombre.setText(nombreF.substring(0,19)+"...");
        }else {
            holder.nombre.setText(nombreF);
        }

        //CircularImageView circularImageView = holder.mView.findViewById(R.id.imageViewBandera);
        //circularImageView.setCircleColor(Color.LTGRAY);
        //circularImageView.setBorderWidth(2f);
        //circularImageView.setBorderColor(Color.GRAY);

        Glide.with(ctx)
                .load("https://www.countryflags.io/"+holder.mItem.alpha2Code+"/flat/64.png")
                .centerCrop()
                .into(holder.bandera);

        Glide.with(ctx)
                .load(R.drawable.ic_chevron_right_grey_24dp)
                .centerCrop()
                .into(holder.flecha);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ICountryListener act = (ICountryListener) ctx;
                act.onCountryClick(holder.mItem);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<Country> filteredList = new ArrayList<Country>();


                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(mValuesAll);
                } else {
                    for (Country country : mValuesAll) {
                        for (int i = 0; i < country.getLanguages().size(); i++) {
                            if (country.getLanguages().get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase()) || country.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredList.add(country);
                                break;
                            } else {
                                for(int j = 0; j < country.getCurrencies().size(); j++) {
                                    if(country.getCurrencies().get(j).getName() != null) {
                                        if(country.getCurrencies().get(j).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                            filteredList.add(country);
                                            break;
                                        }
                                    }

                                }
                            }
                            break;
                        }

                    }
                }


            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues.clear();
            mValues.addAll((Collection<? extends Country>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nombre;
        public final ImageView bandera;
        public final ImageView flecha;
        public Country mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nombre = view.findViewById(R.id.textViewNombreEdit);
            bandera = view.findViewById(R.id.imageViewBandera);
            flecha = view.findViewById(R.id.imageViewFlecha);
        }

    }

}
