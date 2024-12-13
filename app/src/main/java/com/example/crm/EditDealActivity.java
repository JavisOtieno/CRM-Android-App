package com.example.crm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.crm.models.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;

public class EditDealActivity extends AppCompatActivity implements OnSuccessListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Button submitButton;
//    private EditText nameEditText;
    private EditText amountEditText;
    private EditText accountEditText;
    private EditText statusEditText;
    private Boolean isTaskRunning = false;
//    private String name;
    private String account;
    private String status;
    private String amount;
    private Integer checkedStatus=-1;
    private Integer checkedAccount=-1;
    private String selectedAccountId="0";
    private String[] array = {"Pending", "Complete", "Cancelled"};

    private String selectedProductId = "0";
    private Integer checkedProduct=-1;
    private String product;
    private EditText productEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_deal);
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
        actionBar.setTitle("Edit Deal");

        String dealId = getIntent().getStringExtra("dealId");

        NetworkUtils.
                fetchDataGet("editdeal/"+dealId,
                        "",EditDealActivity.this,
                        EditDealActivity.this);

        submitButton = (Button)findViewById(R.id.submitButton);
//        nameEditText = (EditText)findViewById(R.id.nameEditText);

        amountEditText = (EditText)findViewById(R.id.amountEditText);
        accountEditText = (EditText)findViewById(R.id.accountEditText);
        statusEditText = (EditText)findViewById(R.id.statusEditText);
        productEditText = (EditText)findViewById(R.id.productEditText);

        statusEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusDialog(view);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasts.toastIconError(AddAccountActivity.this,"Toast");

                //   longitude = longEditText.getText().toString();
                //   latitude = latEditText.getText().toString();
                if (!isTaskRunning) {
                    isTaskRunning = true;

                    product = selectedProductId;
                    amount = amountEditText.getText().toString();
                    account = selectedAccountId;
                    status = statusEditText.getText().toString().toLowerCase();

                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("product_id", product);
                        requestBody.put("amount", amount);
                        requestBody.put("account_id",account);
                        requestBody.put("status",status);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkUtils.
                            fetchDataPost("submiteditdeal/"+dealId,
                                    "", requestBody,EditDealActivity.this,
                                    EditDealActivity.this);



//                    new JsonTask().execute(Constants.BASE_URL + "api/addf", "POST",
//                            userId, outletId, visitId, feedback);
//
                }


            }
        });

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


    private void showStatusDialog(final View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status");


        builder.setSingleChoiceItems(array, checkedStatus, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                checkedStatus = i;
            }
        });
        builder.show();
    }

    private void showProductDialog(final View v,String[] productOptions, String[] productIds) {
        final String[] array = productOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Products");
        builder.setSingleChoiceItems(array, checkedProduct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                checkedProduct = i;
                selectedProductId = productIds[i];
            }
        });
        builder.show();
    }


    private void showAccountDialog(final View v,String[] accountOptions, String[] accountIds) {
        final String[] array = accountOptions;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accounts");
        builder.setSingleChoiceItems(array, checkedAccount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
                checkedAccount = i;
                selectedAccountId = accountIds[i];
            }
        });
        builder.show();
    }





    @Override
    public void onSuccess(ResponseBody responseBody) {

        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("Deal")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("status") &&
                        jsonObject.get("status") instanceof String &&
                        jsonObject.get("status").equals("success")
                ) {

                    Toasts.toastIconSuccess(EditDealActivity.this,"Deal Edited Successfully");
                    Intent intent = new Intent(EditDealActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "deals");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(EditDealActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(EditDealActivity.this,"Error Editing Deal. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

                }

            }
            else if(result != null && result.contains("\"accounts\":")){

                try {
                    // Create a JSONObject from the response string
                    JSONObject jsonObject = new JSONObject(result);
                    List<Account> items = new ArrayList<>();
                    List<String> accountNames = new ArrayList<>();
                    List<String> accountIds = new ArrayList<>();
                    List<String> productNames = new ArrayList<>();
                    List<String> productIds = new ArrayList<>();

                    JSONObject deal = jsonObject.getJSONObject("deal");
//                    nameEditText.setText(deal.getString("name"));
                    productEditText.setText(deal.getJSONObject("product").getString("name"));
                    amountEditText.setText(deal.getString("amount"));
                    selectedAccountId=deal.getJSONObject("account").getString("id");
                    selectedProductId=deal.getJSONObject("product").getString("id");
                    accountEditText.setText(deal.getJSONObject("account").getString("name"));
                    String statusFromDb=deal.getString("status");
//                    if(statusFromDb.equals("pending"))
                    statusFromDb = statusFromDb.substring(0, 1).toUpperCase() +
                            statusFromDb.substring(1).toLowerCase();

                    checkedStatus = Arrays.asList(array).indexOf(statusFromDb);

                    statusEditText.setText(statusFromDb);


                    JSONArray accounts = jsonObject.getJSONArray("accounts");
//                    System.out.println("Accounts length: "+accounts.length());
                    for (int i = 0; i < accounts.length(); i++) {
                        accountNames.add(accounts.getJSONObject(i).getString("name"));
                        String accountId = accounts.getJSONObject(i).getString("id");
                        accountIds.add(accountId);

                        if (accountId.equals(selectedAccountId)){
                            checkedAccount = i;
                        }

                    }


                    String[] accountNamesArray = accountNames.toArray(new String[0]);
                    String[] accountIdsArray = accountIds.toArray(new String[0]);

                    accountEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAccountDialog(view,accountNamesArray,accountIdsArray);
                        }
                    });


                    JSONArray products = jsonObject.getJSONArray("products");
//                    System.out.println("Products length: "+products.length());
                    for (int i = 0; i < products.length(); i++) {
                        productNames.add(products.getJSONObject(i).getString("name"));
                        String productId = products.getJSONObject(i).getString("id");
                        productIds.add(productId);
                        if (productId.equals(selectedProductId)){
                            checkedProduct = i;
                        }
//                        Product obj = new Product();
//                        //obj.image = drw_arr.getResourceId(i, -1);
//                        obj.name = products.getJSONObject(i).getString("name");
//                        obj.address = products.getJSONObject(i).getString("address");
//                        obj.phone = products.getJSONObject(i).getString("phone");
//                        obj.website = products.getJSONObject(i).getString("website");
//
//
//                        //obj.imageDrw = drw_arr.getDrawable(obj.image);
//                        items.add(obj);


                    }

                    String[] productNamesArray = productNames.toArray(new String[0]);
                    String[] productIdsArray = productIds.toArray(new String[0]);


                    productEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showProductDialog(view,productNamesArray,productIdsArray);
                        }
                    });

                } catch (Exception e) {
                    Toasts.toastIconError(EditDealActivity.this,"Error Loading");
                    Log.e("API Response", "JSON Parsing error: " + e.getMessage());
                }

            }
            else{

                Toasts.toastIconError(EditDealActivity.this,"Edit Deal Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Deal Error: "+result));

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
        isTaskRunning = false;
    }
}