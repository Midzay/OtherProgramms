package com.example.maksimov.missterh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;



public class AlarmService extends Service {
    int y = 0;
    private static String TAG="myLogs";
    SharedPreferences sPref;
    final String SAVED_cal="saved_cal";
    final String SAVED_cal_time="saved_cal_time";
    final String SAVED_ID = "saved_id";
    final String SAVED_tel = "saved_tel_dov";
    SQLiteDatabase db;
    String[] timetaking_izbazy=new String[3];
    String [] name_medicament,time_medikament;
     Calendar calendar,calendar_med;
     long callong;
    private Intent intent1,intent2;
    private PendingIntent pintent;
    private AlarmManager alarm1;
    PendingIntent[] pintent2;
    AlarmManager [] alarm2;
    final String SAVED_date = "saved_date";
    final String SAVED_idautter = "автомт терап";
    DBHelper dbHelper;
    String id_patienta_iz_bazi,tel_dov_l;
    final String LOG_TAG = "myLogs";
    String id_avtomat_t;


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");


        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        // получаем id пашицента
        id_patienta_iz_bazi = sPref.getString(SAVED_date, "");
        Log.d(TAG, "id_patienta_iz_bazi  которое в аларм сервсие" + id_patienta_iz_bazi);
     //   tel_dov_l = sPref.getString(SAVED_tel, "");
//создаем базу данных
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        //Календарь запуска сценария
        calendar = Calendar.getInstance();
        calendar_med= Calendar.getInstance();

        // делаем запрос всех данных из таблицы automatic_t, получаем Cursor
        Cursor a = db.query("automatic_t", null, null, null, null, null, null);
        int i = 0;
        if (a.moveToFirst()) {

            int date_p_i = a.getColumnIndex("time_taking");
            int date_a_i = a.getColumnIndex("idautomatic_therapy");

            do {
                String time_taking_s = a.getString(date_p_i);
                 id_avtomat_t = a.getString(date_a_i);

                timetaking_izbazy[i] = time_taking_s;
                Log.d(TAG, "timetaking_izbazy[i]= " + timetaking_izbazy[i]);
                i++;
            } while (a.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        a.close();

        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sPref.edit();
        ed1.putString(SAVED_idautter,id_avtomat_t );
        ed1.apply();

// Получаем время приема лекарств
        Cursor b = db.query("medicament_t", null, null, null, null, null, null);

        if (b.moveToFirst()) {
            ////sdfjsdl;fjkasl;dkfjasl;dkfjasl;dkjf
            name_medicament=new String[b.getCount()];
            time_medikament=new String[b.getCount()];
            pintent2= new PendingIntent[b.getCount()];
            alarm2=new AlarmManager[b.getCount()];
           // intent2=new Intent[b.getCount()];

            int date_p_l = b.getColumnIndex("time_taking");
            int name_p_l = b.getColumnIndex("medicament_name");
y=0;
            do {
                String time_taking_s = b.getString(date_p_l);
                String name_lec = b.getString(name_p_l);

                time_medikament[y] = time_taking_s;
                name_medicament[y]=name_lec;

                setAlarmMedic();

               // выводим наименование лекарства

                Log.d(TAG, "name_medicament[i]= " + name_medicament[y]);
                Log.d(TAG, "time_medikament[i]= " + time_medikament[y]);

                y++;
            } while (b.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        b.close();


// будильник

        if (id_patienta_iz_bazi != "") {
            setAlarm();
            callong = calendar.getTimeInMillis();
            //сохраняем в системе
            sPref = getSharedPreferences(SAVED_cal, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_cal_time, String.valueOf(callong));
            ed.apply();
        }
    }

    public void SetTimemedicament(String  time_medicament ){

        try {
            String[] parts = time_medicament.split(":");
            String h_d = parts[0];
            String min = parts[1];
            calendar_med.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h_d));
            calendar_med.set(Calendar.MINUTE, Integer.parseInt(min));
            calendar_med.set(Calendar.SECOND, 0);
            calendar_med.set(Calendar.MILLISECOND, 0);
            Log.e(TAG, " Календарь : "+calendar_med.getTimeInMillis() );
        } catch (Exception e) {
            Log.e(TAG, " Ошибка  времени : " );
        }
    }

    public void setAlarmMedic(){

        if (time_medikament[y]!="Не использовать") {
            Log.e(TAG, "Проверка времени="+time_medikament[y]);

            SetTimemedicament(time_medikament[y]);

// Если время для уведомления больше системного времени, тогда устанавливаем будильник
        if (calendar_med.getTimeInMillis() > System.currentTimeMillis()) {
            intent2 = new Intent(this, NotificationMessageMedicament.class);

            intent2.putExtra("name_med",name_medicament[y]);
            intent2.putExtra("time_med",time_medikament[y]);

            pintent2[y] = PendingIntent.getBroadcast(this, y, intent2, 0);
            alarm2[y] = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Log.e(TAG, "Крутой игрэk :   "+y);
            alarm2[y].cancel(pintent2[y]);
            //alarm2[y].setRepeating(AlarmManager.RTC_WAKEUP, calendar_med.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pintent2[y]);
            alarm2[y].set(AlarmManager.RTC_WAKEUP, calendar_med.getTimeInMillis()+(new Double(Math.random()* 10000)).longValue(), pintent2[y]);
            Log.e(TAG, "устанавливаем будильник");


        } else
            Log.e(TAG, "Уже поздно пить лекарства");
        } else
            Log.e(TAG, "Время не использовать");

        // Конец будильника
    }


    public void SetCalendar(String  time_is_bazy ){
        try {
            String[] parts = time_is_bazy.split(":");
            String h_d = parts[0];
            String min = parts[1];
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(h_d));
            calendar.set(Calendar.MINUTE, Integer.parseInt(min));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            Log.e(TAG, " Ошибка  времени : " );


        }
    }

    public void setAlarm(){
        intent1 = new Intent(this, NotificationMessage.class);

        pintent = PendingIntent.getBroadcast(this, 10, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

        alarm1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        if (timetaking_izbazy[0]!="Не использовать") {

            SetCalendar(timetaking_izbazy[0]);
        }
// Если время для уведомления больше системного времени, тогда устанавливаем будильник
        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            Log.e(TAG, "Первый иф: "+calendar);
            alarm1.cancel(pintent);
            alarm1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pintent);

            Log.e(TAG, "Жто из сервиса!!!!!");


        } else
        { if (timetaking_izbazy[1]!="Не использовать") {
            SetCalendar(timetaking_izbazy[1]);


            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                Log.e(TAG, "Второй иф: "+calendar);

                alarm1.cancel(pintent);
                alarm1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pintent);



            } else {
                if (timetaking_izbazy[2]!="Не использовать") {


                    SetCalendar(timetaking_izbazy[2]);



                    if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                        Log.e(TAG, "третий иф: "+calendar);

                        alarm1.cancel(pintent);
                        alarm1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pintent);


                    } else {

                        alarm1.cancel(pintent);
                        Log.e(TAG, "Ни одного будильника: "+calendar);
                        Toast.makeText(getApplicationContext(),
                                "Ни одного уведомления",
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),
                                "Проверьте данные",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        }

        }
        // Конец будильника



    }



    public int onStartCommand(Intent intent, int flags, int startId) {




        Log.d(LOG_TAG, "onStartCommand аларм сервис");


               return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }



}
