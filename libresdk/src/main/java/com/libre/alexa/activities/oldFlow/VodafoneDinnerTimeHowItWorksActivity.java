package com.libre.alexa.activities.oldFlow;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.View;
import android.widget.LinearLayout;


import com.libre.alexa.R;
import com.libre.alexa.R2;
import com.libre.alexa.activities.oldFlow.VodafoneDinnerTimeMainSplashActivity;
import com.libre.alexa.luci.LSSDPNodes;

import com.libre.alexa.models.ModelStoreDeviceDetails;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VodafoneDinnerTimeHowItWorksActivity extends AppCompatActivity {
    List<ModelStoreDeviceDetails> modelStoreDeviceDetailsList = new ArrayList<>();
    Bundle bundle = new Bundle();

    @BindView(R2.id.ll_next)
    LinearLayout ll_next;

    public static final int ACTIVITYREQUESTCODE = 1;

    LSSDPNodes node;

    @BindView(R2.id.toolbar)
    View toolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodafone_how_it_works_screen);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            modelStoreDeviceDetailsList = (List<ModelStoreDeviceDetails>) bundle.getSerializable("deviceDetailsList");
            node = (LSSDPNodes) bundle.getSerializable("LSSDPNodes");
        }

        AppCompatImageView iv_vodafone_app_icon = toolbar.findViewById(R.id.iv_vodafone_app_icon);
        AppCompatImageView iv_app_settings = toolbar.findViewById(R.id.toolbar);

        AppCompatTextView tv_app_toolbar_text = toolbar.findViewById(R.id.tv_app_toolbar_text);

        iv_vodafone_app_icon.setVisibility(View.GONE);
        iv_app_settings.setVisibility(View.GONE);
        tv_app_toolbar_text.setText(R.string.family_routines);


        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VodafoneDinnerTimeHowItWorksActivity.this, VodafoneSelectDinnerTimeDevicesListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("deviceDetailsList", (Serializable) modelStoreDeviceDetailsList);
                bundle.putSerializable("LSSDPNodes", node);
                intent.putExtras(bundle);
                startActivityForResult(intent, ACTIVITYREQUESTCODE);
                finish();
                if (VodafoneDinnerTimeMainSplashActivity.vodafoneDinnerTimeMainSplashActivity != null) {
                    VodafoneDinnerTimeMainSplashActivity.vodafoneDinnerTimeMainSplashActivity.finish();
                }
                if (VodafoneSelectDinnerTimeDevicesListActivity.vodafoneSelectDinnerTimeDevicesListActivity != null) {
                    VodafoneSelectDinnerTimeDevicesListActivity.vodafoneSelectDinnerTimeDevicesListActivity.finish();
                }
            }
        });

    }

}
