package com.example.maksimov.missterh;

import android.util.Log;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by domolink on 17.10.2017.
 */
public class AlarmServiceTest {
    private static final String TAG = "123";


    @Test
    public void setCalendar( ) {

     String  time_is_bazy="01-:45:12";

            String[] parts = time_is_bazy.split(":");
            String h_d = parts[0];
            String min = parts[1];
            assertEquals("ошибка преобразования","01",h_d);
            assertEquals("ошибка преобразования","45",min);


    }
}