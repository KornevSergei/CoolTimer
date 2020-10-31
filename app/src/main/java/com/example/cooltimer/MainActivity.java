package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private SeekBar seekBar;
    private TextView textView;
    //переменная для смены названия кнопки
    private Button button;
    //переменная для определения работает ли сикбар
    private boolean isTimerOn;

    private CountDownTimer countDownTimer;

    //переменная для установки интервала
    private int defaultInterval;

    //переменная для частого обращения к полю
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        //первый варинат таймера, ереопределяем методы, передаем промежуток времени работы  и интервал частоты
//        CountDownTimer myTimer = new CountDownTimer(10000, 1000) {
//            //выполняется метод второго параметра таймера, каждую секунду
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Log.d("myTimer","Осталось секунд - " +  String.valueOf(millisUntilFinished/1000));
//
//            }
//            //выполняется метод когда время первого параметра закончилось
//            @Override
//            public void onFinish() {
//                Log.d("myTimer","Всё!");
//
//            }
//        };
//        //запускаем таймер
//        myTimer.start();
//
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


        //связываем с разметкой
        //связываем с разметкой
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //устанавливаем максимум
        seekBar.setMax(600);
        //устанавливаем начальный параметр ызывая метод
        setIntervalFromSharedPreferences(sharedPreferences);

        //по умолчанию ставим что таймер не включен
        isTimerOn = false;

        //устанавливаем слушатель для изменения текста
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                long progressInMillis = progress * 1000;
                updateTimer(progressInMillis);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //регистрируем
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


    }

    public void start(View view) {
        //елаем проверку на то раоботает ли таймер сли работает то останавливаем
        if (!isTimerOn) {
            button.setText("Стоп");
            //убираем возможность трогать сикбар
            seekBar.setEnabled(false);
            isTimerOn = true;

            //создаём таймер устанавливаем начало которе привязано к сикбару и интервал срабатывания
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //вызываем метод и передаём параметр
                    updateTimer(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    //получем доступ и узнаем включен или выключен звук
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    //проверяем какой результат находится в файле, если  включен то запускаем звук
                    if (sharedPreferences.getBoolean("enable_sound", true)) {

                        //обращаемся к массиву мелодий делаем проверку на выбранный звук
                        String melodyName = sharedPreferences.getString("timer_melody", "bell");
                        if (melodyName.equals("bell")) {
                            //создаём плеер для звонка по окончанию
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell);
                            mediaPlayer.start();
                        } else if (melodyName.equals("laugh")) {

                            Log.d("onFinish: ", "Всё!");
                            //создаём плеер для звонка по окончанию
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.laugh);
                            mediaPlayer.start();
                        }

                    }
                    resetTimer();
                }
            };
            countDownTimer.start();

        } else {
            resetTimer();

        }

    }


    private void updateTimer(long millisUntilFinished) {
        //обнавляем каждую секунду
        int minutes = (int) (millisUntilFinished / 1000 / 60);
        int seconds = (int) (millisUntilFinished / 1000 - (minutes * 60));

        //строки  проверка для правильного отображения цифр в тексвью
        String minutesString = "";
        String secondsString = "";

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = String.valueOf(minutes);
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = String.valueOf(seconds);
        }

        //устанавливаем значения
        textView.setText(minutesString + " : " + secondsString);
    }

    private void resetTimer() {
        //останавливаем таймер и меняем текст
        countDownTimer.cancel();
        button.setText("Старт");
        seekBar.setEnabled(true);
        isTimerOn = false;
        //устанавливаем начальный параметр ызывая метод
        setIntervalFromSharedPreferences(sharedPreferences);
    }


    //переопределяем метод для создания меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //получаем значение из метода гетменюинфлейтор
        MenuInflater menuInflater = getMenuInflater();
        //передаём данные из файла
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;

    }

    //делаем действие по клику на настроики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //пределяем по какому элементу клик
        int id = item.getItemId();
        //проверяем и даём действие открыть новое активити
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;

            //тоже самое по клику на другой элемент
        } else if (id == R.id.action_about) {
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //устанавливаем интервал из преференс
    private void setIntervalFromSharedPreferences(SharedPreferences sharedPreferences) {

//        //не закрываем с ошибкой если ввели строковое значение вместо инт в настройках
//        try {
//
//        } catch (NumberFormatException nef){
//            Toast.makeText(this, "Произошла ошибка первая", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e){
//            Toast.makeText(this, "Произошла ошибка общая", Toast.LENGTH_SHORT).show();
//
//        }
        //присваиваем интервал
        defaultInterval = Integer.valueOf(sharedPreferences.getString("default_interval", "30"));
        long defaultIntervalInMillis = defaultInterval * 1000;
        updateTimer(defaultIntervalInMillis);
        //станавливаем новые данные для сикбара
        seekBar.setProgress(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("default_interval")){
            setIntervalFromSharedPreferences(sharedPreferences);
        }
    }

    //снимаем с регистрации

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}