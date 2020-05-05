package com.example.maksimov.missterh;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;



// Активити настроек. здесь вводим номер телефонаи ииям доверенного лица. По этим данным  получаем ИД пациента из удаленной базы данных и записываем в файл.


public class Main2Activity extends AppCompatActivity {
    SharedPreferences sPref,sPref1;
    private String TAG = "myLogs";
    final String SAVED_all_ident = "saved_all_ident";
    final String SAVED_ID = "saved_id";
    final String SAVED_date = "saved_date";
    final String SAVED_tel = "saved_tel_dov";
    final String SAVED_tel_moy = "saved_tel_moy";
    final String SAVED_dovlico = "saved_dovlico";
    String tel_text,dov_lico_text,sav_chck, str_true="true";
    EditText tel,tel_dov_l,dov_lico;
    private static   Calendar calendar;
    private Intent intent1;
    private  PendingIntent pintent;
    private  AlarmManager alarm2;
    String p_id;
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tel = (EditText) findViewById(R.id.editText_tel);
        tel_dov_l=(EditText)findViewById(R.id.tel_dov);
        dov_lico = (EditText) findViewById(R.id.editText_dov);
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        sPref1= getSharedPreferences(SAVED_all_ident, Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();


        tel.setText(sPref1.getString(SAVED_tel_moy, ""));
        tel_dov_l.setText(sPref1.getString(SAVED_tel, ""));
        dov_lico.setText(sPref1.getString(SAVED_dovlico, ""));


    }

   // событие срабатывает при нажатии на кнопку зарегистирироваться
    public void onZareg(View v) {
        switch (v.getId()) {
            case R.id.button_id:


                // преобразуем данные в текст и присваеваем переменной
                tel_text = tel.getText().toString();
               dov_lico_text=dov_lico.getText().toString();
                // записываем в файл телефон доверенного лица для того чтобы экстренно звонить в случае необходимости
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_tel,tel_dov_l.getText().toString() );
                ed.apply();
//записываем регистрационную информацию
                SharedPreferences.Editor ed1 = sPref1.edit();
                // телефон
                ed1.putString(SAVED_tel_moy,tel.getText().toString() );
                //доверенное лицо
                ed1.putString(SAVED_tel,tel_dov_l.getText().toString() );
                // телефон доверенного лица
                ed1.putString(SAVED_dovlico,dov_lico.getText().toString() );
                ed1.apply();


// получаем контакты с сервера
            AsyncTask a=  new GetContacts().execute();
                try {
                    Thread.sleep(1000); //Приостанавливает поток на 1 секунду
                } catch (Exception e) {

                }
                a.isCancelled();
                Toast.makeText(getApplicationContext(),
                        "Данные получены",Toast.LENGTH_LONG).show();

               startService(new Intent(this, MyService.class).putExtra("id_poc",p_id));


                calendar.set(Calendar.HOUR_OF_DAY, 4);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                intent1 = new Intent(this, MyService.class);
                pintent = PendingIntent.getService(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                alarm2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

                break;
            default:
                break;
        }

    }

    public void Closes(View v) {

              this.finish();
        Intent intent = new Intent(this, Notice.class);
        startActivity(intent);

    }


// Класс который непосредственно подключается к базе данных и передает данные

    class GetContacts extends  AsyncTask<Void, Void, Void>  {


        private static final String url_get_id = "http://a0159198.xsph.ru/service/get_id_patient.php";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // создаем и отправляем запрос

            String url =  url_get_id+"?tel="+tel_text+"&dov_lico="+dov_lico_text;
            Log.e(TAG, "URL= " + url);//  выодим в логах урл
            String jsonStr = sh.makeServiceCall(url);// используем меттоды из класса HttpHandler

            // проверяем  результаты
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Получаем массив из JSON
                    JSONArray pid = jsonObj.getJSONArray("p_id");

                    // Получаем из массива необходимые данные. В данном слувае нужный нам id пациента
                    for (int i = 0; i < pid.length(); i++) {
                        JSONObject c = pid.getJSONObject(i);
                        p_id = c.getString("p_id");

                        Log.e(TAG, "Получаем id " + p_id);



                    }

                } catch (final JSONException e) { // проверяем исключительные события . Получаем в логах сообщения в случае ошибок
                    Log.e(TAG, "Ошибки при обработке Json1: " + e.getMessage());
                    runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Нет доступа к серверу или вы не зарегистрированы в системе: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Не можем получить Json  с сервера.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Нет доступа к серверу или вы не зарегистрированы в системе",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
// закончили выполнение основного запроса
        @Override
            protected void onPostExecute(Void result) {// так как мы получили id теперь его необходимо сохранить на устройстве, чтобы больше не пришлось воодить данные
            super.onPostExecute(result);
            // создаем объекты для кранения

            SharedPreferences.Editor ed = sPref.edit();
            //сохраняем в системе
            ed.putString(SAVED_date,p_id);
            Log.e(TAG, "id пациента  записали в саве дате:  "+p_id);
            ed.apply();
        }


    }




}
