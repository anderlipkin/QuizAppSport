package com.quiz.quizappsport.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.questionQuiz.quizappsport.R;
import com.quiz.quizappsport.User;
import com.quiz.quizappsport.database.UsersDbHelper;
import com.quiz.quizappsport.utils.InputValidation;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNickname, etEmail, etPassword, etAge;
    private TextView tvBackToLogin;
    private RadioGroup rgGender;
    private Button btnRegister;
    private DatePickerDialog datePickerDialog;
    private UsersDbHelper db;
    private InputValidation inputValidation;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();
        setViewActions();
        prepareDatePickerDialog();
        initObjects();
    }

    private void bindViews() {
        etNickname = (EditText) findViewById(R.id.etNickName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAge = (EditText) findViewById(R.id.etAge);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvBackToLogin = (TextView) findViewById(R.id.tvBackToLogin);
    }

    private void setViewActions() {
        btnRegister.setOnClickListener(this);
        etAge.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
    }

    private void initObjects() {
        db = UsersDbHelper.getInstance(this);
        inputValidation = new InputValidation(this);
        user = new User();
    }

    private void prepareDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etAge.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - year));
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.setMessage("When's Your Birthday?");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etAge:
                datePickerDialog.show();
                break;
            case R.id.btnRegister:
                hideSoftKeyboard(RegisterActivity.this);
                register();
                break;
            case R.id.tvBackToLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            default:

        }
    }

    private void register() {
        if (!isValidationRegister()) {
            return;
        }

        String nickname = etNickname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (!db.existUser(nickname, email)) {
            user.setNickName(nickname);
            user.setEmail(email);
            user.setPassword(etPassword.getText().toString().trim());
            user.setAge(Integer.valueOf(etAge.getText().toString().trim()));
            int idx = rgGender.indexOfChild(rgGender.findViewById(rgGender.getCheckedRadioButtonId()));
            user.setGender(((RadioButton)rgGender.getChildAt(idx)).getText().toString());

            db.addUser(user);
            displayToast(getString(R.string.success_message));
            finish();
        } else {
            displayToast(getString(R.string.error_email_exists));
        }

    }

    private boolean isValidationRegister() {
        boolean isValidation = false;
        if (!inputValidation.isEditTextFilled(etNickname, getString(R.string.error_message_name))) {
            return isValidation;
        }
        if (!inputValidation.isEditTextEmail(etEmail, getString(R.string.error_message_email))) {
            return isValidation;
        }
        if (!inputValidation.isEditTextFilled(etPassword, getString(R.string.error_message_password))) {
            return isValidation;
        }
        if (!inputValidation.isEditTextFilled(etAge, getString(R.string.error_message_age))) {
            return isValidation;
        }
        if (!inputValidation.isCheckedRadioButton(rgGender, getString(R.string.error_message_gender))) {
            return isValidation;
        }

        isValidation = true;
        return isValidation;
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
