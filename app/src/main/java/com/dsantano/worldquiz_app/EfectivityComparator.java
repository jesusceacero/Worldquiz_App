package com.dsantano.worldquiz_app;

import com.dsantano.worldquiz_app.models.Users;

import java.util.Comparator;

public class EfectivityComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Users u1 = (Users) o1;
        Users u2 = (Users) o2;

        if(u1.getEffectiveness() == u2.getEffectiveness()) {
            return 0;
        } else if(u1.getEffectiveness() > u2.getEffectiveness()) {
            return -1;
        } else {
            return 1;
        }
    }
}
