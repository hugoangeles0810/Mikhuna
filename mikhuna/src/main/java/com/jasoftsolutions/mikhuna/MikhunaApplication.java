package com.jasoftsolutions.mikhuna;

import android.app.Application;

import com.bugsense.trace.BugSenseHandler;
import com.jasoftsolutions.mikhuna.data.MikhunaSQLiteOpenHelper;
import com.jasoftsolutions.mikhuna.google.gcm.GCMManager;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.routing.Routes;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by pc07 on 02/04/2014.
 */
public class MikhunaApplication extends Application {

    private static final String TAG = MikhunaApplication.class.getSimpleName();

    private GCMManager gcmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        setup();
        Routes.setup(this);
//        Intent updaterServiceIntent=new Intent(this, UpdaterService.class);
//        startService(updaterServiceIntent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        terminateBugSense();
    }

    private void setup() {
        setupBugSense();
        // Configurar base de datos
        MikhunaSQLiteOpenHelper.setup(this);
        // Configurar GCM
        gcmManager=new GCMManager(this);
        gcmManager.setup();
        // Configurar carga de imágenes (con caché)
        setupImageLoader();
    }

    private void setupBugSense() {
        if (Const.BUGSENSE_ENABLED) {
            BugSenseHandler.initAndStartSession(this, "26a2a4db");
        }
    }

    private void terminateBugSense() {
        if (Const.BUGSENSE_ENABLED) {
            BugSenseHandler.closeSession(this);
        }
    }

    private void setupImageLoader() {
        // Habilitar la cache de imágenes por defecto
        DisplayImageOptions displayImageOptions=new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(displayImageOptions)
                .discCacheSize(5*1024*1024)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

}
