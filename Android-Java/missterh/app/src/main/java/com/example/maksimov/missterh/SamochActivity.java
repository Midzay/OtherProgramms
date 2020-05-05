package com.example.maksimov.missterh;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;


public class SamochActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    SharedPreferences sPref;
    final String SAVED_ID = "saved_id";
    final String SAVED_tel = "saved_tel_dov";
    String tel_dov_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samoch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sPref = getSharedPreferences(SAVED_ID, Context.MODE_PRIVATE);
        tel_dov_l=sPref.getString(SAVED_tel, "");
               final Button button6 =(Button)findViewById(R.id.button6);
        button6.setVisibility(View.GONE);

       ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if (ratingBar.getRating()>=0.5 && ratingBar.getRating()<3) {

                    Toast.makeText(SamochActivity.this,
                            "Данные отправлены",
                            Toast.LENGTH_SHORT).show();

                    Toast.makeText(SamochActivity.this,
                            "У вас плохое самочувствие. Позвоните доверенному лицу",
                            Toast.LENGTH_SHORT).show();
                    button6.setVisibility(View.VISIBLE);
                }

                if (ratingBar.getRating()==3) {

                    Toast.makeText(SamochActivity.this,
                            "Данные отправлены",
                            Toast.LENGTH_SHORT).show();

                }
                if (ratingBar.getRating()>3 ) {

                    Toast.makeText(SamochActivity.this,
                            "Данные отправлены",
                            Toast.LENGTH_SHORT).show();


                }


            }
        });

    }



    public void tclose (View v){

       this.finish();
    }

    public void calldovlico(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SamochActivity.this);
        builder.setTitle("У вас очень плохое самочувствие");
        builder.setMessage("Позвонить доверенному лицу?");
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

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ tel_dov_l));
                        startActivity(intent);
                    }
                });
        builder.show();
    }


}