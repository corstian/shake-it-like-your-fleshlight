package co.boerman.chaker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import co.boerman.chaker.common.Helpers;

public class SettingsActivity extends Activity {

    TextView txtEndpoint;
    TextView txtUser;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtEndpoint = (TextView) findViewById(R.id.txtEndpoint);
        txtUser = (TextView) findViewById(R.id.txtUsername);
        txtCount = (TextView) findViewById(R.id.txtNumber);

        txtEndpoint.setText(Helpers.getDataFromPreferences(this, "txtEndpoint"));
        txtUser.setText(Helpers.getDataFromPreferences(this, "txtUser"));
        txtCount.setText(Helpers.getDataFromPreferences(this, "txtCount"));
    }

    @Override
    public void onBackPressed() {
        // Save the text data in the shared preferences
        Helpers.saveDataToPreferences(this, "txtEndpoint", txtEndpoint.getText().toString());
        Helpers.saveDataToPreferences(this, "txtUser", txtUser.getText().toString());
        Helpers.saveDataToPreferences(this, "txtCount", txtCount.getText().toString());

        super.onBackPressed();
    }

}
