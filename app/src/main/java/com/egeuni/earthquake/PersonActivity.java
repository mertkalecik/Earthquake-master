package com.egeuni.earthquake;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonActivity extends AppCompatActivity implements PersonDataAdapter.PersonAdapterOnClickHandler {

    private static final String TAG = PersonActivity.class.getSimpleName();
    private PersonDataAdapter mPersonAdapter;
    private ArrayList<TaskUser> mUserList;
    private AppDatabase mDb;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TaskUser mUser;

    @BindView(R.id.btn_selection)
    FloatingActionButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView = findViewById(R.id.person_recyclerView);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mPersonAdapter = new PersonDataAdapter(PersonActivity.this);
        mPersonAdapter.setActivity(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        loadUsers();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(TaskUser currentUser) {
        mUser = currentUser;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserList = mPersonAdapter.getPersonData();
        if(mUserList != null){
            mDb.taskDao().deleteAllUser();

            for (TaskUser t:mUserList) {
                mDb.taskDao().insertUser(t);
            }
        }
    }

    private void fillUsers() {
        Thread t = new Thread() {
            public void run() {
            }
        };

        t.run();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadUsers() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                mUserList = new ArrayList<TaskUser>();

                List<TaskUser> list= mDb.taskDao().loadAllProfiles();

                for (TaskUser t:list) {
                    mUserList.add(t);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(mUserList.size()>0) {
                    mPersonAdapter.setPersonData(mUserList);
                    mRecyclerView.setAdapter(mPersonAdapter);
                }
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
    }
}
