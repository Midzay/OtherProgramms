package com.example.maksimov.missterh;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Sinhroniz extends AppCompatActivity {


    private String TAG = "myLogs";
    private ListView lv;

    final String SAVED_ID = "saved_id";
    final String SAVED_date = "saved_date";
    DBHelper dbHelper;
    String id_patienta_iz_bazi;

    SharedPreferences sPref;
    SQLiteDatabase db;
    ArrayList<HashMap<String, String>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinhroniz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

        public void Sinhr_server(View v) {


            new  GetHistory().execute();
            Toast.makeText(this, "Данные с сервера загружены", Toast.LENGTH_LONG).show();

        }
    // Класс получения данных с сервера и записи в локальную базу данных

    private class GetHistory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

       }

        @Override
        protected Void doInBackground(Void... arg0) {

            // очищаем нашу базу данных
            db.delete("monitoring_bp", "data_patient_iddata_patient = " + id_patienta_iz_bazi, null);
            HttpHandler sh = new HttpHandler();
            // Создаем урл для отображения данных
            String url = "http://jjts.ru/bro/get_all_patient.php";

            String newUrl=url+"?p_id="+id_patienta_iz_bazi;
            // обращаемся к httpHandler
            String jsonStr = sh.makeServiceCall(newUrl);
// выводим в логах адресс
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Получаем массив данных пациента для последующей обработки
                    JSONArray datesPac = jsonObj.getJSONArray("date_pacient");

                    // Обрабатываем массив и получаем данные
                    for (int i = 0; i < datesPac.length(); i++) {
                        JSONObject c = datesPac.getJSONObject(i);
                        String date_p = c.getString("date");
                        String time_p = c.getString("time");
                        String sbp_p = c.getString("sbp");
                        String dbp_p = c.getString("dbp");
                        String heart_rate_p = c.getString("heart_rate");

                        ContentValues cv = new ContentValues();
                        cv.put("data_patient_iddata_patient",id_patienta_iz_bazi);
                        cv.put("date", date_p);
                        cv.put("time", time_p);
                        cv.put("sbp", sbp_p);
                        cv.put("dbp", dbp_p);
                        cv.put("heart_rate", heart_rate_p);
                        cv.put("connection","1");

                        db.insert("monitoring_bp", null, cv);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, " Ошибка обработки Json : " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Ошибка обработки Json: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Не можем получит Json  с сервера");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Не можем получит Json  с сервера",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
        //заполняем ListView данными из масиива
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }


    public void delBaza(View v) {

        db.delete("monitoring_bp", "data_patient_iddata_patient = " + id_patienta_iz_bazi, null);

    }

    public void Closes(View v) {

        this.finish();

    }
}
