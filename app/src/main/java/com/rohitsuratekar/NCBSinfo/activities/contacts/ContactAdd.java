package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class ContactAdd extends BaseActivity {

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.CONTACTS_ADD;
    }

    private EditText inputName, inputNumber, inputDepartment, inputPosition;
    private TextInputLayout inputLayoutName, inputLayoutNumber, inputLayoutDepartment, inputLayoutPosition;
    int forEdit;
    int feldID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        forEdit = intent.getIntExtra("forEdit", 0);
        feldID = intent.getIntExtra("feldID", 0);
        //App name in middle

        if (forEdit == 0) {
            setTitle(getString(R.string.contact_add));
        } else {
            setTitle(getString(R.string.contact_edit));
        }


        inputLayoutName = (TextInputLayout) findViewById(R.id.contact_input_layout_name);
        inputLayoutNumber = (TextInputLayout) findViewById(R.id.contact_input_layout_number);
        inputLayoutDepartment = (TextInputLayout) findViewById(R.id.contact_input_layout_department);
        inputLayoutPosition = (TextInputLayout) findViewById(R.id.contact_input_layout_position);


        inputName = (EditText) findViewById(R.id.contact_input_name);
        inputNumber = (EditText) findViewById(R.id.contact_input_number);
        inputDepartment = (EditText) findViewById(R.id.contact_input_department);
        inputPosition = (EditText) findViewById(R.id.contact_input_position);
        Button btnSignUp = (Button) findViewById(R.id.contact_submit);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputNumber.addTextChangedListener(new MyTextWatcher(inputNumber));


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        if (forEdit == 1) {
            inputName.setText(new ContactsData(getBaseContext()).get(feldID).getName());
            inputNumber.setText(new ContactsData(getBaseContext()).get(feldID).getExtension());
            inputDepartment.setText(new ContactsData(getBaseContext()).get(feldID).getDepartment());
            inputPosition.setText(new ContactsData(getBaseContext()).get(feldID).getPosition());
            btnSignUp.setText("Change");
        }

    }


    private void submitForm() {
        if (!validateName()) {
            return;
        }
        int pseudoIncrement = 1;
        String contactName = inputName.getText().toString();
        String contactNumber = inputNumber.getText().toString();
        String contactDepartment = inputDepartment.getText().toString();
        String contactPosition = inputPosition.getText().toString();
        if (contactDepartment.length() == 0) {
            contactDepartment = "N/A";
        }
        if (contactPosition.length() == 0) {
            contactPosition = "N/A";
        }
        ContactsData db = new ContactsData(getBaseContext());

        if (forEdit == 0) {
            db.add(new ContactModel(pseudoIncrement, contactName, contactDepartment, contactPosition, contactNumber, "0"));
            Toast.makeText(getBaseContext(), contactName + " added to contact list.", Toast.LENGTH_LONG).show();
        } //zero  added to make it not favorite
        else {
            db.update(new ContactModel(feldID, contactName, contactDepartment, contactPosition, contactNumber, "0"));
            Toast.makeText(getBaseContext(), contactName + " changed!", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(ContactAdd.this, Contacts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.contact_error1));
            requestFocus(inputName);
            inputLayoutNumber.setErrorEnabled(false);
            return false;
        } else if (inputNumber.getText().toString().trim().isEmpty()) {
            inputLayoutNumber.setError(getString(R.string.contact_error2));
            requestFocus(inputNumber);
            inputLayoutName.setErrorEnabled(false);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
            inputLayoutNumber.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
