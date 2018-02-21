package id.web.jokopriyono.moviedicoding;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Created by Joko Priyono on 21/02/18.
 */

public class MethodHelper {
    /**
     * Pengecekan koneksi data device
     * @param context context pemanggil
     * @return default true, jika false artinya tidak ada sambungan data
     */
    public static boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
