package player.andre.awareplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by andre on 24/08/16.
 */
public class MainActivity extends Activity  {


    private View powerOnButton;
    private View powerOffButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerOnButton = findViewById(R.id.main_power_on);
        powerOffButton = findViewById(R.id.main_power_off);
        statusText = (TextView) findViewById(R.id.main_status_text);

        initButtonStatus();
    }

    private void initButtonStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                ListenerService.STATUS,
                Context.MODE_PRIVATE);

        boolean isRunning = sharedPreferences.getBoolean(
                ListenerService.RUNNING_STATUS,
                false);

        setIsRunning(isRunning);
    }

    private void setIsRunning(boolean status) {
        powerOnButton.setEnabled(!status);
        powerOffButton.setEnabled(status);

        if (status) {
            statusText.setText("Status: Ligado!");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            statusText.setText("Status: Desligado!");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), ListenerService.class));
        setIsRunning(true);
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), ListenerService.class));
        setIsRunning(false);
    }
}
