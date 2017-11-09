package com.quiz.quizappsport.utils;


import android.content.Context;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isEditTextFilled(EditText editText, String message) {
        String value = editText.getText().toString().trim();

        if (value.isEmpty()) {
            displayToast(message);
            return false;
        }
        return true;
    }

    public boolean isEditTextEmail(EditText editText, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            displayToast(message);
            return false;
        }
        return true;
    }

    public boolean isCheckedRadioButton(RadioGroup radioGroup, String message) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            displayToast(message);
            return false;
        }
        return true;
    }

    private void displayToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }
}
