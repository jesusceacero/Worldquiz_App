package com.dsantano.worldquiz_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsantano.worldquiz_app.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserLoggedDetailActivity extends AppCompatActivity {

    String uid, nameUser, emailUser, photoUser;
    ImageView ivUser;
    TextView txtUsername, txtEmail, txtPuntuation, txtGamesPlayed, txtUsernameTittle, txtEmailTittle, txtPuntuationTittle, txtGamesPlayedTittle;
    Users user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_detail);

        uid = getIntent().getExtras().get("uid").toString();

        ivUser = findViewById(R.id.imageViewUserDetail);
        txtUsername = findViewById(R.id.textViewUsernameUserDetails);
        txtEmail = findViewById(R.id.textViewEmailUserDetails);
        txtPuntuation = findViewById(R.id.textViewPuntuationUserDetails);
        txtGamesPlayed = findViewById(R.id.textViewGamesPlayedUserDetails);
        progressBar = findViewById(R.id.progressBarUserDetails);
        txtUsernameTittle = findViewById(R.id.textViewUsernameTittle);
        txtEmailTittle = findViewById(R.id.textViewEmailTittle);
        txtPuntuationTittle = findViewById(R.id.textViewPuntuationTittleUserLogged);
        txtGamesPlayedTittle = findViewById(R.id.textViewGamesPlayedTittle);

        getUser();
    }

    public void getUser(){
        progressBar.setVisibility(View.VISIBLE);
        ivUser.setVisibility(View.GONE);
        txtUsername.setVisibility(View.GONE);
        txtEmail.setVisibility(View.GONE);
        txtPuntuation.setVisibility(View.GONE);
        txtGamesPlayed.setVisibility(View.GONE);
        txtUsernameTittle.setVisibility(View.GONE);
        txtEmailTittle.setVisibility(View.GONE);
        txtPuntuationTittle.setVisibility(View.GONE);
        txtGamesPlayedTittle.setVisibility(View.GONE);
        DocumentReference docIdRef = db.collection("users").document(uid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(Users.class);
                        if (user != null) {
                            nameUser = user.getName();
                            emailUser = user.getEmail();
                            photoUser = user.getPhoto();
                            txtUsername.setText(nameUser);
                            txtEmail.setText(emailUser);
                            txtPuntuation.setText(String.valueOf(user.getScore()));
                            txtGamesPlayed.setText(String.valueOf(user.getGamesPlayed()));
                            Glide
                                    .with(UserLoggedDetailActivity.this)
                                    .load(photoUser)
                                    .transform(new CircleCrop())
                                    .error(Glide.with(UserLoggedDetailActivity.this).load(R.drawable.image_not_loaded_icon).transform(new CircleCrop()))
                                    .thumbnail(Glide.with(UserLoggedDetailActivity.this).load(R.drawable.loading_gif).transform(new CircleCrop()))
                                    .into(ivUser);
                            progressBar.setVisibility(View.GONE);
                            ivUser.setVisibility(View.VISIBLE);
                            txtUsername.setVisibility(View.VISIBLE);
                            txtEmail.setVisibility(View.VISIBLE);
                            txtPuntuation.setVisibility(View.VISIBLE);
                            txtGamesPlayed.setVisibility(View.VISIBLE);
                            txtUsernameTittle.setVisibility(View.VISIBLE);
                            txtEmailTittle.setVisibility(View.VISIBLE);
                            txtPuntuationTittle.setVisibility(View.VISIBLE);
                            txtGamesPlayedTittle.setVisibility(View.VISIBLE);
                        }
                        Log.d("FB", "Document exists!");
                    } else {
                        Log.d("FB", "Document does not exist!");
                    }
                } else {
                    Log.d("FB", "Failed with: ", task.getException());
                }
            }
        });
    }
}
