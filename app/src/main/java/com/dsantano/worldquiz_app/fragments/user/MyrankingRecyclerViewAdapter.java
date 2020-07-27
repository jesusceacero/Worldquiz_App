package com.dsantano.worldquiz_app.fragments.user;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dsantano.worldquiz_app.R;
import com.dsantano.worldquiz_app.models.Users;

import java.util.List;


public class MyrankingRecyclerViewAdapter extends RecyclerView.Adapter<MyrankingRecyclerViewAdapter.ViewHolder> {

    Context ctx;
    int layoutTemplate;
    List<Users> listData;
    View view;
    Boolean effectivity;


    public MyrankingRecyclerViewAdapter(Context ctx, int layoutTemplate, List<Users> listData, Boolean effectivity) {
        this.ctx = ctx;
        this.layoutTemplate = layoutTemplate;
        this.listData = listData;
        this.effectivity = effectivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ranking, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = listData.get(position);
        holder.tvEffectivity.setVisibility(View.GONE);
        holder.ivEffectivity.setVisibility(View.GONE);
        holder.tvName.setText(holder.mItem.getName());
        holder.tvGame.setText(String.valueOf(holder.mItem.getGamesPlayed()));
        holder.tvScore.setText(String.valueOf(holder.mItem.getScore()));
        holder.ivPosition.setVisibility(View.GONE);

        Glide.with(ctx).load(holder.mItem.getPhoto()).apply(RequestOptions.bitmapTransform(new CircleCrop())).error(Glide.with(ctx).load("https://www.kindpng.com/picc/m/381-3817314_transparent-groups-of-people-png-user-icon-round.png").apply(RequestOptions.bitmapTransform(new CircleCrop()))).into(holder.ivPhoto);

        if(effectivity) {
            holder.tvEffectivity.setVisibility(View.VISIBLE);
            holder.ivEffectivity.setVisibility(View.VISIBLE);
            holder.tvEffectivity.setText(String.valueOf(holder.mItem.getEffectiveness()));
            holder.tvGame.setVisibility(View.INVISIBLE);
            holder.tvScore.setVisibility(View.INVISIBLE);
            holder.ivMedal.setVisibility(View.INVISIBLE);
            holder.ivGame.setVisibility(View.INVISIBLE);
        } else {
            holder.tvEffectivity.setVisibility(View.GONE);
            holder.ivEffectivity.setVisibility(View.GONE);
            holder.tvGame.setVisibility(View.VISIBLE);
            holder.tvScore.setVisibility(View.VISIBLE);
            holder.ivMedal.setVisibility(View.VISIBLE);
            holder.ivGame.setVisibility(View.VISIBLE);
        }


        switch (position) {
            case 0:
                holder.mView.setBackgroundColor(ctx.getResources().getColor(R.color.goldBackground));
                holder.ivPosition.setVisibility(View.VISIBLE);
                holder.tvName.setTextColor(ctx.getResources().getColor(R.color.gold));
                holder.tvScore.setTextColor(ctx.getResources().getColor(R.color.gold));
                holder.tvGame.setTextColor(ctx.getResources().getColor(R.color.gold));
                holder.tvEffectivity.setTextColor(ctx.getResources().getColor(R.color.gold));
                break;
            case 1:
                holder.mView.setBackgroundColor(ctx.getResources().getColor(R.color.silverBackground));
                holder.ivPosition.setVisibility(View.VISIBLE);
                holder.ivPosition.setImageResource(R.drawable.ic_second_svgrepo_com);
                holder.tvName.setTextColor(ctx.getResources().getColor(R.color.silver));
                holder.tvScore.setTextColor(ctx.getResources().getColor(R.color.silver));
                holder.tvGame.setTextColor(ctx.getResources().getColor(R.color.silver));
                holder.tvEffectivity.setTextColor(ctx.getResources().getColor(R.color.silver));
                break;
            case 2:
                holder.mView.setBackgroundColor(ctx.getResources().getColor(R.color.bronzeBackground));
                holder.ivPosition.setVisibility(View.VISIBLE);
                holder.ivPosition.setImageResource(R.drawable.ic_third_svgrepo_com);
                holder.tvName.setTextColor(ctx.getResources().getColor(R.color.bronze));
                holder.tvScore.setTextColor(ctx.getResources().getColor(R.color.bronze));
                holder.tvGame.setTextColor(ctx.getResources().getColor(R.color.bronze));
                holder.tvEffectivity.setTextColor(ctx.getResources().getColor(R.color.bronze));
                break;

        }


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvName;
        public final TextView tvScore;
        public final TextView tvGame;
        public final TextView tvEffectivity;
        public final ImageView ivPhoto;
        public final ImageView ivMedal;
        public final ImageView ivGame;
        public final ImageView ivEffectivity;
        public final ImageView ivPosition;

        public Users mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvName = view.findViewById(R.id.textViewUsername);
            tvGame = view.findViewById(R.id.textViewGames);
            tvScore = view.findViewById(R.id.textViewScore);
            ivPhoto = view.findViewById(R.id.imageViewPhoto);
            tvEffectivity = view.findViewById(R.id.textViewEffec);
            ivMedal = view.findViewById(R.id.imageViewMedal);
            ivGame = view.findViewById(R.id.imageViewNumGame);
            ivEffectivity = view.findViewById(R.id.imageViewEffec);
            ivPosition = view.findViewById(R.id.imageViewPosition);
        }


    }
}
