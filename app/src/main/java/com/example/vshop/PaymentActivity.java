package com.example.vshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshop.helperClasses.Item;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private List<Item> itemsList;;
    private TextView vPrice,vOffer,vShipping,vTotal;
    private Button bPay;

    private double price,offer,shipping,totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        vPrice= findViewById(R.id.payment_price);
        vOffer= findViewById(R.id.payment_offer);
        vShipping = findViewById(R.id.payment_shipping);
        vTotal = findViewById(R.id.payment_total_amount);

        bPay= findViewById(R.id.pay_btn);

        itemsList = (ArrayList<Item>) getIntent().getSerializableExtra("itemsList");
        if(itemsList!=null &&  itemsList.size()>0){
            price= 0.0;
            for(Item item: itemsList){
                price+=item.getPrice();
            }
        }else{
            price = getIntent().getDoubleExtra("amount",0.0);
        }
        offer=0.0;
        shipping=40.0;
        totalAmount = price + shipping - offer;


        vPrice.setText( "₹ " + price + " ");
        vOffer.setText("₹ " + offer + " ");
        vShipping.setText("₹ " + shipping + " ");
        vTotal.setText( "₹ " + totalAmount + " ");

        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());
        //

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    private void startPayment(){
        Checkout checkout = new Checkout();


        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();

            // key mandatory which it will retrieve from manifest or we can put the api key here also
            options.put("name", "vShop");                                   // madatory
//            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
//            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");                                  // madatory
            options.put("amount", 100*totalAmount);  // pass amount in currency subunits   // madatory
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact","9988776655");

//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String response) {
        Toast.makeText(this, "Order Successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
    }

}