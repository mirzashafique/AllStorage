package pakbachelor.allstorage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import e.mirzashafique.lib.Storage;

public class MainActivity extends AppCompatActivity {
    Button button, button2;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        frameLayout = findViewById(R.id.frame_layout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage.create(MainActivity.this).all().start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage.create(MainActivity.this).showFiles(5).showImages(5).start();
            }
        });

        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        BlankFragment fragment = new BlankFragment();
        manager.add(R.id.frame_layout, fragment);
        manager.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Storage.shouldHandle(requestCode, resultCode, data)) {
            Toast.makeText(getApplicationContext(), Storage.getResults().size() + "", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Storage.handlePermissions(requestCode, grantResults);
    }
}
