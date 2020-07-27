package com.dsantano.worldquiz_app.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private int gamesPlayed;
    private String name;
    private String photo;
    private int score;
    private String uid;
    private String email;

    public double getEffectiveness() {
        double result;
        BigDecimal bd;

        if(gamesPlayed == 0) {
            return 0;
        } else {
            result = (double) getScore() / (double) getGamesPlayed();
            bd = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP);

            return bd.doubleValue();
            
        }
    }
}
