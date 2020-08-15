package com.libre.alexa.activities.oldFlow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.libre.alexa.R;
import com.libre.alexa.R2;
import com.libre.alexa.constants.LSSDPCONST;
import com.libre.alexa.constants.MIDCONST;
import com.libre.alexa.luci.LSSDPNodeDB;
import com.libre.alexa.luci.LSSDPNodes;
import com.libre.alexa.luci.LUCIControl;
import com.libre.alexa.luci.LUCIPacket;
import com.libre.alexa.models.ModelStoreDeviceDetails;
import com.libre.alexa.netty.BusProvider;
import com.libre.alexa.netty.NettyData;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodafoneDinnerTimeMainSplashActivity extends AppCompatActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;



    @BindView(R2.id.ll_setup)
    LinearLayout llSetup;


    String ipAddress;

    Bundle bundle = new Bundle();

    public ProgressDialog mProgressDialog;

    LSSDPNodes node;

    List<ModelStoreDeviceDetails> modelStoreDeviceDetailsList = new ArrayList<>();

    public static VodafoneDinnerTimeMainSplashActivity vodafoneDinnerTimeMainSplashActivity;

    private String envVoxReadValue;

    /**
     * Add pairing by wps button to pair ls9x to gateaway for secure api access.
     * The screen should be added only if extender is connected to ethernet mode.
     * Remove if extender is on wifi
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodafone_dinner_time_main_splash);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        vodafoneDinnerTimeMainSplashActivity = this;
        bundle = getIntent().getExtras();

        if (bundle != null) {
            node = (LSSDPNodes) bundle.getSerializable("LSSDPNodes");
            modelStoreDeviceDetailsList = (List<ModelStoreDeviceDetails>) bundle.getSerializable("deviceDetailsList");
        }

        AppCompatImageView iv_vodafone_app_icon = toolbar.findViewById(R.id.iv_vodafone_app_icon);
        AppCompatImageView iv_app_settings = toolbar.findViewById(R.id.toolbar);

        AppCompatTextView tv_app_toolbar_text = toolbar.findViewById(R.id.tv_app_toolbar_text);

        iv_vodafone_app_icon.setVisibility(View.GONE);
        iv_app_settings.setVisibility(View.GONE);
        tv_app_toolbar_text.setText(R.string.family_routines);

        if (!node.getEnvVoxReadValue().equals("true")) {
            readEnvVoxAlertOne(node.getIP());
        }

//        ivCloseActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


        mProgressDialog = new ProgressDialog(VodafoneDinnerTimeMainSplashActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

        llSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!node.getEnvVoxReadValue().equals("true")) {
                    //false/ empry
                    mProgressDialog.show();

                    readEnvVoxAlertTwo(node.getIP());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            readEnvVox(node.getIP());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    if (node.getEnvVoxReadValue() != null) {
                                        if (node.getEnvVoxReadValue().equals("true")) {

                                            gotoHowItWorksActivity();
                                        } else {
                                            Toast.makeText(VodafoneDinnerTimeMainSplashActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                                            readEnvVoxAlertOne(ipAddress);
                                        }
                                    } else {
                                        Toast.makeText(VodafoneDinnerTimeMainSplashActivity.this, "Techinical error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, 2000);
                        }
                    }, 3000);
                } else {
                    //true
                    gotoHowItWorksActivity();
                }
            }
        });

    }


    public void gotoHowItWorksActivity() {
        Intent intent = new Intent(VodafoneDinnerTimeMainSplashActivity.this, VodafoneDinnerTimeHowItWorksActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceDetailsList", (Serializable) modelStoreDeviceDetailsList);
        bundle.putSerializable("LSSDPNodes", node);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(busEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(busEventListener);
    }

    Object busEventListener = new Object() {


        @Subscribe
        public void newMessageRecieved(NettyData nettyData) {
            LUCIPacket packet = new LUCIPacket(nettyData.getMessage());
            String payloadValueString = new String(packet.getpayload());

            LSSDPNodes mNode = LSSDPNodeDB.getInstance().getTheNodeBasedOnTheIpAddress(nettyData.getRemotedeviceIp());
            node = mNode;
            //RouterCertPaired:false
//            Log.d("atul env value main splash", payloadValueString);x
            if (payloadValueString != null) {
                if (packet.getCommand() == 208 && !payloadValueString.isEmpty()) {
                    if (payloadValueString.contains("RouterCertPaired")) {
                        String[] parts = payloadValueString.split(":");
                        envVoxReadValue = parts[1];
//                        Log.d("atul env value Router splash ", envVoxReadValue);
                        mNode.setEnvVoxReadValue(envVoxReadValue);
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vodafoneDinnerTimeMainSplashActivity = null;
    }

    public void readEnvVoxAlertOne(String speakerIpaddress) {
        new LUCIControl(speakerIpaddress).SendCommand(257, "5", LSSDPCONST.LUCI_SET);
        Log.d("atul  alert one", "atul BT_CONTROLLER new device found");

    }

    public void readEnvVoxAlertTwo(String speakerIpaddress) {
        new LUCIControl(speakerIpaddress).SendCommand(257, "6", LSSDPCONST.LUCI_SET);
        Log.d("atul  alert two", "atul BT_CONTROLLER new device found");

    }

    public void readEnvVox(String speakerIpaddress) {
        new LUCIControl(speakerIpaddress).SendCommand(MIDCONST.MID_ENV_READ, "READ_RouterCertPaired", LSSDPCONST.LUCI_SET);
        Log.d("atul BT_CONTROLLER ", "atul BT_CONTROLLER new device found");

    }

}
