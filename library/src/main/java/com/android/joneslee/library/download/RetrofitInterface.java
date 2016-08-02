package com.android.joneslee.library.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Description:
 *
 * @author lizhenhua9@wanda.cn (lzh)
 * @date 16/7/31 14:17
 */

public interface RetrofitInterface {
  @GET
  @Streaming
  Call<ResponseBody> downloadFile(@Url String fileUrl);
}
