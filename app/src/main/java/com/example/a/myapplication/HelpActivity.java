package com.example.a.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {
    EditText et_type,et_message;
    Switch loc_switch;
    HelpUtils mHelpUtils;
    RadioGroup radioGroup;
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mHelpUtils=HelpUtils.getInstance(this);

//        et_type=(EditText)findViewById(R.id.et_type);
        et_message=(EditText)findViewById(R.id.et_message);
        radioGroup= (RadioGroup) findViewById(R.id.radio_group);
        loc_switch=(Switch)findViewById(R.id.sw);

    }

    public void checkedButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton add = findViewById(R.id.radio_addproblem);
        radioButton=findViewById(radioId);
        if(radioButton == add){
            EditText e = findViewById(R.id.edit_text_add_problem);
            e.setVisibility(View.VISIBLE);
        }
    }


    public void searchForHelp(View view){

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);

        final String helpType ;
        if(radioId == R.id.radio_addproblem){
            EditText et=(EditText)findViewById(R.id.edit_text_add_problem);
            helpType = et.getText().toString().trim();
        }
        else{
            helpType = radioButton.getText().toString().trim();
        }

        Boolean loc=loc_switch.isChecked();
        String location=(loc)?"fixed":"moving";
        String message = et_message.getText().toString() != null ? et_message.getText().toString():"";
        mHelpUtils.help(helpType,message,location);
        Toast.makeText(this,"Help sent,Have a nice day ",Toast.LENGTH_SHORT).show();
        finish();

//        String type=et_type.getText().toString();
//        String message=et_message.getText().toString();
//
//        Boolean loc=loc_switch.isChecked();
//        String location=(loc)?"fixed":"moving";
//        mHelpUtils.help(type,message,location);
//        Toast.makeText(this,"Help sent,Have a nice day ",Toast.LENGTH_SHORT).show();
//        finish();
    }
}
