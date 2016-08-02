package com.android.joneslee.library.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.android.joneslee.library.Config;
import com.android.joneslee.library.R;
import com.android.joneslee.library.util.FileUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DownloadService extends IntentService {
  public static final String FILE_URL = "file_url";
  public static final String FILE_DOWNLOAD = "file_download";

  public DownloadService() {
    super("Download Service");
  }

  private NotificationCompat.Builder notificationBuilder;
  private NotificationManager notificationManager;
  private int totalFileSize;

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) {
      return;
    }
    // Notification
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_download)
        .setContentTitle("Download").setContentText("Downloading File")
        .setAutoCancel(true);
    notificationManager.notify(0, notificationBuilder.build());
    String url = intent.getExtras().getString(FILE_URL);

    initDownload(url);
  }

  private void initDownload(String url) {
    // check url
    if (!TextUtils.isEmpty(url) && url.endsWith(".apk")) {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("http://github.com/").build();
      RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
      Call<ResponseBody> request = retrofitInterface.downloadFile(url);
      try {
        downloadFile(request.execute().body());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void downloadFile(ResponseBody body) throws IOException {
    int count;
    byte data[] = new byte[1024 * 4];
    long fileSize = body.contentLength();
    InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
    if (FileUtils.getDiskCacheDir(getBaseContext()) == null) {
      return;
    }
    File outputFile =
        new File(FileUtils.getDiskCacheDir(getBaseContext()) + File.separator
            + "update.apk");
    OutputStream output = new FileOutputStream(outputFile);
    long total = 0;
    long startTime = System.currentTimeMillis();
    int timeCount = 1;
    while ((count = bis.read(data)) != -1) {
      total += count;
      totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
      double current = Math.round(total / (Math.pow(1024, 2)));
      int progress = (int) ((total * 100) / fileSize);
      long currentTime = System.currentTimeMillis() - startTime;
      Download download = new Download();
      download.setTotalFileSize(totalFileSize);
      if (currentTime > 1000 * timeCount) {
        download.setCurrentFileSize((int) current);
        download.setProgress(progress);
        sendNotification(download);
        timeCount++;
      }
      output.write(data, 0, count);
    }
    onDownloadComplete(outputFile);
    output.flush();
    output.close();
    bis.close();
  }

  private void sendNotification(Download download) {
    sendIntent(download);
    notificationBuilder.setProgress(100, download.getProgress(), false);
    notificationBuilder.setContentText(
        "Downloading file " + download.getCurrentFileSize() + "/" + totalFileSize + " MB");
    notificationManager.notify(0, notificationBuilder.build());
  }

  private void sendIntent(Download download) {
    Intent intent = new Intent(Config.MESSAGE_PROGRESS);
    intent.putExtra(FILE_DOWNLOAD, download);
    LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
  }

  private void onDownloadComplete(File file) {
    Download download = new Download();
    download.setFile(file);
    download.setProgress(100);
    sendIntent(download);
    notificationManager.cancel(0);
    notificationBuilder.setProgress(0, 0, false);
    notificationBuilder.setContentText("File Downloaded");
    notificationManager.notify(0, notificationBuilder.build());
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    notificationManager.cancel(0);
  }
}
