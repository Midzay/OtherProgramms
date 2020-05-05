package com.example.maksimov.missterh;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;



public class MyService extends  Service {


    private static String TAG="myLogs";
    SharedPreferences sPref;
    final String SAVED_ID = "saved_id";

    SQLiteDatabase db;
    final String SAVED_date = "saved_date";
    DBHelper dbHelper;
    String id_patienta_iz_bazi,name_m;
    final String LOG_TAG = "myLogs";


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate сервис");



//создаем базу данных
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");

        new Getautomatic_t().execute();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Getamedicament_t().execute();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startService(new Intent(this, AlarmService.class));

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand Myservice1");
        //sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        //id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
        Log.d(LOG_TAG, "id_patienta_iz_bazi my service====="+id_patienta_iz_bazi);


   //     name_m = intent.getStringExtra("id_poc");

        Log.d(LOG_TAG, "name_m из настроек в май сервис====="+name_m);

        if (id_patienta_iz_bazi==""){
            Log.d(LOG_TAG, "id_patienta_iz_bazi присвоили нname_m ="+name_m);
            id_patienta_iz_bazi=name_m;}
        else Log.d(LOG_TAG, "id_patienta_iz_bazi взят из SPREF ="+ id_patienta_iz_bazi);






       // return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }




    private  class Getautomatic_t extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                if (id_patienta_iz_bazi!=""){
                    db.delete("automatic_t", "data_patient_iddata_patient=" + id_patienta_iz_bazi, null);
                    }
                Log.e(TAG, "Очищаем базу данных");
                Log.e(TAG, "id_patienta_iz_bazi в гет автоматик т  "+id_patienta_iz_bazi);
            }
            catch (ArithmeticException e) {
                Toast.makeText(getApplicationContext(),
                        "Пытаемся очистить несуществующую базу",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Создаем урл для отображения данных

            String url = "http://a0159198.xsph.ru/service/get_automatic_t.php";

            String newUrl=url+"?p_id="+id_patienta_iz_bazi;
            Log.e(TAG, "url   " + newUrl);//  выодим в логах урл
            // обращаемся к httpHandler
            String jsonStr = sh.makeServiceCall(newUrl);
// выводим в логах адресс
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Получаем массив данных пациента для последующей обработки
                    JSONArray datesPac = jsonObj.getJSONArray("automatic_t");

                    // Обрабатываем массив и получаем данные
                    for (int i = 0; i < datesPac.length(); i++) {
                        JSONObject c = datesPac.getJSONObject(i);
                        String data_patient_iddata_patient = c.getString("data_patient_iddata_patient");
                        String idautomatic_therapy = c.getString("idautomatic_therapy");
                        String time_taking = c.getString("time_taking");

// вставляем данные с сервера в  локальную базу данных
                        ContentValues cv = new ContentValues();
                        cv.put("data_patient_iddata_patient", data_patient_iddata_patient);
                        cv.put("idautomatic_therapy", idautomatic_therapy);
                        cv.put("time_taking", time_taking);
                        db.insert("automatic_t", null, cv);
                    }

                } catch (final JSONException e) {


                }

            } else {
                Log.e(TAG, "Не можем получить данные с сервера");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    // конец запроса информации



    private class Getamedicament_t extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                if (id_patienta_iz_bazi!=""){

                    db.delete("medicament_t", null, null);}
                Log.e(TAG, "Очищаем базу данных");
                Log.e(TAG, "id_patienta_iz_bazi медики= "+id_patienta_iz_bazi);
            }
            catch (ArithmeticException e) {
                Toast.makeText(getApplicationContext(),
                        "Пытаемся очистить несуществующую базу",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Создаем урл для отображения данных

            String url = "http://a0159198.xsph.ru/service/get_medicament_t.php";

            String newUrl=url+"?p_id="+id_patienta_iz_bazi;
            Log.e(TAG, "url" + newUrl);//  выодим в логах урл
            // обращаемся к httpHandler
            String jsonStr = sh.makeServiceCall(newUrl);
// выводим в логах адресс
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Получаем массив данных пациента для последующей обработки
                    JSONArray datesPac = jsonObj.getJSONArray("medicament_t");

                    // Обрабатываем массив и получаем данные
                    for (int i = 0; i < datesPac.length(); i++) {
                        JSONObject c = datesPac.getJSONObject(i);
                        String medicament_name = c.getString("medicament_name");
                        String single_dose_in_mg = c.getString("single_dose_in_mg");
                        String mode_of_application = c.getString("mode_of_application");
                        String name_form = c.getString("name_form");
                        String time_taking = c.getString("time_taking");

// вставляем данные с сервера в  локальную базу данных
                        ContentValues cv = new ContentValues();
                        cv.put("medicament_name", medicament_name);
                        cv.put("single_dose_in_mg", single_dose_in_mg);
                        cv.put("mode_of_application", mode_of_application);
                        cv.put("name_form", name_form);
                        cv.put("time_taking", time_taking);
                        db.insert("medicament_t", null, cv);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, " Ошибка обработки Json : " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Не можем получить данные с сервера");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }



    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy МАЙ service");
        stopService(new Intent(this, AlarmService.class));
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }


}
