package com.example.prajakta.mortgagecalculator;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.os.Vibrator;


public class MainActivity extends ActionBarActivity {


    int max = 100;
    int progress = (int) 5.0;
    private SeekBar s1;
    private TextView tv;   //value where seekbar;s current value is displayed
    float sbvalue;         //Seekbar value
    private EditText t;
    private String check;   //Amount Borrowed is temporarily saved here
    private EditText answer; //final answer is displayed here
    private RadioButton r1, r2, r3; //Declaring buttons
    private int LoanTerm;
    private CheckBox cb;
    private float taxes;
    private float AmountBorrowed;
    private Double MonthlyPayment;
    private float IntrestRate = 5.0f;
    private Vibrator vib;
    private RadioGroup r ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vib = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);  // To invoke Vibration in the application

        r = (RadioGroup) findViewById(R.id.radioGroup2);
        s1 = (SeekBar) findViewById(R.id.seekBar);        //to fetch seekbar and its attributes
        tv = (TextView) findViewById(R.id.textView2);     //seekbar value is displayed here
        answer = (EditText) findViewById(R.id.editText2);  //here is the final answer displayed
        t = (EditText) findViewById(R.id.editText); // user's input is taken from here
        r1 = (RadioButton) findViewById(R.id.radioButton);  //radio button to  save loan terms
        r2 = (RadioButton) findViewById(R.id.radioButton2);  //radio button to save loan terms
        r3 = (RadioButton) findViewById(R.id.radioButton3);   //radio button to save loan terms
        cb = (CheckBox) findViewById(R.id.checkBox);          //checkbox for Taxes & Insurance

        s1.setMax(max);                                     //This Sets the maximum value for  Seekbar
        s1.setProgress(progress * 10);                      //Seekbar Progress property deals with by how much the increment of next Seekbar value should be
        tv.setText(Float.toString(progress) + "%");               //Seekbar value is displayed here ,which is been converted from float to String
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {   //Method for seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float value = ((float) progress / 10);
                sbvalue = value;
                tv.setText(Float.toString(sbvalue) + "%");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {     //Method for seekbar

                IntrestRate = sbvalue;
                vib.vibrate(50);
            }
        });

       // RadioGroup r = (RadioGroup) findViewById(R.id.radioGroup2);          //RadioGroup used for loan terms

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  //ActionListener for  RadioGroup
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {    //To check which of the RadioButton is checked

                switch (checkedId) {
                    case R.id.radioButton: {
                        LoanTerm = 7 * 12;
                       // Toast.makeText(getApplicationContext(), "Loan Term :" + String.valueOf(LoanTerm / 12), Toast.LENGTH_SHORT).show();
                        vib.vibrate(50);
                    }
                    break;
                    case R.id.radioButton2: {
                        LoanTerm = 15 * 12;
                        //Toast.makeText(getApplicationContext(), "Loan Term :" + String.valueOf(LoanTerm / 12), Toast.LENGTH_SHORT).show();
                        vib.vibrate(50);
                    }
                    break;

                    case R.id.radioButton3:

                    {

                        LoanTerm = 30 * 12;
                        //Toast.makeText(getApplicationContext(), "Loan Term :" + String.valueOf(LoanTerm / 12), Toast.LENGTH_SHORT).show();
                        vib.vibrate(50);
                    }
                    break;




                }
            }
        });
        t = (EditText) findViewById(R.id.editText);
        t.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    KeyboardHiding(v);
                    vib.vibrate(50);

                }
            }
        });
        Button b = (Button) findViewById(R.id.button);


        b.setOnClickListener(new View.OnClickListener() {       //Listener for Button
                                 @Override
                                 public void onClick(View v) {

                                     check = t.getText().toString();

                                     if (check.isEmpty()) {
                                         Toast.makeText(getApplicationContext(), "Please enter Value for Amount Borrowed", Toast.LENGTH_SHORT).show();
                                         answer.setText("");

                                         vib.vibrate(50);
                                         return;
                                     } else {

                                         AmountBorrowed = Float.parseFloat(check);
                                     }
                                     if (check() == true) {

                                         Toast.makeText(getApplicationContext(), "Please select loan term in years", Toast.LENGTH_SHORT).show();
                                         answer.setText("");
                                         vib.vibrate(50);
                                         return;


                                     }


                                     float monthlyPayment = calculate_mortgage(AmountBorrowed, LoanTerm, IntrestRate, cb.isChecked());  //Method to calculate Monthly payment
                                     vib.vibrate(50);
                                     answer.setText("$" + String.format("%.2f",monthlyPayment));     //Value Returned from the method is displayed in the EditBox.


                                 }


                             }
        );


    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clear();
                return true;

            default:
                return super.onOptionsItemSelected((android.view.MenuItem) item);
        }
    }

    private void clear() {

        answer.setText(""); //here is the final answer displayed
         t.setText("");
         cb.setChecked(false);
         s1.setProgress(50);
         r.clearCheck();

    }

    private void KeyboardHiding(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE); // Checks whether Input method is Changed Focus

        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    private float calculate_mortgage(float amountBorrowed, int loanTerm, float intrestRate, boolean checked) {

        float mnthpay = 0;

        float tax = taxCalculator(amountBorrowed, checked);
        //   Toast.makeText(getApplicationContext(), "Amt :"+String.valueOf(amountBorrowed)+" Loan Term in Months:"+String.valueOf(loanTerm+" Int Rate :"+String.valueOf(intrestRate)+" Taxes :"+String.valueOf(tax)), Toast.LENGTH_SHORT).show();

        if (intrestRate == 0.0)

        {

            mnthpay = (amountBorrowed / loanTerm) + tax;

        } else {
            intrestRate = intrestRate / 1200;
            float denominator = (float) 1 / (1 + intrestRate);
            float y = (float) Math.pow(denominator, LoanTerm);


            mnthpay = (float) (amountBorrowed * ((intrestRate / (1 - y)))) + tax;


        }

        return mnthpay;

    }

    private float taxCalculator(float amountBorrowed, boolean checked) {  //Method to Calculate Tax

        float tax;
        if (checked)

        {

            tax = (float) ((amountBorrowed * 0.1) / 100);

        } else {


            tax = 0;
        }
        return tax;
    }


    public boolean check() {                             //Method to Check at least one of the RadioButton is clicked
        RadioGroup r = (RadioGroup) findViewById(R.id.radioGroup2);

        if (r.getCheckedRadioButtonId() == -1)

        {
            return true;


        } else {

            return false;

        }
    }


}


