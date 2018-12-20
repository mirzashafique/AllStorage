package e.mirzashafique.lib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.List;

import e.mirzashafique.lib.model.Config;
import e.mirzashafique.lib.model.SelectedFiles;
import e.mirzashafique.lib.model.SingletonList;

public abstract class Storage implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_STORAGE = 0;
    private static final int ACTIVITY_REQUEST_CODE = 121;

    //Variables
    public static Fragment fragment;
    private static Config config;
    public static Activity activity;

    public abstract void start();

    //Nested Classes
    public static class StorageAccessWithActivity extends Storage {
        private Activity activity;

        public StorageAccessWithActivity(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        }

        @Override
        public void start() {
            showStoragePreview(activity);
        }
    }

    public static class StorageAccessWithFragment extends Storage {
        private Fragment fragment;

        public StorageAccessWithFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void start() {
            Storage.showStoragePreview(fragment.getActivity());
        }

        @Override
        public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {

        }
    }

    //Initialization and permissions
    public static StorageAccessWithActivity create(Activity activityA) {
        config = new Config(false, 5, false, 5, false, 5, false, 5);
        activity = activityA;
        return new StorageAccessWithActivity(activityA);
    }

    public static StorageAccessWithFragment create(Fragment fragmentA) {
        config = new Config(false, 5, false, 5, false, 5, false, 5);
        fragment = fragmentA;
        return new StorageAccessWithFragment(fragment);
    }


    private static void showStoragePreview(Activity activity) {
        // Check if the Storage permission has been granted
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start storage preview
            mStart();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }

    }

    private static void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        } else {
            Toast.makeText(activity, R.string.storage_access_required, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
    }

    public static void mStart() {
        activity.startActivityForResult(getIntent(activity), ACTIVITY_REQUEST_CODE);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AllStorageActivity.class);
        intent.putExtra("all-storage-actvity", config);
        return intent;
    }

    //Configration
    public Storage all() {
        config = new Config(true, 5, true, 5, true, 5, true, 5);
        return this;
    }

    public Storage showFiles(int maxFilesSelection) {
        config.setFiles(true);
        config.setMaxFile(maxFilesSelection);
        return this;
    }

    public Storage showImages(int maxImagesSelection) {
        config.setImages(true);
        config.setMaxImages(maxImagesSelection);
        return this;
    }

    public Storage showOnlyAudios(int maxAudioSelection) {
        config.setAudios(true);
        config.setMaxAudios(maxAudioSelection);
        return this;
    }

    public Storage showOnlyVideos(int maxVideoSelection) {
        config.setVideos(true);
        config.setMaxVideos(maxVideoSelection);
        return this;
    }


    // Helper


    public static boolean shouldHandle(int requestCode, int resultCode, Intent data) {
        return resultCode == Activity.RESULT_OK
                && requestCode == ACTIVITY_REQUEST_CODE
                && data != null;
    }

    public static void handlePermissions(int requestCode, int grantResults[]) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[PERMISSION_REQUEST_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                mStart();
            } else {
                // Permission request was denied.
                Toast.makeText(activity, R.string.storage_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static List<SelectedFiles> getResults() {
        return SingletonList.getmInstence().getSelectedFiles();
    }
}
