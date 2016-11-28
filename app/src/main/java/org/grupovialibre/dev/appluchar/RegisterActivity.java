package org.grupovialibre.dev.appluchar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUserNameField;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mEmailField = (EditText) findViewById(R.id.registerEmailField);
        mPasswordField = (EditText) findViewById(R.id.registerPasswordField);
        mUserNameField = (EditText) findViewById(R.id.registerUserNameText);
        mRegisterButton = (Button) findViewById(R.id.registerButton);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private  void registerUser(){
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        final String username = mUserNameField.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();

            return;
        }

        //if validations are ok
        progressDialog.setMessage("Registering ...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("username").setValue(username);
                            progressDialog.dismiss();

                            Toast.makeText(RegisterActivity.this,R.string.register_succesfull,Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(RegisterActivity.this,AccountActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this,R.string.register_failed,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void checkUserExists(){
        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){

                }else{
                    Toast.makeText(RegisterActivity.this,R.string.register_failed,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
