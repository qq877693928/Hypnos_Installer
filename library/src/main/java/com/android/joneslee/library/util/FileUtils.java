package com.android.joneslee.library.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

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
    String cachePath = null;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || !Environment.isExternalStorageRemovable()) {
      if (context != null) {
        cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
      }
    } else {
      Toast.makeText(context, "请插入SD卡", Toast.LENGTH_LONG).show();
    }
    return cachePath;
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
