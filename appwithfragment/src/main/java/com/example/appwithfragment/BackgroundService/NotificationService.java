package com.example.appwithfragment.BackgroundService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Евгения on 01.09.2016.
 */
public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*
* службе нужно передать ссылки нескольких первых(или последних ? ) загруженных фотографий (как правильно использовать интент)
* в onStartCommand запускаем поток на выполнение запроса к сайту фликр
* получив ответ просматриваем полученные ссылки, если они отличны от старых, то посылаем уведомление
* если не получен ответ или результат отрицателен, то продолжить выполнение службы
*
* после получения положительного результата и посылки уведомления продолжить выполнение службы (бесконечный while ?)
*
* после получения полож рез-та идет запуск активити (скорее всего новый запуск => нам не нужно заморачиваться
* на счет загрузки фото с нуля)
*
* запрос может быть можно выполнять через некоторые промежутки времени ? наверное используя alarmManager ?
*
* если система уничтожит службу, надо начать ее вновь со старым интентом(или без интента, если мы сохраним данные в поле класса службы ?)
*
* */
    /*TODO как правильно использовать интент
    * TODO как правильно использовать alarmManager
    * TODO сохраним данные в поле класса службы ?*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendNotification(){

    }
}
