package com.libre.alexa.activities.oldFlow;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.libre.alexa.R;
import com.libre.alexa.R2;


import butterknife.BindView;
import butterknife.ButterKnife;

public class VodafoneDinnerTimeSetupCompleteActivity extends AppCompatActivity {

    @BindView(R2.id.ll_confirm)
    LinearLayout llConfirm;

    @BindView(R2.id.rl_cancel)
    RelativeLayout rlCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodafone_dinner_time_setup_complete);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
