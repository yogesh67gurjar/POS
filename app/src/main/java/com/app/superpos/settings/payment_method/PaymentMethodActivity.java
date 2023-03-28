package com.app.superpos.settings.payment_method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superpos.R;
import com.app.superpos.adapter.PaymentMethodAdapter;
import com.app.superpos.database.DatabaseAccess;
import com.app.superpos.utils.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PaymentMethodActivity extends BaseActivity {


    private RecyclerView recyclerView;

    ImageView imgNoProduct;
    EditText etxtSearch;

    FloatingActionButton fabAdd;
    ImageButton paypal;


    String clientId = "AWcP2CFx7BLyVcyN7OagZDrbACnZ2hnwBmTEoWwb2GjHcUgqh79VL7ml_bCdDCtGM_WSb8L-_Mcy2sBW";
    int PAYPAL_REQUEST_CODE = 295;

    public static PayPalConfiguration payPalConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        paypal = findViewById(R.id.paypal);
        payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);

        paypal.setOnClickListener(v -> {
//            getPayment();

        });

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.all_payment_method) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradiant_flotting_btn));


        recyclerView = findViewById(R.id.recycler_view);
        imgNoProduct = findViewById(R.id.image_no_product);
        etxtSearch = findViewById(R.id.etxt_customer_search);
        fabAdd = findViewById(R.id.fab_add);


        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


        recyclerView.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(PaymentMethodActivity.this);
        databaseAccess.open();

        //get data from local database
        List<HashMap<String, String>> paymentMethodData;
        paymentMethodData = databaseAccess.getPaymentMethod();

        Log.d("data", "" + paymentMethodData.size());

        if (paymentMethodData.isEmpty()) {
            Toasty.info(this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
            imgNoProduct.setImageResource(R.drawable.no_data);
        } else {


            imgNoProduct.setVisibility(View.GONE);
            PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(PaymentMethodActivity.this, paymentMethodData);

            recyclerView.setAdapter(paymentMethodAdapter);


        }

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentMethodActivity.this, AddPaymentMethodActivity.class);
                startActivity(intent);
            }
        });


        etxtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("data", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(PaymentMethodActivity.this);
                databaseAccess.open();
                //get data from local database
                List<HashMap<String, String>> searchPaymentMethodList;

                searchPaymentMethodList = databaseAccess.searchPaymentMethod(s.toString());


                if (searchPaymentMethodList.isEmpty()) {

                    recyclerView.setVisibility(View.GONE);
                    imgNoProduct.setVisibility(View.VISIBLE);
                    imgNoProduct.setImageResource(R.drawable.no_data);


                } else {


                    recyclerView.setVisibility(View.VISIBLE);
                    imgNoProduct.setVisibility(View.GONE);


                    PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(PaymentMethodActivity.this, searchPaymentMethodList);

                    recyclerView.setAdapter(paymentMethodAdapter);


                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }


        });


    }


    private void getPayment() {


        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(200)), "USD", "Shubham", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    //FOR PAYPAL
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//            PayPalConfiguration paymentConfiguration = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//            if (paymentConfiguration != null) {
//                try {
//                    String paymentDetails = paymentConfiguration.toString();
//
//                    JSONObject object = new JSONObject(paymentDetails);
//                } catch (JSONException e) {
//                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            } else if (requestCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//
//            }
//        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//            Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
//        }
//    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
