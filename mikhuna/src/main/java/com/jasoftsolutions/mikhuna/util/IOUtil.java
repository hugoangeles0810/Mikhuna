package com.jasoftsolutions.mikhuna.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pc07 on 08/04/2014.
 */
public class IOUtil {

    private static final String TAG = IOUtil.class.getSimpleName();

    public static final String getAllTextInput(InputStream inputStream) throws IOException {
        InputStreamReader isr=new InputStreamReader(inputStream);
        char[] buffer=new char[10000];
        StringBuilder result=new StringBuilder();
        int count;
        while ((count = isr.read(buffer))>0) {
            result.append(buffer, 0, count);
        }
        return result.toString();
    }

    public static final String getAssetTextFileContent(Context context, String filename) throws IOException {
        InputStream is = context.getAssets().open(filename);
        String result = getAllTextInput(is);
        is.close();
        return result;
    }
}
