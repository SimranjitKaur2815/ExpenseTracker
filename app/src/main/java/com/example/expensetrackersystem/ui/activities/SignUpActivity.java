package com.example.expensetrackersystem.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.utils.DateHelper;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    EditText firstNameEt, lastNameEt, dobEt, passwordEt;
    Button submit;
    Date dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    public void init() {
        initElements();
        initListeners();
    }


    private void initElements() {
        firstNameEt = findViewById(R.id.firstName);
        lastNameEt = findViewById(R.id.lastName);
        dobEt = findViewById(R.id.dob);
        passwordEt = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
    }

    private void initListeners() {
        dobEt.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                Calendar dobCal = Calendar.getInstance();
                dobCal.set(Calendar.YEAR, year);
                dobCal.set(Calendar.MONTH, month);
                dobCal.set(Calendar.DAY_OF_MONTH, day);
                dob = dobCal.getTime();
                Log.e("TAG", "initListeners: "+dob );
                dobEt.setText(DateHelper.getProcessedDate(year, month, day));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMaxDate(cal.getTime().getTime());
            datePickerDialog.show();

        });
        submit.setOnClickListener(v -> {

            try {
                User user = new User("testAvatar",
                        firstNameEt.getText().toString(),
                        lastNameEt.getText().toString(),
                        AESCrypt.encrypt(passwordEt.getText().toString(), passwordEt.getText().toString()), dob);
                DbHelper.getInstance().registerUser(this, user, new UserDbListener.onAuthListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show());
                    }
                });
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                Toast.makeText(SignUpActivity.this, "Exception : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}