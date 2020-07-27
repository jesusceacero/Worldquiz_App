package com.dsantano.worldquiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dsantano.worldquiz_app.Interfaces.ICountryListener;
import com.dsantano.worldquiz_app.models.Country;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity implements ICountryListener {

    String uid;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        uid = getIntent().getExtras().get("uid").toString();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return true;
    }



    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemMyUser:
                Intent i = new Intent(MainActivity.this, UserLoggedDetailActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
                return true;
            case R.id.menuItemLogOut:
                logOut();
                return true;
            case R.id.menuItemTryQuiz:
                Intent in = new Intent(MainActivity.this, QuizActivity.class);
                in.putExtra("uid", FirebaseAuth.getInstance().getUid());
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut() {
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleLogin = GoogleSignIn.getClient(this, googleSignInOptions);
        FirebaseAuth.getInstance().signOut();
        mGoogleLogin.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onCountryClick(Country c) {
        Intent i = new Intent(this,
                CountryDetailActivity.class);
        i.putExtra("alpha", c.getAlpha2Code());
        startActivity(i);

    }
}
