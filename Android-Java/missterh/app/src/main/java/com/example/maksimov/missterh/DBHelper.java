package com.example.maksimov.missterh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    Context mContext;
    private String TAG = "myLogs";
    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 2);
      // mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table monitoring_bp ("
                + "id integer primary key autoincrement,"
                + "data_patient_iddata_patient integer ,"
                + "date date,"
                + "time time,"
                + "sbp integer,"
                + "dbp integer,"
                + "connection text,"
                + "heart_rate integer" + ");");
// таблица для записи времени автоматического измерения давления
        db.execSQL("create table automatic_t ("
                + "id integer primary key autoincrement,"
                + "data_patient_iddata_patient integer ,"
                + "idautomatic_therapy integer,"
                + "time_taking time" + ");");

        db.execSQL("create table medicament_t ("
                + "id integer primary key autoincrement,"
                + "medicament_name text,"
                + "single_dose_in_mg text,"
                + "mode_of_application text,"
                + "name_form text,"
                + "data_patient_iddata_patient integer ,"
                + "time_taking time" + ");");

        // таблица для записи времени автоматической терапии

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

