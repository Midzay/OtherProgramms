package com.example.maksimov.missterh;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;

import android.text.TextWatcher;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;



public class MainActivity extends AppCompatActivity  {
    private static String TAG="myLogs";
     SharedPreferences sPref;
    String formattedDateMG,formattedDateHMS, sbp_text,dbp_text,hr_text,conn_i;
    final String SAVED_ID = "saved_id";
     final String SAVED_tel = "saved_tel_dov";
    final String SAVED_vsezagr = "vsezagr";
     SQLiteDatabase db;
    final String SAVED_idautter = "автомт терап";
    private static   Calendar calendar;

    final String SAVED_cal="saved_cal";
    final String SAVED_cal_time="saved_cal_time";


    final String SAVED_date = "saved_date";
    int vovremy, samoch;
    TextView tv3;
    EditText sbp_edt,dbp_edt,hr_edt;
    DBHelper dbHelper;
    String id_patienta_iz_bazi,tel_dov_l,service_zagr;
    Button button3;
     ImageView imv7,imv5,imv4,imv3,imv2,imv59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv3 = (TextView) findViewById(R.id.textView3);
        sbp_edt = (EditText) findViewById(R.id.sbp_editText);
        dbp_edt = (EditText) findViewById(R.id.dbp_editText);
        hr_edt = (EditText) findViewById(R.id.hr_editText);
        button3 = (Button) findViewById(R.id.send_Data);
        button3.setEnabled(false);
        imv2=(ImageView)findViewById(R.id.imageView2);
        imv3=(ImageView)findViewById(R.id.imageView3);
        imv4=(ImageView)findViewById(R.id.imageView4);
        imv5=(ImageView)findViewById(R.id.imageView5);
        imv7=(ImageView)findViewById(R.id.imageView7);

        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");



        if (id_patienta_iz_bazi == "") {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }

//создаем базу данных
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        //Календарь запуска сценария

      //  calendar = Calendar.getInstance();


//  Проверка интернет подключения
        isOnline(MainActivity.this);
      /*
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        intent1 = new Intent(this, MyService.class);
        pintent = PendingIntent.getService(this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        alarm2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

*/
        EditText[] edList = {sbp_edt, dbp_edt, hr_edt};
        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, button3);
        for (EditText editText : edList) editText.addTextChangedListener(textWatcher);

// конец onCreate
    }


public class CustomTextWatcher implements TextWatcher {

    View v;
    EditText[] edList;

    public CustomTextWatcher(EditText[] edList, Button v) {
        this.v = v;
        this.edList = edList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        for (EditText editText : edList) {
            if (editText.getText().toString().trim().length() <= 0) {
                v.setEnabled(false);
                break;
            }
            else v.setEnabled(true);
        }
    }
}

    //Самочуствие
    public void Samochu(View v){

        Intent intent = new Intent(this, SamochActivity.class);
        startActivity(intent);
    }

    public void CloseMain(View v){
this.finish();
    }

//  процедура выполняется когда  возвращаемся на экран из настроек
    protected void onResume() {
        super.onResume();
        /*
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        tel_dov_l=sPref.getString(SAVED_tel, "");
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
        service_zagr=sPref.getString(SAVED_vsezagr,"");

        Log.e(TAG, "id_patienta_iz_bazi резуме " + id_patienta_iz_bazi);
        Log.e(TAG, "SAVED_vsezagr SAVED_vsezagr= " + service_zagr);
         imv2.setImageResource(R.mipmap.vergoodn);
        imv3.setImageResource(R.mipmap.goodn);
        imv4.setImageResource(R.mipmap.normaln);
        imv5.setImageResource(R.mipmap.nobadn);
        imv7.setImageResource(R.mipmap.badn);

*/

      }
    protected void onStart() {
        super.onStart();
        /*
        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        tel_dov_l=sPref.getString(SAVED_tel, "");
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
        Log.e(TAG, "id_patienta_iz_bazi  старт " + id_patienta_iz_bazi);
        */
        }


    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, com.example.maksimov.missterh.MyService.class));
        stopService(new Intent(this, AlarmService.class));
        Log.e(TAG, "Остановлено");

    }

    protected void onDestroy() {
        super.onDestroy();
        /*
        stopService(new Intent(this, com.example.maksimov.missterh.MyService.class));
        stopService(new Intent(this, AlarmService.class));
        Log.e(TAG, "Уничтожено");
*/
    }
// отправка данных на сервер
    public void sendData(View v) throws IOException {
        switch (v.getId()) {
            case R.id.send_Data:
                if (id_patienta_iz_bazi!=""){

                //получаем дату и время для записи в базу данных
                Date today = new Date();
                SimpleDateFormat dmg = new SimpleDateFormat("yyyy-MM-dd");
                formattedDateMG=dmg.format(today);
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
                formattedDateHMS=hms.format(today);
                sbp_text=sbp_edt.getText().toString();
                dbp_text=dbp_edt.getText().toString();
                hr_text=hr_edt.getText().toString();

// данные для отправки в локальную базу данных
                    /*
                ContentValues cv = new ContentValues();
                cv.put("data_patient_iddata_patient",id_patienta_iz_bazi);
                cv.put("date", formattedDateMG);
                cv.put("time", formattedDateHMS);
                cv.put("sbp", sbp_text);
                cv.put("dbp", dbp_text);
                cv.put("heart_rate", hr_text);
                cv.put("connection",conn_i);

                db.insert("monitoring_bp", null, cv);
*/


                    try {

                        SharedPreferences sPref1 = getSharedPreferences(SAVED_cal, Context.MODE_PRIVATE);
                        String cal_longfile = sPref1.getString(SAVED_cal_time, "");
                        Long loncal = Long.parseLong(cal_longfile);

                        Log.e(TAG, "my.someTask(1) " + loncal);
                        if (System.currentTimeMillis()>loncal +10800000){
                            vovremy=2;
                            Log.e(TAG, "my.someTask() истина " + vovremy );
                            //  статус 2 в  измерено в не назначенное время



                        }else {
                            // измерено в назначеное время
                            vovremy=1;
                            Log.e(TAG, "my.someTask() лож " + vovremy);
                        }
                    }catch (Exception e){}

                //!!!
               startService(new Intent(this, AlarmService.class));
               // stopService(new Intent(this, AlarmService.class));
                 //   stopService(new Intent(this, com.example.maksimov.missterh.MyService.class));

                new addData().execute();

                // обнуляем поля для ввода
                sbp_edt.setText("");
                dbp_edt.setText("");
                hr_edt.setText("");

                    imv2.setImageResource(R.mipmap.vergood);
                    imv3.setImageResource(R.mipmap.good);
                    imv4.setImageResource(R.mipmap.normal);
                    imv5.setImageResource(R.mipmap.nobad);
                    imv7.setImageResource(R.mipmap.bad);
                    samoch=1;
             //   alarm1.cancel(pintent);

try{
                if ((Integer.parseInt(sbp_text))> 200)
                    createTwoButtonsAlertDialog("Давление очень высокое!!!","Позвонить доверенному лицу?");}

catch (Exception e){}

                break;

       }else {Toast.makeText(getApplicationContext(),"Необходимо авторизоваться в настройках", Toast.LENGTH_LONG).show();

                }
        }

    }
// конец sendData


// класс записи данных на удаленный сервер
    class addData extends AsyncTask<Void, Void, Void> {

        String resultString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
               String id_aut_ter = sPref.getString(SAVED_idautter, "");
                // адрес на сервере где лежит сервис обрабатывающий данные. работает по методу POST
                String myURL = "http://a0159198.xsph.ru/service/add_monitoring_bp.php?";
                String parammetrs;
                byte[] data = null;
                InputStream is = null;

                try {
// загружаем из данных на устройстве id нашего пациента. которое мы получили на экране настроек

                    parammetrs = "id_p=" + id_patienta_iz_bazi + "&id_aut_ter="+id_aut_ter + "&date_p=" + formattedDateMG+"&time_p="+formattedDateHMS+"&sbp_p="+sbp_text+"&dbp_p="
                            +dbp_text+"&heart_p="+hr_text+"&status="+vovremy;

                    // в параметрах сохранен  полный список всех параметров с датой и временем

                    // отправка запроса на сервер с параметрами
                    URL url = new URL(myURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
                    OutputStream os = conn.getOutputStream();
                    data = parammetrs.getBytes("UTF-8");
                    os.write(data);
                    data = null;
                    conn.connect();
                    Log.e(TAG, "URL" + url);
                    int responseCode= conn.getResponseCode();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    if (responseCode == 200) {
                        is = conn.getInputStream();

                        byte[] buffer = new byte[8192]; //  размер буфера

                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, bytesRead);
                        }
                        data = baos.toByteArray();
                        resultString = new String(data, "UTF-8");
                    } else {
                    }
                    // конец  отправки запроса на сервер
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(resultString != null) {
                 Toast toast = Toast.makeText(getApplicationContext(), "Данные успешно отправлены", Toast.LENGTH_SHORT);
                   toast.show();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Отправте данные о самочувствии", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }




//создаем меню в верхнем правом углу
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



// вызов окна при большом давлении

    private void createTwoButtonsAlertDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton("НЕТ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        //  showMessage("Нажали Нет");
                    }
                });
        builder.setPositiveButton("ДА",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        Intent  intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ tel_dov_l));
                        startActivity(intent);
                    }
                });
        builder.show();
    }


    public  boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            Toast.makeText(getApplicationContext(),"Есть подключение к интернет", Toast.LENGTH_LONG).show();
            return true;
        } else { Toast.makeText(getApplicationContext(),"Нет подключения к интернет", Toast.LENGTH_LONG).show();
            return false;
        }
    }




// события при нажатии на меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
               if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.notification) {

            Intent intent = new Intent(this, Notice.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.close) {
            this.finish();
            return true;
                    }
        return super.onOptionsItemSelected(item);
    }



// вызов диалогового окна с барабаном
    public void show(View v)
    {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Введите данные");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(300);
        np.setMinValue(80);
        np.setValue(120);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sbp_edt.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }
    public void show1(View v)
    {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Введите данные");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(220);
        np.setMinValue(80);
        np.setValue(80);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dbp_edt.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }
    public void show2(View v)
    {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Введите данные");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(220);
        np.setMinValue(20);
        np.setValue(60);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                hr_edt.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

    public void Samoch(View v)
    {
        if (samoch==1){


       Toast.makeText(getApplicationContext(),"Данные о самочувствии отправлены ", Toast.LENGTH_LONG).show();
    }

       samoch=0;
        imv2.setImageResource(R.mipmap.vergoodn);
        imv3.setImageResource(R.mipmap.goodn);
        imv4.setImageResource(R.mipmap.normaln);
        imv5.setImageResource(R.mipmap.nobadn);
        imv7.setImageResource(R.mipmap.badn);
    }



}
