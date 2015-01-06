package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by pc07 on 27/03/14.
 */
public class ResourcesUtil {

    public static Drawable getDrawableByName(Context context, String name) {
        return context.getResources().getDrawable(
                context.getResources().getIdentifier(name,
                        "drawable", context.getPackageName())
        );
    }

}
