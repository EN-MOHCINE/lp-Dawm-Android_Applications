package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private final String DEFAUT = "Calculer IMC";
    private final String MEGA_STRING = "Great job!";

    private Button envoyer;
    private Button raz;
    private EditText poids;
    private EditText taille;
    private RadioGroup group;
    private TextView result;
    private CheckBox mega;

    // Horizontal progress bar
    private ProgressBar imcProgressBar;
    // Circular progress bar + label
    private ProgressBar imcCircularProgressBar;
    private TextView imcCategoryLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to UI elements
        envoyer = findViewById(R.id.calcul);
        raz = findViewById(R.id.raz);
        taille = findViewById(R.id.taille);
        poids = findViewById(R.id.poids);
        mega = findViewById(R.id.mega);
        group = findViewById(R.id.group);
        result = findViewById(R.id.result);

        // Horizontal ProgressBar
        imcProgressBar = findViewById(R.id.imcProgressBar);

        // Circular ProgressBar and label
        imcCircularProgressBar = findViewById(R.id.imcCircularProgressBar);
        imcCategoryLabel = findViewById(R.id.imcCategoryLabel);

        // Attach listeners
        envoyer.setOnClickListener(envoyerListener);
        raz.setOnClickListener(razListener);
        mega.setOnClickListener(checkedListener);

        // Add text watchers
        taille.addTextChangedListener(textWatcher);
        poids.addTextChangedListener(textWatcher);

        // Initialize result text
        result.setText(DEFAUT);
    }

    /**
     * Watch for text changes to reset the result TextView to the default text
     * and reset both progress bars.
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            result.setText(DEFAUT);

            // Reset the horizontal progress bar
            imcProgressBar.setProgress(0);
            imcProgressBar.getProgressDrawable().setColorFilter(
                    Color.GRAY, PorterDuff.Mode.SRC_IN
            );

            // Reset the circular progress bar
            imcCircularProgressBar.setProgress(0);
            imcCircularProgressBar.getProgressDrawable().setColorFilter(
                    Color.GRAY, PorterDuff.Mode.SRC_IN
            );

            // Reset the label in the middle
            imcCategoryLabel.setText("IMC");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not used
        }
    };

    /**
     * Listener for the "Calculer" (envoyer) button.
     */
    private final View.OnClickListener envoyerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // If the checkbox is checked, show the "megaString" message
            if (mega.isChecked()) {
                result.setText(MEGA_STRING);
                return;
            }

            // Otherwise, calculate IMC
            String t = taille.getText().toString().trim();
            String p = poids.getText().toString().trim();

            // Check if the fields are empty
            if (t.isEmpty() || p.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        "Veuillez renseigner le poids et la taille.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to parse float values
            float tValue;
            float pValue;
            try {
                tValue = Float.parseFloat(t);
                pValue = Float.parseFloat(p);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this,
                        "Entrée invalide pour le poids ou la taille.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for zero or unrealistic values
            if (tValue <= 0) {
                Toast.makeText(MainActivity.this,
                        "Taille doit être > 0",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pValue <= 0) {
                Toast.makeText(MainActivity.this,
                        "Poids doit être > 0",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // If the user has chosen centimeters (radio2), convert cm to meters
            if (group.getCheckedRadioButtonId() == R.id.radio2) {
                tValue = tValue / 100.0f;
            }

            // Calculate IMC
            float imc = pValue / (tValue * tValue);

            // 1) Show IMC in the "result" TextView
            result.setText(String.format("Votre IMC est %.2f", imc));

            // 2) Clamp IMC progress to 50 (since max=50 on both bars)
            int progress = (int) Math.min(imc, 50.0f);

            // 3) Update both progress bars (horizontal and circular)
            imcProgressBar.setProgress(progress);
            imcCircularProgressBar.setProgress(progress);

            // 4) Determine IMC category and color
            String imcCategory;
            int color;

            if (imc < 18.5) {
                imcCategory = "Maigreur";
                color = Color.CYAN;
            } else if (imc < 25) {
                imcCategory = "Normal";
                color = Color.GREEN;
            } else if (imc < 30) {
                imcCategory = "Surpoids";
                color = Color.YELLOW;
            } else if (imc < 40) {
                imcCategory = "Obésité Modérée";
                color = Color.rgb(255,165,0); // Orange
            } else {
                imcCategory = "Obésité Sévère";
                color = Color.RED;
            }

            // Update the result text with category
            result.setText(String.format("Votre IMC est %.2f\n=> %s", imc, imcCategory));

            // Color for the horizontal bar
            imcProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

            // Color for the circular bar
            imcCircularProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

            // Show IMC + Category in the center, in black
            imcCategoryLabel.setTextColor(Color.BLACK);   // Optionally set here if not in XML
            imcCategoryLabel.setText(String.format("%.1f\n%s", imc, imcCategory));
        }
    };

    /**
     * Listener for the "RAZ" (raz) button to reset fields.
     */
    private final View.OnClickListener razListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            poids.getText().clear();
            taille.getText().clear();
            result.setText(DEFAUT);

            // Reset the horizontal bar
            imcProgressBar.setProgress(0);
            imcProgressBar.getProgressDrawable().setColorFilter(
                    Color.GRAY, PorterDuff.Mode.SRC_IN
            );

            // Reset the circular bar
            imcCircularProgressBar.setProgress(0);
            imcCircularProgressBar.getProgressDrawable().setColorFilter(
                    Color.GRAY, PorterDuff.Mode.SRC_IN
            );

            // Reset the label in the middle
            imcCategoryLabel.setText("IMC");
        }
    };

    /**
     * Listener for the "mega" checkbox.
     */
    private final View.OnClickListener checkedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // If the checkbox is unchecked and the result text is MEGA_STRING, revert to default
            if (!((CheckBox) v).isChecked() && MEGA_STRING.contentEquals(result.getText())) {
                result.setText(DEFAUT);
            }
        }
    };
}
