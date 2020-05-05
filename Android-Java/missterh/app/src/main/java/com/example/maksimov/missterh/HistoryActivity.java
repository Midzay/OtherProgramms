package com.example.maksimov.missterh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;



import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;


import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {
    private String TAG = "myLogs";
    private ListView lv;
    final String SAVED_ID = "saved_id";
    final String SAVED_date = "saved_date";
    DBHelper dbHelper;
    TextView dt_p,tt_p;
    String sbp_p;
    SharedPreferences sPref;
    ArrayList<HashMap<String, String>> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dt_p = (TextView) findViewById(R.id.data_p);
        tt_p = (TextView) findViewById(R.id.time_p);




        // отображение данных из локальной базы данных//
String orderby="date desc,time desc";

        // делаем запрос всех данных из таблицы monitoring_bp, получаем Cursor
        Cursor c = db.query("monitoring_bp", null, null, null, null, null, orderby);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
          //  int data_patient_iddata_patient = c.getColumnIndex("data_patient_iddata_patient");
            int date_p_i = c.getColumnIndex("date");
            int time_p_i  = c.getColumnIndex("time");
            int sbp_p_i = c.getColumnIndex("sbp");
            int dbp_p_i = c.getColumnIndex("dbp");
            int heart_rate_p_i = c.getColumnIndex("heart_rate");
            int conn_p_i=c.getColumnIndex("connection");


            do {

                String date_p=c.getString(date_p_i);
                String time_p=c.getString(time_p_i);
                 sbp_p=c.getString(sbp_p_i);
                String dbp_p=c.getString(dbp_p_i);
                String heart_rate_p=c.getString(heart_rate_p_i);

                //идентификатор подключеник к интернету
                String conn_p=c.getString(conn_p_i);
                Log.e(TAG, "conn_p " + conn_p);

                HashMap<String, String> alldata = new HashMap<>();

                // вставляем данные
                alldata.put("date_p", date_p);
                alldata.put("time_p", time_p);
                alldata.put("sbp_p", sbp_p);
                alldata.put("dbp_p", dbp_p);
                alldata.put("heart_rate_p", heart_rate_p);

                // формируем данные для листвью
                dataList.add(alldata);


            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();
        // отображаем данные из масиива в адаптере
        ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, dataList,
                R.layout.list_item, new String[]{ "date_p","time_p","sbp_p","dbp_p","heart_rate_p"},
                new int[]{R.id.data_p, R.id.time_p, R.id.sad_data, R.id.dad_data, R.id.heart_data})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView

               View view = super.getView(position, convertView, parent);

             //   View view = super.getView(position,convertView,parent);

                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFF8DC"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFDEAD"));
                }
                return view;


            }
        };
        lv.setAdapter(adapter);
     //  new  GetHistory().execute();   // получение данных из локальной базы данных
 }


    // Процедура смены бекграунда


// кнопка закрытия окна программы
    public void onClose(View v) {
        switch (v.getId()) {
            case R.id.butClose:
// Закрываем окно
                this.finish();
                break;
            default:
                break;
        }

    }

    // класс получение данных с удаленного сервера
private class GetHistory extends AsyncTask<Void, Void, Void> {
@Override
protected void onPreExecute() {
    super.onPreExecute();


}

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Создаем урл для отображения данных
        String url = "http://jjts.ru/bro/get_all_patient.php";
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        String id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
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


                    HashMap<String, String> alldata = new HashMap<>();

                    // вставляем данные
                    alldata.put("date_p", date_p);
                    alldata.put("time_p", time_p);
                    alldata.put("sbp_p", sbp_p);
                    alldata.put("dbp_p", dbp_p);
                    alldata.put("heart_rate_p", heart_rate_p);

                    // формируем данные для листвью
                    dataList.add(alldata);
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
        ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, dataList,
                R.layout.list_item, new String[]{ "date_p","time_p","sbp_p","dbp_p","heart_rate_p"},
                new int[]{R.id.data_p, R.id.time_p, R.id.sad_data, R.id.dad_data, R.id.heart_data});
        lv.setAdapter(adapter);
    }
}

    //создаем меню в верхнем правом углу
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    // события при нажатии на меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Options.class);
            startActivity(intent);
            this.finish();

            if (id == R.id.close) {
                this.finish();
                return true;
            }
            return true;


        }

        return true;
    }

}






