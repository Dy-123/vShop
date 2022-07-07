package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    
    private Toolbar tToolbar;
    private EditText vName,vCity,vAddress,vCode, vNumber;
    private Button bAddress;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        tToolbar=findViewById(R.id.add_address_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        vName=findViewById(R.id.ad_name);
        vCity=findViewById(R.id.ad_city);
        vAddress=findViewById(R.id.ad_address);
        vCode=findViewById(R.id.ad_code);
        vNumber=findViewById(R.id.ad_phone);

        bAddress=findViewById(R.id.ad_add_address);

        firebaseDb=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        bAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=vName.getText().toString();
                String city=vCity.getText().toString();
                String address=vAddress.getText().toString();
                String code=vCode.getText().toString();
                String number=vNumber.getText().toString();
                String final_address="";
                if(!name.isEmpty()){
                    final_address+=name+", ";
                }
                if(!city.isEmpty()){
                    final_address+=city+", ";
                }
                if(!address.isEmpty()){
                    final_address+=address+", ";
                }
                if(!code.isEmpty()){
                    final_address+=code+", ";
                }
                if(!number.isEmpty()){
                    final_address+=number+", ";
                }
                Map<String,String> mMap=new HashMap<>();
                mMap.put("address",final_address);

                firebaseDb.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });
    }
}