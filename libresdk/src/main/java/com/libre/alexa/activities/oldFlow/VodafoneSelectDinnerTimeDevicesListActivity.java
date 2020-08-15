package com.libre.alexa.activities.oldFlow;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;


import com.libre.alexa.R;
import com.libre.alexa.R2;
import com.libre.alexa.activities.oldFlow.VodafoneDinnerTimeMainSplashActivity;
import com.libre.alexa.activities.oldFlow.VodafoneDinnerTimeSetupCompleteActivity;
import com.libre.alexa.adapter.ShowDevicesAdapter;
import com.libre.alexa.constants.LSSDPCONST;
import com.libre.alexa.luci.LSSDPNodes;
import com.libre.alexa.luci.LUCIControl;

import com.libre.alexa.models.ModelStoreDeviceDetails;
import com.libre.alexa.utils.interfaces.AddRemovedCheckedDevicesInterface;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VodafoneSelectDinnerTimeDevicesListActivity extends AppCompatActivity implements AddRemovedCheckedDevicesInterface {

    @BindView(R2.id.show_device_recycler_view)
    RecyclerView showDeviceRecyclerView;

    @BindView(R2.id.ll_done)
    LinearLayout ll_done;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;

    ShowDevicesAdapter showDevicesAdapter;

    List<ModelStoreDeviceDetails> modelStoreDeviceDetailsList = new ArrayList<>();

    List<ModelStoreDeviceDetails> modelStoreDeviceDetailsListForPostApi = new ArrayList<>();

    Bundle bundle;

    JSONArray jsonArray;


    LSSDPNodes node;

    public static VodafoneSelectDinnerTimeDevicesListActivity vodafoneSelectDinnerTimeDevicesListActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_devices_for_dinner_time);
        ButterKnife.bind(this);
        vodafoneSelectDinnerTimeDevicesListActivity = this;
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            modelStoreDeviceDetailsList = (List<ModelStoreDeviceDetails>) bundle.getSerializable("deviceDetailsList");
            node = (LSSDPNodes) bundle.getSerializable("LSSDPNodes");
        }


        AppCompatImageView iv_app_settings = toolbar.findViewById(R.id.toolbar);



        linearLayoutManager = new LinearLayoutManager(VodafoneSelectDinnerTimeDevicesListActivity.this);
        showDeviceRecyclerView.setLayoutManager(linearLayoutManager);

        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Collections.sort(modelStoreDeviceDetailsList, new Comparator<ModelStoreDeviceDetails>() {

            @SuppressLint("NewApi")
            @Override
            public int compare(ModelStoreDeviceDetails modelStoreDeviceDetails1, ModelStoreDeviceDetails modelStoreDeviceDetails2) {
                return Boolean.compare(modelStoreDeviceDetails2.isBlackListed, modelStoreDeviceDetails1.isBlackListed);
            }
        });

        setShowDevicesAdapter();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ll_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSucessFullMsg();
            }
        });


        iv_app_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VodafoneSelectDinnerTimeDevicesListActivity.this, VodafoneDinnerTimeMainSplashActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("LSSDPNodes", (Serializable) node);
                bundle.putSerializable("deviceDetailsList", (Serializable) modelStoreDeviceDetailsList);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vodafoneSelectDinnerTimeDevicesListActivity = null;
    }

    public void showSucessFullMsg() {
        //Are you sure save the changes
        if (!isFinishing()) {
            new AlertDialog.Builder(VodafoneSelectDinnerTimeDevicesListActivity.this)
                    .setTitle("")
                    .setMessage(VodafoneSelectDinnerTimeDevicesListActivity.this.getResources().getString(R.string.successfulSentDeviceChanges))

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            makeJsonForPostingTheDevice();


                        }
                    }).create().show();

        }
    }


    public void makeJsonForPostingTheDevice() {
        jsonArray = new JSONArray();
        try {
            for (ModelStoreDeviceDetails modelStoreDeviceDetails : modelStoreDeviceDetailsListForPostApi) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("isActive", modelStoreDeviceDetails.isActive);
                jsonObject.put("isBlackListed", modelStoreDeviceDetails.isBlackListed);
                jsonObject.put("macid", modelStoreDeviceDetails.macId);
                jsonObject.put("name", modelStoreDeviceDetails.name);

                jsonArray.put(jsonObject);
            }

            JSONObject mainObject = new JSONObject();
            mainObject.put("deviceList", jsonArray);

            String jsonData = "2::" + mainObject;



            writeVoxControlValue(node.getIP(), jsonData);



            Intent intent = new Intent(VodafoneSelectDinnerTimeDevicesListActivity.this, VodafoneDinnerTimeSetupCompleteActivity.class);
            startActivity(intent);
            VodafoneSelectDinnerTimeDevicesListActivity.this.finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void writeVoxControlValue(String ip, String finalJsonData) {
        new LUCIControl(ip).SendCommand(257, finalJsonData, LSSDPCONST.LUCI_SET);
    }


    public void setShowDevicesAdapter() {

//        dividerItemDecoration = new DividerItemDecoration(showDeviceRecyclerView.getContext());
//        showDeviceRecyclerView.addItemDecoration(dividerItemDecoration);


        showDevicesAdapter = new ShowDevicesAdapter(VodafoneSelectDinnerTimeDevicesListActivity.this, modelStoreDeviceDetailsList);
        showDeviceRecyclerView.setAdapter(showDevicesAdapter);
        showDevicesAdapter.setAddRemovedCheckedDevicesInterface(this);

    }


    @Override
    public void addCheckedDevice(ModelStoreDeviceDetails modelStoreDeviceDetails) {
        modelStoreDeviceDetailsListForPostApi.add(modelStoreDeviceDetails);
    }

    @Override
    public void removeUnCheckedDevice(String macId) {
        for (Iterator<ModelStoreDeviceDetails> iterator = modelStoreDeviceDetailsListForPostApi.iterator(); iterator.hasNext(); ) {
            if (iterator.next().macId.equals(macId)) {
                iterator.remove();
            }
        }
    }
}
