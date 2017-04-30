package compsci290.edu.duke.kvc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Bao on 4/11/2017.
 */

//use this class instead of having to repetedly call SharedPreferences and Editor
public class SharedPref {
    private static SharedPreferences sSharedPref;

    public static void initialize(Context c){
        if (sSharedPref == null){
            sSharedPref = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
        }
    }


    //for booleans
    public static void write(String tag, boolean value){
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putBoolean(tag, value);
        editor.commit();
    }

    public static boolean read(String tag, boolean defaultValue){
        return sSharedPref.getBoolean(tag, defaultValue);
    }


    //for Strings
    public static void write(String tag, String s){
        SharedPreferences.Editor editor = sSharedPref.edit();
        editor.putString(tag, s);
        editor.commit();
    }

    public static String read(String tag, String defaultValue){
        return sSharedPref.getString(tag, defaultValue);
    }

    //for ints
    public static void write(String tag, int i){
        SharedPreferences.Editor  editor = sSharedPref.edit();
        editor.putInt(tag, i);
        editor.commit();
    }

    public static int read(String tag, int defaultValue){
        return sSharedPref.getInt(tag, defaultValue);
    }
}
