package com.example.firstapplication;
import android.util.Log; // Add this import at the top of your file
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText poidsInput, tailleInput;
    private RadioGroup group;
    private CheckBox megaCheckbox;
    private TextView resultText, motivationalText, circularProgressText;
    private ProgressBar bmiProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_calculator); // Link the new layout

        // Find views
        poidsInput = findViewById(R.id.poids);
        tailleInput = findViewById(R.id.taille);
        group = findViewById(R.id.group);
        megaCheckbox = findViewById(R.id.mega);
        resultText = findViewById(R.id.result);
        bmiProgressBar = findViewById(R.id.bmiProgressBar);
        motivationalText = findViewById(R.id.motivationalText);  // Initialize this TextView
        circularProgressText = findViewById(R.id.circularProgressText); // Initialize this TextView
        Button calculButton = findViewById(R.id.calcul);
        Button razButton = findViewById(R.id.raz);

        // Set button click listeners
        calculButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Button clicked");
                calculateBMI();
            }
        });

        razButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    private void calculateBMI() {
        String poidsStr = poidsInput.getText().toString();
        String tailleStr = tailleInput.getText().toString();

        // Ensure inputs are not empty
        if (poidsStr.isEmpty() || tailleStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Poids or Taille is empty");
            return;
        }

        try {
            double poids = Double.parseDouble(poidsStr);
            double taille = Double.parseDouble(tailleStr);

            // Check the selected radio button
            int selectedId = group.getCheckedRadioButtonId();
            if (selectedId == R.id.radio2) { // Centimetre
                taille /= 100; // Convert cm to meters
            }

            if (taille == 0) {
                Toast.makeText(this, "La taille ne peut pas être zéro", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Height cannot be zero");
                return;
            }

            double bmi = poids / (taille * taille);

            // Calculate progress based on BMI
            int progress = (int) ((bmi / 40.0) * 100); // Assuming 40 is the max BMI

            // Update CircularProgressBar
            CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
            circularProgressBar.setProgress(progress);

            // Update motivational text based on BMI range
            if (bmi < 18.5) {
                motivationalText.setText("You're underweight. Time to eat healthy!");
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                motivationalText.setText("You're in the normal range. Great job!");
            } else if (bmi >= 25 && bmi <= 29.9) {
                motivationalText.setText("You're overweight. Consider some fitness!");
            } else {
                motivationalText.setText("Obesity alert! Health is wealth.");
            }

            // Update result text
            if (megaCheckbox.isChecked()) {
                resultText.setText("Votre IMC est excellent !");
            } else {
                resultText.setText(String.format("Votre IMC est: %.2f", bmi));
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error parsing input: " + e.getMessage());
        }
    }

    private void resetFields() {
        poidsInput.setText("");
        tailleInput.setText("");
        resultText.setText("");
        motivationalText.setText("");
        bmiProgressBar.setProgress(0);
        circularProgressText.setText(""); // Reset percentage
        group.check(R.id.radio2); // Default to Centimetre
        megaCheckbox.setChecked(false);
    }
}
