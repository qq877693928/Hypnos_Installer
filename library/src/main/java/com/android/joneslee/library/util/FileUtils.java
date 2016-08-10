package com.android.joneslee.library.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 14:22
 */

public class FileUtils {

  /**
   * get file path of store
   *
   * @return
   */
  public static String getDiskCacheDir(@NonNull Context context) {
    String cachePath;
    if (isExistSdcard()) {
      cachePath = getFilePath(context.getExternalFilesDir(null).getAbsolutePath());
    } else {
      cachePath = getFilePath(context.getFilesDir().getAbsolutePath());
    }
    return cachePath;
  }

  @NonNull
  private static String getFilePath(String absolutePath) {
    if (TextUtils.isEmpty(absolutePath)) {
      return "";
    }
    File file = new File(absolutePath);
    if (!file.exists()) {
      file.mkdir();
    }
    return file.getAbsolutePath();
  }

  public static boolean isExistSdcard() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable();
  }

  /**
   * check phone had root
   */
  public static boolean isRooted() {
    String[] paths = {"/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"};
    for (String path : paths) {
      if (new File(path).exists()) {
        return true;
      }
    }
    return false;
  }
}
