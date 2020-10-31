package com.example.cooltimer;

//создали класс для элементов настроек

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

//имплементируемся что бы изменения были сразу а не пересоздавая активити
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //добавляем элементы настроек из хмл
        addPreferencesFromResource(R.xml.timer_preferences);


        //делаем возможность видеть в настройках выбраную мелодию
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        //создаём колличество натсроек
        int count = preferenceScreen.getPreferenceCount();

        //проходим циклом по элементам и получаем выбранный
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            //проверяем что бы настройки не косались чекбокс
            if (!(preference instanceof CheckBoxPreference)) {
                //присваиваем и вызываем метод
                String value = sharedPreferences.getString(preference.getKey(), "");
                //вызываем метод  и передаем параметры
                setPreferenceLabel(preference, value);
            }
        }

        //делаем слушатель для конкретной настройки
        Preference preference = findPreference("default_interval");
        preference.setOnPreferenceChangeListener(this);

    }

    //вспомогательный метод ля определения элемента настройки
    private void setPreferenceLabel(Preference preference, String value) {
        //если параметр лист преференс то выполняем
        if (preference instanceof ListPreference) {
            //создаём обьект класса
            ListPreference listPreference = (ListPreference) preference;
            //определяем позицию и передаём строку
            int index = listPreference.findIndexOfValue(value);

            if (index >= 0) {
                //устанавливаем позицию по индексу
                listPreference.setSummary(listPreference.getEntries()[index]);
            }

        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //находим настройку по ключу
        Preference preference = findPreference(key);
        //исключаем чекбокс
        if (!(preference instanceof CheckBoxPreference)) {
            String value = sharedPreferences.getString(preference.getKey(), "");

            //вызываем метод  и передаем параметры
            setPreferenceLabel(preference, value);
        }
    }

    //регистриуем лисенер и переопределяем
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //снимаем с регистрации лисенер и переопределяем
    @Override
    public void onDestroy() {
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    //еализуем метод для конкретной настройки
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Toast toast = Toast.makeText(getContext(), "Введи число!", Toast.LENGTH_SHORT);

        if (preference.getKey().equals("default_interval")) {
            String defaultIntervalString = (String) newValue;

            try {
                //пробуем распознать эту строку как целое число
                int defaultInterval = Integer.parseInt(defaultIntervalString);
                //если ошибка то выводим тост
            } catch (NumberFormatException nef) {
                toast.show();

            }
        }
        return false;
    }
}
