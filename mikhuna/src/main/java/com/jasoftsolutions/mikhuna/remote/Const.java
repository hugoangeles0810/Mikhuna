package com.jasoftsolutions.mikhuna.remote;

import com.jasoftsolutions.mikhuna.BuildConfig;

/**
 * Created by pc07 on 02/04/2014.
 */
public final class Const {

    /**
     * Indica si Bugsense (envío de correos de error) está activo
     */
    public static final boolean BUGSENSE_ENABLED = !BuildConfig.DEBUG;
//    public static final boolean BUGSENSE_ENABLED = true;

    /**
     * ID de la aplicación de Google
     */
    public static final String GOOGLE_APP_ID = "741004900173";

    /**
     * ID de la app en facebook
     */
    public static final String FACEBOOK_APP_ID = "1471838033066907";
//    public static final String GOOGLE_APP_ID = "790204040906";

    public static final String DEFAULT_SERVER_DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DEFAULT_USER_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Tiempo máximo de espera para la actualización de datos desde el servidor
     */
    public static final int MAX_UPDATING_MILLISECONDS_DELAY = 6000;

    /**
     * Indica la cantidad máxima de audits que se pueden enviar por post
     */
    public static final Integer MAX_AUDITS_X_POST = 20;


    public static final String BACKEND_BASE_URL;
    public static final String BACKEND_BASE_URL_ABOUT = "http://www.mikuna.co";

    static {
        if (BuildConfig.DEBUG) {
            BACKEND_BASE_URL = "http://www.mikhuna.com";
//            BACKEND_BASE_URL = "http://192.168.0.17/mikhuna";
//            BACKEND_BASE_URL = "http://190.117.240.6/mikhuna";
//            BACKEND_BASE_URL = "http://190.117.168.162/mikhuna";
//            BACKEND_BASE_URL = "http://192.168.40.107/mikhuna";
//            BACKEND_BASE_URL = "http://localhost/mikhuna";
        } else {
            BACKEND_BASE_URL = "http://www.mikhuna.com";
        }
    }



    public static final String BACKEND_REST_BASE_URL = BACKEND_BASE_URL + "/Rest";

    public static final int MAX_RATING_BAR_VALUE = 50;

    /**
     * Tiempo mínimo que debe permanecer visible la pantalla de bienvenida
     */
    public static final long SPLASH_MIN_DELAY = 3000;

}
