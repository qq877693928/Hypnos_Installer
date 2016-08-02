package com.android.joneslee.hypnos_installer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LauncherActivity extends AppCompatActivity {
  private Snackbar mSnackbar;
  private Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_launcher, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void showSnackBarNotice(View view) {
    SpannableStringBuilder snackbarText = new SpannableStringBuilder();
    snackbarText.append("是否打开");
    int boldStart = snackbarText.length();
    snackbarText.append("Accessibility");
    snackbarText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
        boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart,
        snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    snackbarText.append("设置");
    mSnackbar = Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG)
        .setAction("Setting", new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
          }
        });
    mSnackbar.show();
  }
}
