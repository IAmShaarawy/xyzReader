package com.example.xyzreader.Utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by elshaarawy on 11-May-17.
 */

public class FontUtil {


    private static final String TAG = "FontUtil";

    public static void applyFonts(final View v)
    {
        Activity activity= (Activity) v.getContext();
        Typeface fontToSet = Typeface.createFromAsset(activity.getAssets(),"Roboto-Regular.ttf");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"البس علشان خرجين");
        }
    }

}
