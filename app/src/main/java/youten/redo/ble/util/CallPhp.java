package youten.redo.ble.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by kiri on 2016/8/11.
 */
public interface CallPhp {

    @POST("http://163.17.136.253/s1410232003/beacon.php")
    Call<ResponseBody> getMethod(@Query("useUnicode") String useUnicode,
                                 @Query("characterEncoding") String encode,
                                 @Query("location") String location,
                                 @Query("blue") String blue,
                                 @Query("green") String green,
                                 @Query("purple") String purple);
}
