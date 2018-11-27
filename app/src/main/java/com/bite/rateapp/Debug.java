package com.bite.rateapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Debug
{
    public static final boolean debugging=true;//Установить false при создании .apk-файла для залива новой версии
    static final String TAG="rateme";
    public static void e(Throwable t, String msg)
    {
        if(debugging)
            Log.e(TAG, msg, t);
    }
    public static void e(Throwable t)
    {
        if(debugging)
            Log.e(TAG, getCallingClass()+" error", t);
    }
    public static void d(Throwable t, String msg)
    {
        if(debugging)
            Log.d(TAG, msg, t);
    }
    public static void d(Throwable t)
    {
        if(debugging)
            Log.d(TAG, "", t);
    }
    public static void print(String msg, Context ctx)
    {
        if(debugging)
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    private static String getCallingClass()
    {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for(int i=1; i<stElements.length; i++)
        {
            StackTraceElement ste = stElements[i];
            if(!ste.getClassName().equals(Debug.class.getName())
                    &&ste.getClassName().indexOf("java.lang.Thread")!=0)
            {
                return ste.getClassName();
            }
        }
        return null;
    }
}
