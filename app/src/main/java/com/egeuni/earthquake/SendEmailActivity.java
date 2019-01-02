package com.egeuni.earthquake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendEmailActivity extends AppCompatActivity {

    @BindView(R.id.tv_subject)
    EditText mSubject;
    @BindView(R.id.tv_body)
    EditText mBody;
    @BindView(R.id.btn_send)
    Button mSendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        ButterKnife.bind(this);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }


    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"earthquake.egeuni@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, mBody.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendEmailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
