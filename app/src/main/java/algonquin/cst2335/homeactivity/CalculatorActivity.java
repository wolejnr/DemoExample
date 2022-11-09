package algonquin.cst2335.homeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/** This is the activity that handles the calculation of monthly payments for
 * a mortgage
 *
 * @author Adewole Adewumi
 * @version 1.0
 *
 */
public class CalculatorActivity extends AppCompatActivity {

    TextView payment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Intent fromPrevious = getIntent();
        String userName = fromPrevious.getStringExtra("userID");

        TextView tvWelcome = findViewById(R.id.tvWelcomeMsg);
        tvWelcome.setText("Welcome " + userName);

        EditText loanAmount = findViewById(R.id.etLoanAmount);
        EditText numberOfYears = findViewById(R.id.etYears);
        EditText interestRate = findViewById(R.id.etInterestRate);
        payment = findViewById(R.id.tvResult);
        Button btnCalculate = findViewById(R.id.btnCalculate);


        btnCalculate.setOnClickListener(f -> {
            // Get data from all the edit texts
            Double amount = Double.parseDouble(loanAmount.getText().toString());
            Double years = Double.parseDouble(numberOfYears.getText().toString());
            Double interest = Double.parseDouble(interestRate.getText().toString());

            // Call the method
            calculateMonthlyPayments(amount, years, interest);
        });


    }

    /** This is a function that calculates the monthly mortgage payment of an individual
     *
     * @param amount Total monthly payment
     * @param years Duration available to pay off the mortgage
     * @param interest The interest rate on the mortgage
     */
    public void calculateMonthlyPayments(Double amount, Double years, Double interest) {
        // Perform computation on using the formula
        double i = (interest/100.0)/12.0;

        Double result = (amount * (i * Math.pow((1 + i), years)))/Math.pow((1+i), amount-1);

        // Display the result in the textView called tvResult
        payment.setText("Monthly payment: " + String.valueOf(result));
    }



}