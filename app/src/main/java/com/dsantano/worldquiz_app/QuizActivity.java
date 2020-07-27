package com.dsantano.worldquiz_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dsantano.worldquiz_app.models.Country;
import com.dsantano.worldquiz_app.models.Users;
import com.dsantano.worldquiz_app.retrofit.generator.CountryGenerator;
import com.dsantano.worldquiz_app.retrofit.services.CountryService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    List<Country> listCountrys;
    List<Country> listResultFromAsyncTask;
    Country countrySelected;
    CountryService service;
    int numCountrysForQuiz, gamesPlayedByUser, numQuestion, answer, correctAnswer, puntuation;
    String uid;
    Map<String, Object> userfb;
    Users user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtTittle;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnNext;
    ProgressBar progressBar;
    String questionTittleFromR, crtAnswer, secondAnswer, thirdAnswer, fourthAnswer;
    ImageView ivFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        numQuestion = 1;
        puntuation = 0;
        answer = 0;
        listCountrys = new ArrayList<Country>();
        listResultFromAsyncTask = new ArrayList<Country>();
        uid = getIntent().getExtras().get("uid").toString();
        txtTittle = findViewById(R.id.textViewTittleQuestion);
        btnAnswer1 = findViewById(R.id.buttonAnswerOne);
        btnAnswer2 = findViewById(R.id.buttonAnswerTwo);
        btnAnswer3 = findViewById(R.id.buttonAnswerThree);
        btnAnswer4 = findViewById(R.id.buttonAnswerFour);
        btnNext = findViewById(R.id.buttonNextQuiz);
        ivFlag = findViewById(R.id.imageViewFlagQuiz);
        progressBar = findViewById(R.id.progressBarQuiz);

        service = CountryGenerator.createService(CountryService.class);
        new DownloadCountrys().execute();

        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonAnswerOne){
            answer = 1;
            btnAnswer1.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnAnswer2.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer3.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer4.setTextColor(getResources().getColor(R.color.pure_black));
        } else if(id == R.id.buttonAnswerTwo){
            answer = 2;
            btnAnswer1.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer2.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnAnswer3.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer4.setTextColor(getResources().getColor(R.color.pure_black));
        } else if(id == R.id.buttonAnswerThree){
            answer = 3;
            btnAnswer1.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer2.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer3.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnAnswer4.setTextColor(getResources().getColor(R.color.pure_black));
        } else if(id == R.id.buttonAnswerFour){
            answer = 4;
            btnAnswer1.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer2.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer3.setTextColor(getResources().getColor(R.color.pure_black));
            btnAnswer4.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if(id == R.id.buttonNextQuiz){
            if(numQuestion == 5){
                checkPuntuation(answer, correctAnswer);
                finishQuiz();
            } else {
                btnAnswer1.setTextColor(getResources().getColor(R.color.pure_black));
                btnAnswer2.setTextColor(getResources().getColor(R.color.pure_black));
                btnAnswer3.setTextColor(getResources().getColor(R.color.pure_black));
                btnAnswer4.setTextColor(getResources().getColor(R.color.pure_black));
                numQuestion = numQuestion + 1;
                checkPuntuation(answer, correctAnswer);
                loadQuestion(numQuestion);
            }

        }
    }

    public void loadQuestion(int numQuestionToLoad){
        answer = 1;
        if(numQuestionToLoad >= 5){
            btnNext.setText(R.string.btn_finish_quiz);
        }
        Random r = new Random();
        int numRandom = r.nextInt(3 - 1) + 1;
        correctAnswer = r.nextInt(4- 1) + 1;
        Country correctCountry = listCountrys.get(numRandom);
        Country secondCountry = listCountrys.get(r.nextInt(5 - 4) + 4);
        Country thirdCountry = listCountrys.get(r.nextInt(7 - 6) + 6);
        Country fourthCountry = listCountrys.get(r.nextInt(10 - 8) + 8);
        switch (numQuestionToLoad){
            case 1:
                Random r5 = new Random();
                questionTittleFromR = getResources().getString(R.string.tittle_question_one);
                txtTittle.setText(questionTittleFromR + " " +correctCountry.getName() + "?");
                crtAnswer = correctCountry.getCapital();
                secondAnswer = secondCountry.getCapital();
                thirdAnswer = thirdCountry.getCapital();
                fourthAnswer = fourthCountry.getCapital();
                if(correctCountry.getCapital().isEmpty()){
                    crtAnswer = getResources().getString(R.string.dont_have_border);
                } else{
                    crtAnswer = correctCountry.getCapital();
                }
                if(secondCountry.getCapital().isEmpty()){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r5.nextInt(250 - 1) + 1);
                        if (countrySelected.getCapital().isEmpty()) {
                            i = 0;
                        } else if(crtAnswer.equals(countrySelected.getCapital())){
                            i = 0;
                        } else {
                            secondAnswer = countrySelected.getCapital();
                            i = 3;
                        }
                    }
                } else{
                    secondAnswer = secondCountry.getCapital();
                }
                if(thirdCountry.getCapital().isEmpty()){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r5.nextInt(250 - 1) + 1);
                        if (countrySelected.getCapital().isEmpty()) {
                            i = 0;
                        } else if(crtAnswer.equals(countrySelected.getCapital())){
                            i = 0;
                        } else if(crtAnswer.equals(secondAnswer)){
                            i = 0;
                        } else {
                            thirdAnswer = countrySelected.getCapital();
                            i = 3;
                        }
                    }
                } else{
                    thirdAnswer = thirdCountry.getCapital();
                }
                if(fourthCountry.getCapital().isEmpty()){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r5.nextInt(250 - 1) + 1);
                        if (countrySelected.getCapital().isEmpty()) {
                            i = 0;
                        } else if(crtAnswer.equals(countrySelected.getCapital())){
                            i = 0;
                        } else if(crtAnswer.equals(secondAnswer)){
                            i = 0;
                        } else if(crtAnswer.equals(thirdAnswer)){
                            i = 0;
                        } else {
                            fourthAnswer = countrySelected.getCapital();
                            i = 3;
                        }
                    }
                } else{
                    fourthAnswer = fourthCountry.getCapital();
                }
                switch (correctAnswer){
                    case 1:
                        btnAnswer1.setText(crtAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 2:
                        btnAnswer1.setText(secondAnswer);
                        btnAnswer2.setText(crtAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 3:
                        btnAnswer1.setText(thirdAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(crtAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 4:
                        btnAnswer1.setText(fourthAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(crtAnswer);
                        break;
                }
                break;
            case 2:
                Random r4 = new Random();
                questionTittleFromR = getResources().getString(R.string.tittle_question_two);
                txtTittle.setText(questionTittleFromR + " " +correctCountry.getName() + "?");
                crtAnswer = correctCountry.getCurrencies().get(0).getName();
                secondAnswer = secondCountry.getCurrencies().get(0).getName();
                thirdAnswer = thirdCountry.getCurrencies().get(0).getName();
                fourthAnswer = fourthCountry.getCurrencies().get(0).getName();
                if(crtAnswer.equals(secondAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r4.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())) {
                            i = 0;
                        } else {
                            if(countrySelected.getCurrencies().get(0).getSymbol() == null){
                                secondAnswer = countrySelected.getCurrencies().get(0).getName();
                            } else {
                                secondAnswer = countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol();
                            }
                            i = 3;
                        }
                    }
                } else {
                    if(secondCountry.getCurrencies().get(0).getSymbol() == null){
                        secondAnswer = secondCountry.getCurrencies().get(0).getName();
                    } else {
                        secondAnswer = secondCountry.getCurrencies().get(0).getName() + " " +secondCountry.getCurrencies().get(0).getSymbol();
                    }
                }
                if(crtAnswer.equals(thirdAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r4.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())) {
                            i = 0;
                        } else if(secondAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())){
                            i = 0;
                        } else {
                            if(countrySelected.getCurrencies().get(0).getSymbol() == null){
                                thirdAnswer = countrySelected.getCurrencies().get(0).getName();
                            } else {
                                thirdAnswer = countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol();
                            }
                            i = 3;
                        }
                    }
                } else {
                    if(thirdCountry.getCurrencies().get(0).getSymbol() == null){
                        thirdAnswer = thirdCountry.getCurrencies().get(0).getName();
                    } else {
                        thirdAnswer = thirdCountry.getCurrencies().get(0).getName() + " " +thirdCountry.getCurrencies().get(0).getSymbol();
                    }
                }
                if(crtAnswer.equals(fourthAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r4.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())) {
                            i = 0;
                        } else if(secondAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())){
                            i = 0;
                        } else if(thirdAnswer.equals(countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol())){
                            i = 0;
                        } else {
                            if(countrySelected.getCurrencies().get(0).getSymbol() == null){
                                fourthAnswer = countrySelected.getCurrencies().get(0).getName();
                            } else {
                                fourthAnswer = countrySelected.getCurrencies().get(0).getName() + " " +countrySelected.getCurrencies().get(0).getSymbol();
                            }
                            i = 3;
                        }
                    }
                } else {
                    if(fourthCountry.getCurrencies().get(0).getSymbol() == null){
                        fourthAnswer = fourthCountry.getCurrencies().get(0).getName();
                    } else {
                        fourthAnswer = fourthCountry.getCurrencies().get(0).getName() + " " +fourthCountry.getCurrencies().get(0).getSymbol();
                    }
                }
                crtAnswer = correctCountry.getCurrencies().get(0).getName()+ " " +correctCountry.getCurrencies().get(0).getSymbol();
                switch (correctAnswer){
                    case 1:
                        btnAnswer1.setText(crtAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 2:
                        btnAnswer1.setText(secondAnswer);
                        btnAnswer2.setText(crtAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 3:
                        btnAnswer1.setText(thirdAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(crtAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 4:
                        btnAnswer1.setText(fourthAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(crtAnswer);
                        break;
                }
                break;
            case 3:
                Random r2 = new Random();
                questionTittleFromR = getResources().getString(R.string.tittle_question_three);
                txtTittle.setText(questionTittleFromR + " " +correctCountry.getName() + "?");
                if(correctCountry.getBorders().isEmpty()){
                    crtAnswer = getResources().getString(R.string.dont_have_border);
                } else{
                    crtAnswer = correctCountry.getBorders().get(0);
                }
                if(secondCountry.getBorders().isEmpty()){
                    for(int i = 0; i< 2 ; i++){
                        countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                        if(countrySelected.getBorders().isEmpty()){
                            i = 0;
                        } else {
                            if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                                i = 0;
                            } else {
                                secondAnswer = countrySelected.getBorders().get(0);
                                i = 3;
                            }
                        }
                    }
                } else {
                    countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                    if(countrySelected.getBorders().isEmpty()){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                i = 3;
                            }
                        }
                    }
                    if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else {
                                    secondAnswer = countrySelected.getBorders().get(0);
                                    i = 3;
                                }
                            }
                        }
                    } else {
                        secondAnswer = countrySelected.getBorders().get(0);
                    }
                }
                if(thirdCountry.getBorders().isEmpty()){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                        if (countrySelected.getBorders().isEmpty()) {
                            i = 0;
                        } else {
                            if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                                i = 0;
                            } else if(secondAnswer.equals(countrySelected.getBorders().get(0))){
                                i = 0;
                            } else {
                                thirdAnswer = countrySelected.getBorders().get(0);
                                i = 3;
                            }
                        }
                    }
                } else {
                    countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                    if(countrySelected.getBorders().isEmpty()){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                i = 3;
                            }
                        }
                    }
                    if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else if(secondAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else {
                                    thirdAnswer = countrySelected.getBorders().get(0);
                                    i = 3;
                                }
                            }
                        }
                    } else {
                        thirdAnswer = countrySelected.getBorders().get(0);
                    }
                }
                if(fourthCountry.getBorders().isEmpty()){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                        if (countrySelected.getBorders().isEmpty()) {
                            i = 0;
                        } else if(secondAnswer.equals(countrySelected.getBorders().get(0))){
                            i = 0;
                        } else if(thirdAnswer.equals(countrySelected.getBorders().get(0))){
                            i = 0;
                        } else {
                            fourthAnswer = countrySelected.getBorders().get(0);
                            i = 3;
                        }
                    }
                } else {
                    countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                    if(countrySelected.getBorders().isEmpty()){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                i = 3;
                            }
                        }
                    }
                    if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                        for(int i = 0; i< 2 ; i++){
                            countrySelected = listResultFromAsyncTask.get(r2.nextInt(250 - 1) + 1);
                            if(countrySelected.getBorders().isEmpty()){
                                i = 0;
                            } else {
                                if(crtAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else if(secondAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else if(thirdAnswer.equals(countrySelected.getBorders().get(0))){
                                    i = 0;
                                } else {
                                    fourthAnswer = countrySelected.getBorders().get(0);
                                    i = 3;
                                }
                            }
                        }
                    } else {
                        fourthAnswer = countrySelected.getBorders().get(0);
                    }
                }
                switch (correctAnswer){
                    case 1:
                        btnAnswer1.setText(crtAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 2:
                        btnAnswer1.setText(secondAnswer);
                        btnAnswer2.setText(crtAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 3:
                        btnAnswer1.setText(thirdAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(crtAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 4:
                        btnAnswer1.setText(fourthAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(crtAnswer);
                        break;
                }
                break;
            case 4:
                ivFlag.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load("https://www.countryflags.io/"+correctCountry.getAlpha2Code()+"/flat/64.png")
                        .centerCrop()
                        .into(ivFlag);
                questionTittleFromR = getResources().getString(R.string.tittle_question_four);
                txtTittle.setText(questionTittleFromR + " " + "?");
                crtAnswer = correctCountry.getName();
                secondAnswer = secondCountry.getName();
                thirdAnswer = thirdCountry.getName();
                fourthAnswer = fourthCountry.getName();
                switch (correctAnswer){
                    case 1:
                        btnAnswer1.setText(crtAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 2:
                        btnAnswer1.setText(secondAnswer);
                        btnAnswer2.setText(crtAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 3:
                        btnAnswer1.setText(thirdAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(crtAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 4:
                        btnAnswer1.setText(fourthAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(crtAnswer);
                        break;
                }
                break;
            case 5:
                Random r3 = new Random();
                ivFlag.setVisibility(View.GONE);
                questionTittleFromR = getResources().getString(R.string.tittle_question_five);
                txtTittle.setText(questionTittleFromR + " " +correctCountry.getName() + "?");
                crtAnswer = correctCountry.getLanguages().get(0).getName();
                secondAnswer = secondCountry.getLanguages().get(0).getName();
                thirdAnswer = thirdCountry.getLanguages().get(0).getName();
                fourthAnswer = fourthCountry.getLanguages().get(0).getName();
                if(crtAnswer.equals(secondAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r3.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getLanguages().get(0).getName())) {
                            i = 0;
                        } else {
                            secondAnswer = countrySelected.getLanguages().get(0).getName();
                            i = 3;
                        }
                    }
                }
                if(crtAnswer.equals(thirdAnswer) || secondAnswer.equals(thirdAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r3.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getLanguages().get(0).getName())) {
                            i = 0;
                        } else if(secondAnswer.equals(countrySelected.getLanguages().get(0).getName())){
                            i = 0;
                        } else {
                            thirdAnswer = countrySelected.getLanguages().get(0).getName();
                            i = 3;
                        }
                    }
                }
                if(crtAnswer.equals(fourthAnswer)|| secondAnswer.equals(fourthAnswer) || thirdAnswer.equals(fourthAnswer)){
                    for(int i = 0; i< 2 ; i++) {
                        countrySelected = listResultFromAsyncTask.get(r3.nextInt(250 - 1) + 1);
                        if (crtAnswer.equals(countrySelected.getLanguages().get(0).getName())) {
                            i = 0;
                        } else if(secondAnswer.equals(countrySelected.getLanguages().get(0).getName())){
                            i = 0;
                        } else if(thirdAnswer.equals(countrySelected.getLanguages().get(0).getName())){
                            i = 0;
                        } else {
                            fourthAnswer = countrySelected.getLanguages().get(0).getName();
                            i = 3;
                        }
                    }
                }
                switch (correctAnswer){
                    case 1:
                        btnAnswer1.setText(crtAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 2:
                        btnAnswer1.setText(secondAnswer);
                        btnAnswer2.setText(crtAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 3:
                        btnAnswer1.setText(thirdAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(crtAnswer);
                        btnAnswer4.setText(fourthAnswer);
                        break;
                    case 4:
                        btnAnswer1.setText(fourthAnswer);
                        btnAnswer2.setText(secondAnswer);
                        btnAnswer3.setText(thirdAnswer);
                        btnAnswer4.setText(crtAnswer);
                        break;
                }
                break;
                default:
                    break;
        }
    }

    public void checkPuntuation(int answer, int correctAnswer){
        if(answer == correctAnswer){
            puntuation = puntuation + 2;
        }
    }

    public void finishQuiz() {
        userfb = new HashMap<>();
        DocumentReference docIdRef = db.collection("users").document(uid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(Users.class);
                        if (user != null) {
                            gamesPlayedByUser = user.getGamesPlayed();
                            int totalPuntuation = user.getScore();
                            userfb.put("gamesPlayed", gamesPlayedByUser + 1);
                            userfb.put("score", totalPuntuation + puntuation);
                        }
                        Log.d("FB", "Document exists!");
                        db.collection("users")
                                .document(uid)
                                .update(userfb);
                        Intent i = new Intent(QuizActivity.this, QuizEndedActivity.class);
                        i.putExtra("puntuation", puntuation);
                        i.putExtra("uid", uid);
                        startActivity(i);
                        finish();
                    } else {
                        Log.d("FB", "Document does not exist!");
                    }
                } else {
                    Log.d("FB", "Failed with: ", task.getException());
                }
            }
        });
    }

    public class DownloadCountrys extends AsyncTask<Void, Void, List<Country>>{

        List<Country> result;

        @Override
        protected void onPreExecute() {
            txtTittle = findViewById(R.id.textViewTittleQuestion);
            btnAnswer1 = findViewById(R.id.buttonAnswerOne);
            btnAnswer2 = findViewById(R.id.buttonAnswerTwo);
            btnAnswer3 = findViewById(R.id.buttonAnswerThree);
            btnAnswer4 = findViewById(R.id.buttonAnswerFour);
            btnNext = findViewById(R.id.buttonNextQuiz);
            ivFlag = findViewById(R.id.imageViewFlagQuiz);
            progressBar = findViewById(R.id.progressBarQuiz);
            progressBar.setVisibility(View.VISIBLE);
            txtTittle.setVisibility(View.GONE);
            btnAnswer1.setVisibility(View.GONE);
            btnAnswer2.setVisibility(View.GONE);
            btnAnswer3.setVisibility(View.GONE);
            btnAnswer4.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }

        @Override
        protected List<Country> doInBackground(Void... voids) {
            Call<List<Country>> callAllCountrys = service.allCountry();
            Response<List<Country>> responseAllCountrys = null;
            try {
                responseAllCountrys = callAllCountrys.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseAllCountrys.isSuccessful()) {
                result = responseAllCountrys.body();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Country> countries) {
            numCountrysForQuiz = 10;
            for(int i = 0; i<numCountrysForQuiz; i++){
                Random r = new Random();
                listCountrys.add(countries.get(r.nextInt(250 - 1) + 1));
            }
            listResultFromAsyncTask.addAll(countries);
            progressBar.setVisibility(View.GONE);
            txtTittle.setVisibility(View.VISIBLE);
            btnAnswer1.setVisibility(View.VISIBLE);
            btnAnswer2.setVisibility(View.VISIBLE);
            btnAnswer3.setVisibility(View.VISIBLE);
            btnAnswer4.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            loadQuestion(numQuestion);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_priority_high_black_24dp)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userfb = new HashMap<>();
                            DocumentReference docIdRef = db.collection("users").document(uid);
                            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            user = document.toObject(Users.class);
                                            if (user != null) {
                                                gamesPlayedByUser = user.getGamesPlayed();
                                                userfb.put("gamesPlayed", gamesPlayedByUser + 1);
                                            }
                                            Log.d("FB", "Document exists!");
                                            db.collection("users")
                                                    .document(uid)
                                                    .update(userfb);
                                        } else {
                                            Log.d("FB", "Document does not exist!");
                                        }
                                    } else {
                                        Log.d("FB", "Failed with: ", task.getException());
                                    }
                                }
                            });
                            QuizActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
