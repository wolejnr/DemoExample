package algonquin.cst2335.homeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/** This is the Main activity page that gets loaded when a user load the app
 *
 * @author Adewole Adewumi
 * @version 1.0
 *
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent nextPage = new Intent(MainActivity.this, CalculatorActivity.class);


        Button loginButton = findViewById(R.id.btnLogin);
        EditText userID = findViewById(R.id.etUserID);
        EditText userPass = findViewById(R.id.etPassword);


        loginButton.setOnClickListener(e -> {
            String input = userID.getText().toString();
            if(input != null) {
                nextPage.putExtra("userID", input);
            }

            startActivity(nextPage);
        });
    }
}