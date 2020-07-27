package com.dsantano.worldquiz_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsantano.worldquiz_app.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizEndedActivity extends AppCompatActivity {

    String uid, nameUser, emailUser, photoUser;
    int puntuation, totalCorrectAnswers;
    Users user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView ivFoto;
    TextView txtScore, txtNumCorrect, txtUsername;
    Button btnBackToMenu;
    LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_ended);

        animation = findViewById(R.id.finishAnimation);

        uid = getIntent().getExtras().get("uid").toString();
        puntuation = getIntent().getExtras().get("puntuation").hashCode();

        txtUsername = findViewById(R.id.textViewUsernameQuizEnd);
        txtScore = findViewById(R.id.textViewPuntuationObtainedEndQuiz);
        txtNumCorrect = findViewById(R.id.textViewNumberOfCorrectAnswersEndQuiz);
        btnBackToMenu = findViewById(R.id.buttonGoToMenuEndQuiz);

        totalCorrectAnswers = puntuation/2;

        getUser();

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizEndedActivity.this, MainActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
                finish();
            }
        });
    }

    public void getUser(){
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
                            txtScore.setText(puntuation + " " +getResources().getString(R.string.puntos));
                            txtNumCorrect.setText(totalCorrectAnswers + "/5 " + getResources().getString(R.string.question));

                            switch (totalCorrectAnswers) {
                                case 0:
                                    animation.setAnimation("2068-error-cross.json");
                                    animation.playAnimation();
                                    break;
                                case 1:
                                    animation.setAnimation("2252-broken-stick-error.json");
                                    animation.playAnimation();
                                    break;
                                case 2:
                                    animation.setAnimation("7481-banana-boy.json");
                                    animation.playAnimation();
                                    break;
                                case 3:
                                    animation.setAnimation("1270-error.json");
                                    animation.playAnimation();
                                    break;
                                case 4:
                                    animation.setAnimation("1801-fireworks.json");
                                    animation.playAnimation();
                                    break;
                                case 5:
                                    animation.setAnimation("2497-trophy(1).json");
                                    animation.playAnimation();
                                    break;
                            }
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
