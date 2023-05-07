package com.jhon.gen_dorado_oficial.servicios;

import static com.jhon.gen_dorado_oficial.servicios.App.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jhon.gen_dorado_oficial.R;

import java.util.concurrent.TimeUnit;

public class workerService extends Worker {

    private Context thisContext = getApplicationContext();


    public workerService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    public static void guardarNoti(long duracion, Data data, String tag){
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

        String titulo = getInputData().getString("titulo");
        String texto = getInputData().getString("texto");
        int id = (int) getInputData().getLong("idNoti", 0);

        mostrarNotification(titulo, texto);

        return Result.success();

    }

    private void mostrarNotification(String title, String descrip) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                    CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_add)
                    .setContentTitle(title)
                    .setContentText(descrip)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            managerCompat.notify(1, builder.build());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                    CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_add)
                    .setContentTitle(title)
                    .setContentText(descrip)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
            managerCompat.notify(1, builder.build());
        }
    }


}
