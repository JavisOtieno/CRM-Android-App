package com.example.crm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class IndividualTaskActivity extends AppCompatActivity
implements OnSuccessListener{
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView accountNameTextView;
    private TextView contactNameTextView;
    private TextView statusTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button editTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_task);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Task");

        String taskId = getIntent().getStringExtra("taskId");

        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);
        contactNameTextView = (TextView) findViewById(R.id.contactNameTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        editTaskButton = (Button) findViewById(R.id.editTaskButton);

        NetworkUtils.
                fetchDataGet("task/"+taskId,
                        "", IndividualTaskActivity.this,
                        IndividualTaskActivity.this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(ResponseBody responseBody) {
        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("task")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("task") &&
                        jsonObject.get("task") instanceof JSONObject
                ) {

                    JSONObject task = jsonObject.getJSONObject("task");

                    accountNameTextView.setText(task.getJSONObject("account")
                            .getString("name"));
                    contactNameTextView.setText(task.getJSONObject("contact")
                            .getString("name"));
                    String status = task.getString("status");
                    status = status.substring(0, 1).toUpperCase() +
                            status.substring(1).toLowerCase();
                    statusTextView.setText(status);

                    descriptionTextView.setText(task.getString("description"));
                    dateTextView.setText(task.getString("date"));
                    timeTextView.setText(task.getString("time"));

                    editTaskButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(IndividualTaskActivity.this, EditTaskActivity.class);
                            intent.putExtra("task", task.toString());
                            startActivity(intent);
                        }
                    });


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualTaskActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualTaskActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualTaskActivity.this,"Loading Account Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onComplete() {

    }
}