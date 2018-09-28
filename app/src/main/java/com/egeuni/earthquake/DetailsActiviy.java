package com.egeuni.earthquake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activiy);
        TextView tv_mag_label = findViewById(R.id.tv_mag_label);
        TextView tv_place_label = findViewById(R.id.tv_place_label);
        TextView tv_depth_label = findViewById(R.id.tv_depth_label);
        Intent intent = getIntent();

        Event event = (Event)intent.getSerializableExtra("object");

        tv_mag_label.setText(event.getMag());
        tv_depth_label.setText(event.getDepth() + "km");
        tv_place_label.setText(event.getPlace());

    }
}
