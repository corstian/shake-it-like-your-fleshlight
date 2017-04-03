package co.boerman.chaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import co.boerman.chaker.common.HSLColor;
import co.boerman.chaker.common.Helpers;
import co.boerman.chaker.common.HueHelper;
import co.boerman.chaker.common.RGBColor;
import co.boerman.chaker.common.ShakeDetector;

public class ShakerActivity extends Activity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    Button settingsButton;

//    private SharedPreferences sharedPreferences
//            = this.getSharedPreferences("_", Context.MODE_APPEND);

    private void NavigateToSettings(String message) {
        Intent intent = new Intent(this, SettingsActivity.class);
        if (message != null) intent.putExtra("message", message);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shaker);

        settingsButton = (Button)findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavigateToSettings(null);
            }
        });

        String endpoint = Helpers.getDataFromPreferences(this, "txtEndpoint");

        if (endpoint == null) {
            // Show a nice little dialog which forces the user to the settings page.
            new AlertDialog.Builder(this)
                    .setTitle("Enter configuration")
                    .setMessage("Please enter some information so that we can control your light bulbs.")
                    .setPositiveButton("Yep let me enter the settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavigateToSettings(null);
                        }
                    })
                    .setNegativeButton("Force me to enter the settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavigateToSettings("Sorry we had to force you. Now enter the settings.");
                        }
                    }).show();
        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
//                handleShakeEvent(count);
                // ToDo: Change the light. If no configuration is available show a toast or something.
                HueHelper helper = new HueHelper(getApplicationContext());

                try {
                    // ToDo: Set the background
                    RGBColor color = Helpers.getRandomRGBColor();

                    // Set the background and text colors.
                    findViewById(R.id.activity_shaker)
                            .setBackgroundColor(Color.parseColor(Helpers.rgbColorToHexString(color)));
                    ((TextView)findViewById(R.id.text))
                            .setTextColor(Color.parseColor(Helpers.rgbColorToHexString(Helpers.getInverse(color))));

                    HSLColor hslColor = Helpers.fromRGB(color);
                    helper.setLight(true, hslColor.L, hslColor.H, hslColor.S);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * Handle some tombstoning logic and such. (Would be fun if we'd remove this
     * but it would drive people nuts because there lights would be flipping.)
     */
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
