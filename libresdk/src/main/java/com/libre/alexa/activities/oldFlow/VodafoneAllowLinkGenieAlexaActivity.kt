package com.libre.alexa.activities.oldFlow

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.libre.alexa.DeviceDiscoveryActivity
import com.libre.alexa.R
import com.libre.alexa.luci.LSSDPNodes
import com.libre.alexa.netty.LibreDeviceInteractionListner
import com.libre.alexa.netty.NettyData
import com.libre.alexa.utils.UtilClass
import kotlinx.android.synthetic.main.activity_allow_link_genie_alexa_activity.*

class VodafoneAllowLinkGenieAlexaActivity : DeviceDiscoveryActivity(),LibreDeviceInteractionListner {

    val utilClass = UtilClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allow_link_genie_alexa_activity)

        btnAllow.setOnClickListener {
            val intent = Intent(
                this@VodafoneAllowLinkGenieAlexaActivity,
                VodafoneLinkDinnerTImeWithAlexaActivity::class.java
            )
            startActivity(intent)
        }
    }


    fun showProgressLoader() {
        progressBar.visibility = View.VISIBLE
    }

    fun dismissProgressLoader() {
        progressBar.visibility = View.GONE
    }




    override fun newDeviceFound(node: LSSDPNodes?) {
        TODO("Not yet implemented")
    }

    override fun deviceGotRemoved(ipaddress: String?) {
        TODO("Not yet implemented")
    }

    override fun messageRecieved(packet: NettyData?) {
        TODO("Not yet implemented")
    }

    override fun deviceDiscoveryAfterClearingTheCacheStarted() {
        TODO("Not yet implemented")
    }

}