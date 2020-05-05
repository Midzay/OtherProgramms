package com.example.maksimov.missterh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Options extends Activity  {

    Button btnAut, btnSinhr, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // получаем объекты на кнопки
        btnAut = (Button) findViewById(R.id.autoriz);

        btnSinhr = (Button) findViewById(R.id.sinhroniz);

        btnClose = (Button) findViewById(R.id.close_opt);



    }


    //обработчик нажатий на кнопки
// нажатие на конопку авторизация
    public void Autoriz(View v) {

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }
    // нажатие на конопку Синхронизация
    public void Sinhroniz(View v) {

        Intent intent = new Intent(this, Sinhroniz.class);
        startActivity(intent);

    }
    // нажатие на конопку Закрыть
    public void Closes(View v) {

        this.finish();

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

            return true;
        }
        if (id == R.id.close) {
            this.finish();
            return true;
        }


        return true;
    }

}
