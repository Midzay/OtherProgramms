package com.example.maksimov.missterh;



import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetContactsTest {
    @Test
    public void onPreExecute() throws Exception {

    }

    @Test
    public void doInBackground() throws Exception {
        HttpHandler sh = new HttpHandler();

        // создаем и отправляем запрос

        String url = "http://a0159198.xsph.ru/service/get_id_patient.php";

        String jsonStr = sh.makeServiceCall(url);// используем меттоды из класса HttpHandler

        Assert.assertTrue("Ошибка подключения к серверу",jsonStr!=null);

    }

    @Test
    public void onPostExecute() throws Exception {


    }

}