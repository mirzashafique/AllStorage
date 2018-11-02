package e.mirzashafique.lib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

public class Storage implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    public static Context context;

    public static void method(Context mcontext) {
        context = mcontext;
        showStoragePreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

                Toast.makeText(context, R.string.storage_permission_granted, Toast.LENGTH_LONG).show();
                startStorageActivity();
            } else {
                // Permission request was denied.

                Toast.makeText(context, R.string.storage_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private static void showStoragePreview() {
        // BEGIN_INCLUDE(startStorageActivity)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview

            startStorageActivity();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }

    }

    private static void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Toast.makeText(context, R.string.storage_access_required, Toast.LENGTH_LONG).show();
            // Request the permission
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        } else {
            Toast.makeText(context, R.string.storage_unavailable, Toast.LENGTH_LONG).show();

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
    }


    private static void startStorageActivity() {
        //  startActivity(new Intent(getApplicationContext(), AllMediaStorageActivity.class));
        context.startActivity(new Intent(context, AllStorageActivity.class));
    }
}
