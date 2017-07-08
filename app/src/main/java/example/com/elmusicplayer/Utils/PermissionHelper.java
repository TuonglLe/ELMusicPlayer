package example.com.elmusicplayer.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public final class PermissionHelper {
    private PermissionHelper(){}

    public static boolean permissionIsGranted(Context context, String permission){
        boolean isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        if(isGranted){
           return true;
        }
        return false;
    }

    public static void requestPermission(Context context, String permission, int requestCode){
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }
}
