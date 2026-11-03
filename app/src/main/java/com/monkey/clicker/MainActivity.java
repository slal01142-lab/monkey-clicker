package com.monkey.clicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText xField, yField, msField;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 80, 40, 40);

        TextView info = new TextView(this);
        info.setText("1) Accessibility ON karo\n2) X, Y aur interval daalo\n3) Start dabao, phir game kholo\n4) Rokne ke liye app wapas kholo aur Stop dabao");
        info.setTextSize(16);
        root.addView(info);

        status = new TextView(this);
        status.setPadding(0, 30, 0, 30);
        root.addView(status);

        xField = makeField("X position (pixels)", "540");
        yField = makeField("Y position (pixels)", "1200");
        msField = makeField("Interval (ms)", "100");
        root.addView(xField);
        root.addView(yField);
        root.addView(msField);

        Button accBtn = new Button(this);
        accBtn.setText("Accessibility Settings kholo");
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        root.addView(accBtn);

        Button startBtn = new Button(this);
        startBtn.setText("START");
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickService.instance == null) {
                    Toast.makeText(MainActivity.this, "Pehle Accessibility ON karo", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    float x = Float.parseFloat(xField.getText().toString());
                    float y = Float.parseFloat(yField.getText().toString());
                    long ms = Long.parseLong(msField.getText().toString());
                    ClickService.instance.start(x, y, ms);
                    updateStatus();
                    Toast.makeText(MainActivity.this, "Clicking shuru. Ab game kholo", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Sahi number daalo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        root.addView(startBtn);

        Button stopBtn = new Button(this);
        stopBtn.setText("STOP");
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickService.instance != null) {
                    ClickService.instance.stop();
                }
                updateStatus();
            }
        });
        root.addView(stopBtn);

        setContentView(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private EditText makeField(String hint, String def) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setText(def);
        e.setInputType(InputType.TYPE_CLASS_NUMBER);
        return e;
    }

    private void updateStatus() {
        if (ClickService.instance == null) {
            status.setText("Status: Accessibility OFF");
        } else if (ClickService.instance.isRunning()) {
            status.setText("Status: Clicking chal rahi hai");
        } else {
            status.setText("Status: Ready (ruki hui)");
        }
    }
}
