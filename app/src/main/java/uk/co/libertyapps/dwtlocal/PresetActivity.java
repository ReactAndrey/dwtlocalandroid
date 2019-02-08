package uk.co.libertyapps.dwtlocal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PresetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);
        setTitle("Presets");

        Button presetDoneButton = findViewById(R.id.presetDoneButton);

        presetDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TestingActivity.class);
                startActivity(intent);
            }
        });

    }
}
