package com.example.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //первый варинат таймера, ереопределяем методы, передаем промежуток времени работы  и интервал частоты
        CountDownTimer myTimer = new CountDownTimer(10000, 1000) {
            //выполняется метод второго параметра таймера, каждую секунду
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("myTimer","Осталось секунд - " +  String.valueOf(millisUntilFinished/1000));

            }
            //выполняется метод когда время первого параметра закончилось
            @Override
            public void onFinish() {
                Log.d("myTimer","Всё!");

            }
        };
        //запускаем таймер
        myTimer.start();







        //второй варинат таймера
//        //создаём таймер при помощи класа Хендлера
//        Handler handler = new Handler();
//
//        //даём возможность запкустить обьект
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                //смотрим в лог
//                Log.d("Runnable: ", "Две секунды прошли");
//                //делаем задержку 2 секунды
//                handler.postDelayed(this, 2000);
//            }
//        };
//        //запускаем процесс
//        handler.post(runnable);

    }
}