package com.egeuni.earthquake;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActiviy extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailsActiviy";

    private Event event;
    //Label UI
    @BindView(R.id.label_mag)
    TextView labelMag;
    @BindView(R.id.label_depth)
    TextView labelDepth;
    @BindView(R.id.label_place)
    TextView labelPlace;

    //Details UI
    @BindView(R.id.tv_date_result)
    TextView dateResult;
    @BindView(R.id.tv_hour_result)
    TextView hourResult;
    @BindView(R.id.tv_lat_result)
    TextView latResult;
    @BindView(R.id.tv_long_result)
    TextView longResult;
    @BindView(R.id.btn_redirect)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        event = (Event)intent.getSerializableExtra("object");


        initUI();

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorDetails));
    }

    private void initUI() {
        /*Result TextViews definitions*/
        dateResult.setText(event.getDate());
        latResult.setText(event.getLatitude());
        longResult.setText(event.getLongitude());
        hourResult.setText(event.getHour());


        labelMag.setText(event.getMag());
        labelDepth.setText(event.getDepth());
        labelPlace.setText(event.getPlace());



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailsActiviy.this, MapActivity.class);
                i.putExtra("object", event);
                startActivity(i);
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

}
