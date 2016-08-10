package com.android.joneslee.library;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.android.joneslee.library.download.DownFileHelper;
import com.android.joneslee.library.util.AccessibilityUtils;
import com.android.joneslee.library.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * Description:安装器
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 15:45
 */

public class Installer {
  private static final String TAG = "Installer";
  private WeakReference<Activity> mActivityWeakReference;

  public enum MODE {
    ROOT_ONLY,
    ACCESSIBILITY_ONLY,
    AUTO
  }

  private MODE mMode = MODE.AUTO;

  private static volatile Installer sInstance;

  public static Installer getInstance() {
    Installer result = sInstance;
    if (result == null) {
      synchronized (Installer.class) {
        result = sInstance;
        if (result == null) {
          sInstance = result = new Installer();
        }
      }
    }
    return result;
  }


  public void install(Activity activity, String fileUrl) {
    install(activity, null, fileUrl);
  }

  public void install(Activity activity, MODE mode, String fileUrl) {
    if (isEmptyActivity(activity)) {
      return;
    }
    downApkFile(mode, fileUrl);
  }

  public void install(Activity activity, File file) {
    install(activity, null, file);
  }

  /**
   * @param mode install method
   * @param file local file
   */
  public void install(Activity activity, MODE mode, File file) {
    if (mode != null) {
      mMode = mode;
    }
    if (isEmptyActivity(activity)) {
      return;
    }
    if (file != null && file.exists()) {
      switch (mMode) {
        case ROOT_ONLY:
          if (FileUtils.isRooted()) {
            installWithRooted(file);
          }
          break;
        case ACCESSIBILITY_ONLY:
          if (mActivityWeakReference.get() != null) {
            if (AccessibilityUtils.isAccessibilitySettingsOn(mActivityWeakReference.get())) {
              installWithNormal(mActivityWeakReference.get(), file);
            } else {
              mActivityWeakReference.get()
                  .startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
          }
          break;
        case AUTO:
          // first step: check rooted method
          // second step: check accessibility setting method
          // finally: launch system install activity
          if (FileUtils.isRooted()) {
            installWithRooted(file);
          } else {
            if (mActivityWeakReference.get() != null) {
              installWithNormal(mActivityWeakReference.get(), file);
            }
          }
          break;
      }
    }
  }

  private boolean isEmptyActivity(Activity activity) {
    if (activity == null) {
      return true;
    }
    mActivityWeakReference = new WeakReference<>(activity);
    return false;
  }

  private void installWithNormal(Activity activity, File file) {
    if (activity != null) {
      if (!FileUtils.isExistSdcard()) {
        try {
          grantFile(file);
        } catch (IOException e) {
          return;
        }
      }
      Uri uri = Uri.fromFile(file);
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setDataAndType(uri, Config.INTALL_ACTION);
      activity.startActivity(intent);
    }
  }

  /**
   * 授权文件夹777权限
   *
   * @param file
   * @throws IOException
   */
  private void grantFile(File file) throws IOException {
    String command = "chmod -R 777 " + file.getParent();
    Runtime.getRuntime().exec(command);
  }


  private void downApkFile(final MODE mode, String fileUrl) {
    DownFileHelper.downloadApkFile(mActivityWeakReference.get(), fileUrl,
        new DownFileHelper.OnProgressListener() {
          @Override
          public void onProgress(int progress, int currentFileSize, int totalFileSize) {}

          @Override
          public void onSuccess(File file) {
            install(mActivityWeakReference.get(), mode, file);
          }
        });
  }


  /**
   * install apk with shell method (when phone had root)
   *
   * @param file apk file obj
   * @return
   */
  private boolean installWithRooted(File file) {
    boolean result = false;
    Process process = null;
    OutputStream outputStream = null;
    BufferedReader errorStream = null;
    try {
      process = Runtime.getRuntime().exec("su");
      outputStream = process.getOutputStream();

      String command = "pm install -r " + file.getAbsolutePath() + "\n";
      outputStream.write(command.getBytes());
      outputStream.flush();
      outputStream.write("exit\n".getBytes());
      outputStream.flush();
      process.waitFor();
      errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      StringBuilder msg = new StringBuilder();
      String line;
      while ((line = errorStream.readLine()) != null) {
        msg.append(line);
      }
      Log.d(TAG, "install msg is " + msg);
      if (!msg.toString().contains("Failure")) {
        result = true;
      }
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
        if (errorStream != null) {
          errorStream.close();
        }
      } catch (IOException e) {
        outputStream = null;
        errorStream = null;
        process.destroy();
      }
    }
    return result;
  }
}
