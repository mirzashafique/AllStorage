package e.mirzashafique.lib;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import e.mirzashafique.lib.model.Config;

public class AllStorageActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_storage);

        Intent intent = getIntent();
        final Config config = intent.getParcelableExtra("all-storage-actvity");

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", config.getMaxFile());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
}
