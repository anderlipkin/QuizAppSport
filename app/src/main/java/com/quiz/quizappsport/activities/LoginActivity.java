package com.quiz.quizappsport.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.questionQuiz.quizappsport.R;
import com.quiz.quizappsport.Session;
import com.quiz.quizappsport.database.UsersDbHelper;
import com.quiz.quizappsport.utils.InputValidation;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvLinkRegister;
    private UsersDbHelper db;
    private InputValidation inputValidation;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
        setViewActions();
        initObjects();
    }

    private void bindViews() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvLinkRegister = (TextView) findViewById(R.id.tvRegister);
    }

    private void setViewActions() {
        btnLogin.setOnClickListener(this);
        tvLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        db = UsersDbHelper.getInstance(this);
        inputValidation = new InputValidation(getApplicationContext());
        session = new Session(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                hideSoftKeyboard(LoginActivity.this);
                login();
                break;
            case R.id.tvRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void login() {
        if (!isValidationLogin())
            return;

        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        int idUser = db.getIdUser(email, pass);
        if (idUser != -1) {
            session.createLoginSession(idUser);
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            //mainIntent.putExtra();
            startActivity(mainIntent);
            finish();
        } else {
            displayToast(getString(R.string.error_valid_email_password));
        }
    }

    private boolean isValidationLogin() {
        boolean isValidation = false;
        if (!inputValidation.isEditTextEmail(etEmail, getString(R.string.error_message_email))) {
            return isValidation;
        }
        if (!inputValidation.isEditTextFilled(etPassword, getString(R.string.error_message_password))) {
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
