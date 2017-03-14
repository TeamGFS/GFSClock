package com.github.gfsclock.gfstimeclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final Button button = (Button) findViewById(R.id.Save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toScanBadge(view);
            }
        });
    }

    public void toScanBadge(View view) {
        Intent next = new Intent (Admin.this, ScanBadge.class);
        startActivity (next);
    }
}
