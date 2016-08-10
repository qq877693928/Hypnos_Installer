package com.android.joneslee.hypnos_installer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.joneslee.library.Installer;
import com.android.joneslee.library.util.AccessibilityUtils;
import com.android.joneslee.library.util.FileUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class LauncherActivityFragment extends Fragment {
  private final static String URL =
      "http://storage.pgyer.com/a/1/1/6/8/a1168341d7774a4a59d76a1b7e0756cd.apk";
  private Button mRootInstall;
  private Button mAccessibilityInstall;
  private Button mAutoInstall;

  public LauncherActivityFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_launcher, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    mRootInstall = (Button) view.findViewById(R.id.root_button);
    mRootInstall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (FileUtils.isRooted()) {
          Installer.getInstance().install(getActivity(), Installer.MODE.ROOT_ONLY, URL);
        } else {
          Toast.makeText(view.getContext(), "未获取Root权限", Toast.LENGTH_LONG).show();
        }
      }
    });
    mAccessibilityInstall = (Button) view.findViewById(R.id.accessibility_button);
    mAccessibilityInstall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (AccessibilityUtils.isAccessibilitySettingsOn(view.getContext())) {
          Installer.getInstance().install(getActivity(), Installer.MODE.ACCESSIBILITY_ONLY, URL);
        } else {
          if (getActivity() != null && getActivity() instanceof LauncherActivity) {
            ((LauncherActivity) getActivity()).showSnackBarNotice(view);
          }
        }
      }
    });
    mAutoInstall = (Button) view.findViewById(R.id.auto_button);
    mAutoInstall.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Installer.getInstance().install(getActivity(), Installer.MODE.AUTO, URL);
      }
    });
  }
}
