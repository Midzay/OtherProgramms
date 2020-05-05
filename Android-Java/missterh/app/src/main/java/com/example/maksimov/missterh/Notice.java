package com.example.maksimov.missterh;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class Notice extends AppCompatActivity {
    private String TAG = "myLogs";
    private ListView nt,nm;

    DBHelper dbHelper;
    Intent intent;
    String time_m;
    String selection = null;
    String[] selectionArgs = null;
    Cursor c;


    ArrayList<HashMap<String, String>> dataList_t,dataList_m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);
        dataList_t = new ArrayList<>();
        dataList_m = new ArrayList<>();
        nt = (ListView) findViewById(R.id.notice_t);
        nm = (ListView) findViewById(R.id.notice_m);
        intent = getIntent();
        time_m = intent.getStringExtra("time_med");

        Therapy();

        Medicament();


 }

    public void Therapy(){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("automatic_t", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке

            int time_taking = c.getColumnIndex("time_taking");



            do {
                String time_taking_p=c.getString(time_taking);
                if (!time_taking_p.equals("Не использовать")) {
                    time_taking_p = time_taking_p.substring(0, 5);
                    HashMap<String, String> alldata = new HashMap<>();
                    // вставляем данные
                    alldata.put("time_taking", time_taking_p);
                    //   Log.e(TAG, " time_takingnnnnnnn: " + time_taking_p);

                    // формируем данные для листвью
                    dataList_t.add(alldata);
                }



            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();
        // отображаем данные из масиива в адаптере
        ListAdapter adapter = new SimpleAdapter(Notice.this, dataList_t,
                R.layout.notice_item, new String[]{ "time_taking"},
                new int[]{R.id.time_taking_p })
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView

                View view = super.getView(position, convertView, parent);

                if(position %2 == 1)
                {
                    view.setBackgroundColor(Color.parseColor("#D6F8F6"));

                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#D6F8F6"));
                }
                return view;

              /*  View view = super.getView(position, convertView, parent);

                if(position %2 == 1)
                {
                   view.setBackgroundColor(Color.parseColor("#FFF8DC"));

                }
                else
                {
                   view.setBackgroundColor(Color.parseColor("#FFDEAD"));
                }
                return view;
                */
            }
        };
        nt.setAdapter(adapter);

    }

    public void Medicament(){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        time_m = intent.getStringExtra("time_med");
         if( time_m!=null) {

             selection = "time_taking = ?";
             selectionArgs = new String[]{time_m};
              c = db.query("medicament_t", null, selection, selectionArgs, null, null, "time_taking");
         }else

             c = db.query("medicament_t", null, null, null, null, null, "time_taking");
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int time_taking = c.getColumnIndex("time_taking");
            int medicament_name = c.getColumnIndex("medicament_name");
            int single_dose_in_mg = c.getColumnIndex("single_dose_in_mg");
            int mode_of_application = c.getColumnIndex("mode_of_application");
            int name_form = c.getColumnIndex("name_form");

            do {
                String time_taking_p = c.getString(time_taking);
                Log.d(TAG, "time_taking_p*******"+time_taking_p);
                if (!time_taking_p.equals("Не использовать"))
                {

                 time_taking_p = time_taking_p.substring(0, 5);
                String medicament_name_p = c.getString(medicament_name);
                String single_dose_in_mg_p = c.getString(single_dose_in_mg);
                String mode_of_application_p = c.getString(mode_of_application);
                String name_form_p = c.getString(name_form);

                HashMap<String, String> alldata = new HashMap<>();
                // вставляем данные
                alldata.put("time_taking", time_taking_p);
                alldata.put("medicament_name", medicament_name_p);
                alldata.put("single_dose_in_mg", single_dose_in_mg_p);
                alldata.put("mode_of_application", mode_of_application_p);
                alldata.put("name_form", name_form_p);

                //   Log.e(TAG, " time_takingttttttttt: " + name_form_p);

                // формируем данные для листвью
                dataList_m.add(alldata);

            }


            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();
        // отображаем данные из масиива в адаптере
        ListAdapter adapter = new SimpleAdapter(Notice.this, dataList_m,
                R.layout.notice_item_medicament, new String[]{ "time_taking","medicament_name","single_dose_in_mg","mode_of_application","name_form"},
                new int[]{R.id.time_priema_izbazi,R.id.naimenovanie_izbazi, R.id.doza_izbazi,R.id.sposob_priema_izbazi,R.id.lek_forma_izbazi})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView


                View view = super.getView(position, convertView, parent);



                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#ffffff"));

                }
                else
                {

                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return view;

/*
                //   View view = super.getView(position,convertView,parent);

                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFF8DC"));

                }
                else
                {

                    view.setBackgroundColor(Color.parseColor("#FFDEAD"));
                }
                return view;
*/

            }
        };
       nm.setAdapter(adapter);
    }

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
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            this.finish();
            }
        if (id == R.id.close) {
            this.finish();
            return true;

        }


        return true;
    }

}






