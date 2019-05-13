package com.saint_gab.sacconnecte;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public static void changeLocale(Resources res, String locale)
    {
        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch (locale)
        {
            case "en":
                config.setLocale(Locale.ENGLISH);
                break;
            case "fr":
                config.setLocale(Locale.FRENCH);
                break;
            default:
                config.setLocale(Locale.ENGLISH);
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
