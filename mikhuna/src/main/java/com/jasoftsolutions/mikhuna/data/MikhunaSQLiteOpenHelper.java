package com.jasoftsolutions.mikhuna.data;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.IOUtil;

import java.io.IOException;

public class MikhunaSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = MikhunaSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_NAME="mikhuna_sqlite";
//    public static final int CURRENT_VERSION = 2;     // v5
//    public static final int CURRENT_VERSION = 3;     // v6
//    public static final int CURRENT_VERSION = 4;     // v9
//    public static final int CURRENT_VERSION = 5;     // v15
//    public static final int CURRENT_VERSION = 6;     // v18
    public static final int CURRENT_VERSION = 7;       // v20

    private static MikhunaSQLiteOpenHelper singletonInstance;

    private Integer connections;

    private Context context;
    
    private MikhunaSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
        this.context = context;
        connections=0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        createDb(db);
        onUpgrade(db, 0, CURRENT_VERSION);
    }

    private void createDb(SQLiteDatabase db) {
        Log.i(TAG, "Creating database...");
        for (String sql : CurrentSchema.DEFINITION) {
            db.execSQL(sql);
        }
        Log.i(TAG, "Database created successfully");
        //
        Log.i(TAG, "Loading current data script...");
        executeSqlDbUp(db, "current");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int startIndex = oldVersion;
        int endIndex = newVersion;
        Log.i(TAG, "Upgrading database from version "+oldVersion+" to "+newVersion);
        for (int i = startIndex; i < endIndex; i++) {
            Log.i(TAG, "Executing upgrade database script "+i);
            for (String sql : SchemaChanges.UPGRADES[i]) {
                db.execSQL(sql);
            }
            // Ejecuta los sql fijados para esta versión
            executeSqlDbUp(db, String.valueOf(i + 1));
        }
    }

    private void executeSqlDbUp(SQLiteDatabase db, String file) {
        try {
            String sqlFile = IOUtil.getAssetTextFileContent(context, "dbup/mikhuna/"+file+".sql");
            if (sqlFile.trim().length() > 0) {
                String[] statements = sqlFile.split(";");
                for (String sql : statements) {
                    if (sql.trim().length()>0) {
                        Log.i(TAG, "Executing sql statement: "+sql);
                        db.execSQL(sql);
                    }
                }
            }
        } catch (SQLException e) {
            ExceptionUtil.ignoreException(e);
        } catch (IOException e) {
            ExceptionUtil.ignoreException(e);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int startIndex = oldVersion - 1;
        int endIndex = newVersion;
        for (int i = startIndex; i >= endIndex; i--) {
            Log.i(TAG, "Executing downgrade database script "+i);
            for (String sql : SchemaChanges.DOWNGRADES[i]) {
                db.execSQL(sql);
            }
        }
    }

    /**
     * Configura la instancia singleton a utilizar en la aplicación
     * @param context
     */
    public static void setup(Context context) {
        MikhunaSQLiteOpenHelper.singletonInstance = new MikhunaSQLiteOpenHelper(context);
        try {
            singletonInstance.getWritableDatabase();
        } catch (SQLiteException e) {
            ExceptionUtil.handleExceptionWithMessage(e, "Creando la base de datos", "Primer intento");
            try {
                singletonInstance.getWritableDatabase();
            } catch (SQLiteException e2) {
                ExceptionUtil.handleExceptionWithMessage(e2, "Creando la base de datos", "Segundo intento");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setMessage(R.string.error_database_creation)
                        .setPositiveButton(R.string.msg_ok, null)
                ;
                builder.create().show();
            }
        }
    }

    /**
     * Devuelve una instancia singleton previamente configurada
     * @return
     */
    public static MikhunaSQLiteOpenHelper getInstance() {
        return MikhunaSQLiteOpenHelper.singletonInstance;
    }

    public void safeClose(SQLiteDatabase db) {
    }
}
