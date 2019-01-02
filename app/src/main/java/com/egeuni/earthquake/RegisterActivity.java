package com.egeuni.earthquake;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase mDb;
    private String name, surname, relation, sex, place;
    private String[] cityArray =new String[]{"...","Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
            "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};;

    @BindView(R.id.tv_regname)
    EditText mName;
    @BindView(R.id.tv_regsurname)
    EditText mSurname;
    @BindView(R.id.tv_regrelation)
    EditText mRelation;
    @BindView(R.id.spinner)
    Spinner mSpinner;
    @BindView(R.id.radio_male)
    RadioButton rMale;
    @BindView(R.id.radio_female)
    RadioButton rFemale;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mDb = AppDatabase.getsInstance(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, cityArray);
        mSpinner.setAdapter(adapter);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        initialize();
        if (!validate()) {
            Toast.makeText(this, "Register has Failed!", Toast.LENGTH_LONG).show();
        } else {
            onSuccess();
        }
    }

    private String toUpperCaseFirstLetter(String s) {
        if(s.length() > 1) {
            return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        } else {
            return "";
        }
    }

    private void onSuccess() {
        TaskUser user = new TaskUser(name, surname, place, relation, sex);
        mDb.taskDao().insertUser(user);
        Intent i = new Intent(this,PersonActivity.class);
        startActivity(i);
    }

    private boolean validate() {
        boolean valid = true;
        if(name.isEmpty() || name.length()>32) {
            mName.setError("Please enter a valid name");
            valid = false;
        }
        if(surname.isEmpty() || surname.length()>32) {
            mSurname.setError("Please enter a valid name");
            valid = false;
        }
        if(relation.isEmpty() || relation.length()>32) {
            mRelation.setError("Please enter a valid name");
            valid = false;
        }
        if(place.equals("...")) {Toast.makeText(this,"You should select a city...", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    private void initialize() {
        name = mName.getText().toString().trim();
        surname = mSurname.getText().toString().trim();
        relation = mRelation.getText().toString().trim();
        place = mSpinner.getSelectedItem().toString().trim();
        if(rMale.isChecked()) {
            sex = getApplicationContext().getString(R.string.person_gender_male);
        } else {
            sex =  getApplicationContext().getString(R.string.person_gender_female);
        }
        name = toUpperCaseFirstLetter(name);
        surname = toUpperCaseFirstLetter(surname);
        relation = toUpperCaseFirstLetter(relation);
        place = toUpperCaseFirstLetter(place);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,PersonActivity.class);
        startActivity(intent);
    }
}
