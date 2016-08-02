package com.android.joneslee.library.download;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 14:20
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class Download implements Parcelable {

  public Download() {

  }

  private int progress;
  private int currentFileSize;
  private int totalFileSize;
  private File file;

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public int getCurrentFileSize() {
    return currentFileSize;
  }

  public void setCurrentFileSize(int currentFileSize) {
    this.currentFileSize = currentFileSize;
  }

  public int getTotalFileSize() {
    return totalFileSize;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
  }

  public void setTotalFileSize(int totalFileSize) {
    this.totalFileSize = totalFileSize;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeInt(progress);
    dest.writeInt(currentFileSize);
    dest.writeInt(totalFileSize);
  }

  private Download(Parcel in) {

    progress = in.readInt();
    currentFileSize = in.readInt();
    totalFileSize = in.readInt();
  }

  public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
    public Download createFromParcel(Parcel in) {
      return new Download(in);
    }

    public Download[] newArray(int size) {
      return new Download[size];
    }
  };
}
