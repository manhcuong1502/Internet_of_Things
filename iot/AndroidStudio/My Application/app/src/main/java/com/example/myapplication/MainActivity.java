package com.example.myapplication;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;

    TextView txtTemp, txtHumi;
    ToggleButton btnLED;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemperature);
        txtHumi = findViewById(R.id.txtHumidity);
        btnLED = findViewById(R.id.btnLED);

        btnLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b){
                if(b){
                    Log.d("mqtt","Button is ON");
                    sendDataMQTT("manhcuogn/f/iot-lab.iot-led","1");
                }else{
                    Log.d("mqtt","Button is OFF");
                    sendDataMQTT("manhcuogn/f/iot-lab.iot-led","0");
                }
            }

        });

        txtTemp.setText("50" + "°C");
        txtHumi.setText("90%");

        setupScheduler();
        startMQTT();
    }

    int waiting_period = 0;
    boolean sending_message_again = false;
    String mess = "";

    private void setupScheduler(){
        Timer aTimer = new Timer();
        TimerTask scheduler = new TimerTask() {
            @Override
            public void run() {
                Log.d(tag:"mqtt", msg:"Timer is ticking...")
                if(waiting_period > 0) {
                    waiting_period--;
                    if(waiting_period == 0) {
                        sending_message_again = true;
                    }
                }
                if(sending_message_again = true){
                    sendDataMQTT(topic:"abc",mess);
                }
            }
        };
        aTimer.schedule(scheduler, delay:5000, period:1000);
    }

    private void sendDataMQTT(String topic, String value){

        waiting_period = 3;
        sending_message_again = false;
        mess = value;

        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = value.getBytes(Charset.forname("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (Exception e) {}
    }

    private void startMQTT(){
        mqttHelper = new MQTTHelper (getApplicationContext(), "123456789");
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("mqtt", "Connection is successful");
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.d("mqtt", "Received: " + message.toString());

                if(topic.contains("bbc-humi")){
                    txtTemp.setText(message.toString() + "°C");
                }
                if(topic.contains("bbc-temp")){
                    txtHumi.setText(message.toString() + "%");
                }
/*               if(topic.contains("iot-led")){
                    if(message.toString().equals("1")) btnLED.setchecked(true);
                    else btnLED.setchecked(false);
                    txtLED.setText(message.toString());
                } */
                if(message.toString().equals("abc")){
                    waiting_period = 0;
                    sending_message_again = false;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}