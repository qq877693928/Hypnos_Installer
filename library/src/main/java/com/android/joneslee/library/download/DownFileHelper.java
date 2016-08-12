package com.android.joneslee.library.download;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.joneslee.library.Config;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 14:41
 */

public class DownFileHelper {
  private static WeakReference<OnProgressListener> sProgressListenerWeakReference;
  private static WeakReference<Activity> sActivityWeakReference;

  private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

      if (intent.getAction().equals(Config.MESSAGE_PROGRESS)) {

        Download download = intent.getParcelableExtra(DownloadService.FILE_DOWNLOAD);
        if (sProgressListenerWeakReference.get() != null) {
          sProgressListenerWeakReference.get().onProgress(
              download.getProgress(),
              download.getCurrentFileSize(), download.getTotalFileSize());
        }
        if (download.getProgress() == 100) {
          Log.d("DownFileHelper",
              "File Download Complete : " + download.getFilePath());
          if (sProgressListenerWeakReference.get() != null) {
            sProgressListenerWeakReference.get().onSuccess(new File(download.getFilePath()));
          }
        } else {
          Log.d("DownFileHelper", String.format("Downloaded (%d/%d) MB",
              download.getCurrentFileSize(), download.getTotalFileSize()));
        }
      }
    }
  };

  public static void downloadApkFile(Activity activity, String apkUrl,
      OnProgressListener onProgressListener) {
    if (activity == null) {
      return;
    }
    sActivityWeakReference = new WeakReference<>(activity);
    sProgressListenerWeakReference = new WeakReference<>(onProgressListener);
    startDownload(apkUrl);
  }

  private static void startDownload(String url) {
    if (sActivityWeakReference.get() != null) {
      registerReceiver(sActivityWeakReference.get());

      Intent intent = new Intent(sActivityWeakReference.get(), DownloadService.class);
      Bundle bundle = new Bundle();
      bundle.putString(DownloadService.FILE_URL, url);
      intent.putExtras(bundle);
      sActivityWeakReference.get().startService(intent);
    }
  }

  private static void registerReceiver(Activity activity) {
    LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(activity);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Config.MESSAGE_PROGRESS);
    bManager.registerReceiver(broadcastReceiver, intentFilter);
  }

  public interface OnProgressListener {
    void onProgress(int progress, int currentFileSize, int totalFileSize);

    void onSuccess(File file);
  }
}
