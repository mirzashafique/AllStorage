package e.mirzashafique.lib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import e.mirzashafique.lib.model.Config;

public abstract class Storage implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    public static Context context;
    private static Config config;
    public static Activity activity;
    private static boolean isStartActivity = false;

//    public static void method(Context mcontext) {
//        context = mcontext;
//        showStoragePreview();
//    }

    public abstract void start();

    public abstract void start(int requestCode);

    public static class ImagePickerWithActivity extends Storage {

        private Activity activity;

        public ImagePickerWithActivity(Activity activity) {
            this.activity = activity;
            // init();
        }

        @Override
        public void start(int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        @Override
        public void start() {
            activity.startActivityForResult(getIntent(activity), 121);
        }
    }

    public static class ImagePickerWithFragment extends Storage {

        private Fragment fragment;

        public ImagePickerWithFragment(Fragment fragment) {
            this.fragment = fragment;
            //  init();
        }

        @Override
        public void start(int requestCode) {
            fragment.startActivityForResult(getIntent(fragment.getActivity()), requestCode);
        }

        @Override
        public void start() {
            fragment.startActivityForResult(getIntent(fragment.getActivity()), 1);
        }
    }

    public static void create(Activity activityA) {
        config = new Config(true, 5, true, 5, true, 5, true, 5);
        // return new ImagePickerWithActivity(activity);
        activity = activityA;
    }

    public static ImagePickerWithFragment create(Fragment fragment) {
        config = new Config(true, 5, true, 5, true, 5, true, 5);
        return new ImagePickerWithFragment(fragment);
    }

    public Intent getIntent(Context context) {
        //ImagePickerConfig config = ConfigUtils.checkConfig(getConfig());
        Intent intent = new Intent(context, AllStorageActivity.class);
        intent.putExtra("all-storage-actvity", config);
        //intent.putExtra(ImagePickerConfig.class.getSimpleName(), config);
        return intent;
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
                startStorageActivity(activity);
            } else {
                // Permission request was denied.

                Toast.makeText(context, R.string.storage_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private static void showStoragePreview(Activity activity) {
        // BEGIN_INCLUDE(startStorageActivity)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview

            startStorageActivity(activity);
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


    private static ImagePickerWithActivity startStorageActivity(Activity activity) {
        //  startActivity(new Intent(getApplicationContext(), AllMediaStorageActivity.class));
        //   context.startActivity(new Intent(context, AllStorageActivity.class));
        return new ImagePickerWithActivity(activity);
    }
}
