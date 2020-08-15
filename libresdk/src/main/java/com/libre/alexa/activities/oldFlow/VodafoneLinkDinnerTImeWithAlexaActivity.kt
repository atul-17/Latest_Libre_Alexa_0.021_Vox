package com.libre.alexa.activities.oldFlow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libre.alexa.R
import com.libre.alexa.activities.newFlow.VodafoneAlexaSkillEnablementSucessActivity
import kotlinx.android.synthetic.main.activity_vodafone_link_dinner_time.*


class VodafoneLinkDinnerTImeWithAlexaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vodafone_link_dinner_time)

        btnLink.setOnClickListener {
            val intent = Intent(this@VodafoneLinkDinnerTImeWithAlexaActivity, VodafoneAlexaSkillEnablementSucessActivity::class.java)
            startActivity(intent)
        }
    }
}