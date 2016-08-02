package com.android.joneslee.library.util;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.joneslee.library.AccessibilityInstallService;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 16:29
 */

public class AccessibilityUtils {
  private static final String TAG = "AccessibilityUtils";

  public static boolean isAccessibilitySettingsOn(Context context) {
    int accessibilityEnabled = 0;
    final String service =
        context.getPackageName() + "/" + AccessibilityInstallService.class.getCanonicalName();
    try {
      accessibilityEnabled = Settings.Secure.getInt(
          context.getApplicationContext().getContentResolver(),
          android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
      Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
    } catch (Settings.SettingNotFoundException e) {
      Log.e(TAG, "Error finding setting, default accessibility to not found: "
          + e.getMessage());
    }
    TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

    if (accessibilityEnabled == 1) {
      Log.v(TAG, "ACCESSIBILITY IS ENABLED");
      String settingValue = Settings.Secure.getString(
          context.getApplicationContext().getContentResolver(),
          Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
      if (settingValue != null) {
        mStringColonSplitter.setString(settingValue);
        while (mStringColonSplitter.hasNext()) {
          String accessibilityService = mStringColonSplitter.next();
          if (accessibilityService.equalsIgnoreCase(service)) {
            return true;
          }
        }
      }
    } else {
      Log.v(TAG, "ACCESSIBILITY IS DISABLED");
    }
    return false;
  }
}
