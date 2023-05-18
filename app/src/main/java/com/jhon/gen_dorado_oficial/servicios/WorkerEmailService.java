package com.jhon.gen_dorado_oficial.servicios;

import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import java.util.concurrent.TimeUnit;

public class WorkerEmailService extends Worker {


    private Context thisContext = getApplicationContext();

    public WorkerEmailService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    public static void guardarEmail(long duracion, Data data, String tag){
        OneTimeWorkRequest noti = new OneTimeWorkRequest.Builder(workerService.class)
                .setInitialDelay(duracion, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .setInputData(data)
                .build();

        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(noti);
    }

    @NonNull
    @Override
    public Result doWork() {

        String titulo = getInputData().getString("email");
        String texto = getInputData().getString("asunto");
        int id = (int) getInputData().getLong("idNoti", 0);



        return Result.success();

    }


}
