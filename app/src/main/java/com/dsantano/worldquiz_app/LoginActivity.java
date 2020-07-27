package com.dsantano.worldquiz_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final int GOOGLE_LOGIN = 123;
    FirebaseAuth mAuth;
    Button btnLogin;
    TextView txtNameApp;
    GoogleSignInClient mGoogleLogin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    Map<String, Object> userfb;
    ImageView ivLogo;
    String nameOfEmail, defaultPhoto, email, photo, name;
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivLogo = findViewById(R.id.imageViewLogoLogin);
        btnLogin = findViewById(R.id.buttonLogin);

        Glide.with(this)
                .load("https://png.pngtree.com/templates/md/20180526/md_5b09436f38c00.png")
                .transform(new CircleCrop())
                .error(Glide.with(this).load(R.drawable.image_not_loaded_icon))
                .thumbnail(Glide.with(this).load(R.drawable.loading_gif).transform( new CircleCrop()))
                .into(ivLogo);

        mAuth = FirebaseAuth.getInstance();
        checkIsLogged();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleLogin = GoogleSignIn.getClient(this, googleSignInOptions);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    public void signInGoogle(){
        Intent loginIntent = mGoogleLogin.getSignInIntent();
        startActivityForResult(loginIntent, GOOGLE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_LOGIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Error ", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser u) {
        checkIsLogged();
        user = u;
        if(user != null) {
            defaultPhoto = "https://covitalidad.edu.umh.es/wp-content/uploads/sites/1352/2018/06/default-user.png";
            userfb = new HashMap<>();
            userfb.put("uid", user.getUid());
            userfb.put("email", user.getEmail());
            if(user.getDisplayName() == null || user.getDisplayName().equals("")){
                nameOfEmail = user.getEmail().toString().split("@")[0];
                name = nameOfEmail;
                userfb.put("name", nameOfEmail);
            } else {
                name = user.getDisplayName();
                userfb.put("name", user.getDisplayName());
            }
            if(user.getPhotoUrl() == null){
                photo = defaultPhoto;
                userfb.put("photo", defaultPhoto);
            } else {
                photo = user.getPhotoUrl().toString();
                userfb.put("photo", user.getPhotoUrl().toString());
            }
            email = user.getEmail();
            DocumentReference docIdRef = db.collection("users").document(user.getUid());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("FB", "Document exists!");
                            db.collection("users")
                                    .document(user.getUid())
                                    .update(userfb);
                        } else {
                            Log.d("FB", "Document does not exist!");
                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userfb);
                        }
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("uid", user.getUid());
                        startActivity(i);
                        finish();
                    } else {
                        Log.d("FB", "Failed with: ", task.getException());
                    }
                }
            });
        } else {
            Toast.makeText(this, "Login Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        mGoogleLogin.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }

    public void checkIsLogged(){
        btnLogin = findViewById(R.id.buttonLogin);
        loading = findViewById(R.id.loading);
        txtNameApp = findViewById(R.id.textViewNameAppLogin);
        if(FirebaseAuth.getInstance().getUid() != null){
            btnLogin.setVisibility(View.GONE);
            txtNameApp.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            txtNameApp.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }

}
