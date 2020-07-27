package com.dsantano.worldquiz_app.fragments.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsantano.worldquiz_app.EfectivityComparator;
import com.dsantano.worldquiz_app.R;
import com.dsantano.worldquiz_app.ScoreComparator;
import com.dsantano.worldquiz_app.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;


public class rankingFragment extends Fragment {

    private int mColumnCount = 1;
    private List<Users> usersList;
    private MyrankingRecyclerViewAdapter myrankingRecyclerViewAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context mContext;
    private RecyclerView recyclerView;
    private View view;
    private boolean order;


    public rankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking_list, container, false);
        usersList = new ArrayList<Users>();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        usersList = task.getResult().toObjects(Users.class);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }

                    Collections.sort(usersList, new ScoreComparator());

                    recyclerView = view.findViewById(R.id.listRanking);

                    myrankingRecyclerViewAdapter = new MyrankingRecyclerViewAdapter(
                            mContext,
                            R.layout.fragment_ranking,
                            usersList,
                            order);

                    recyclerView.setAdapter(myrankingRecyclerViewAdapter);


                }
            });

        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ranking_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter_ranking:
                if(order) {
                    item.setIcon(R.drawable.ic_repeat_one_white_24dp);
                    Collections.sort(usersList, new ScoreComparator());

                    myrankingRecyclerViewAdapter = new MyrankingRecyclerViewAdapter(
                            mContext,
                            R.layout.fragment_ranking,
                            usersList,
                            false);
                    recyclerView.setAdapter(myrankingRecyclerViewAdapter);
                    item.setTitle(getResources().getString(R.string.filteringE));
                } else {
                    item.setIcon(R.drawable.ic_repeat_white_24dp);
                    Collections.sort(usersList, new EfectivityComparator());

                    myrankingRecyclerViewAdapter = new MyrankingRecyclerViewAdapter(
                            mContext,
                            R.layout.fragment_ranking,
                            usersList,
                            true);

                    recyclerView.setAdapter(myrankingRecyclerViewAdapter);
                    item.setTitle(getResources().getString(R.string.filteringS));
                }
                order = !order;
                //TODO ordererRanking();
                break;
//            case R.id.action_order_ranking:
//                if(ordenAsc) {
//                    item.setIcon(R.drawable.ic_dashboard_black_24dp);
//                } else {
//                    item.setIcon(R.drawable.ic_home_black_24dp);
//                }
//                ordenAsc = !ordenAsc;
//                //TODO orederRanking();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
