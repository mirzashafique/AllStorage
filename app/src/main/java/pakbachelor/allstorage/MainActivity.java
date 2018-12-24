package pakbachelor.allstorage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import e.mirzashafique.lib.Storage;
import e.mirzashafique.lib.model.SelectedFiles;

public class MainActivity extends AppCompatActivity {
    Button selectFromCameraButton, selectImFileButton, selectAllMediaButton;
    FrameLayout frameLayout;
//    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFromCameraButton = findViewById(R.id.select_from_camera_button);
        selectImFileButton = findViewById(R.id.select_im_file_button);
        selectAllMediaButton = findViewById(R.id.select_all_button);
        frameLayout = findViewById(R.id.frame_layout);

        selectFromCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Storage.create(MainActivity.this).showCamera(5).start(); //Select Images by using camera
            }
        });
        selectImFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage.create(MainActivity.this).showFiles(5).showImages(5).start();  // Select Files and Images
                Storage.create(MainActivity.this).getClass();
            }
        });
        selectAllMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage.create(MainActivity.this).all().start();  // Select All Media Types
            }
        });

        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        BlankFragment fragment = new BlankFragment();
        manager.add(R.id.frame_layout, fragment);
        manager.commit();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Storage.shouldHandle(requestCode, resultCode, data)) {
            List<SelectedFiles> selectedFiles = Storage.getResults();
            Toast.makeText(getApplicationContext(), selectedFiles.size() + "", Toast.LENGTH_LONG).show();
            Log.d("www", "" + selectedFiles.size());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Storage.handlePermissions(requestCode, grantResults);
    }


}