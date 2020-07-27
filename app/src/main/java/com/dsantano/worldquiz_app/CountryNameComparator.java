package com.dsantano.worldquiz_app;

import com.dsantano.worldquiz_app.models.Country;

import java.util.Comparator;

public class CountryNameComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Country c1 = (Country) o1;
        Country c2 = (Country) o2;

        if(c1.getName() != null && c2.getName() != null) {
            return c1.getName().compareToIgnoreCase(c2.getName());
        }
        return 0;
    }
}
