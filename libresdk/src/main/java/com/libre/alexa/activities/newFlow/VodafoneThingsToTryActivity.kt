package com.libre.alexa.activities.newFlow

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.libre.alexa.DeviceDiscoveryActivity
import com.libre.alexa.R
import com.libre.alexa.constants.LSSDPCONST
import com.libre.alexa.constants.LSSDPCONST.LUCI_GET
import com.libre.alexa.constants.LSSDPCONST.LUCI_SET
import com.libre.alexa.constants.MIDCONST
import com.libre.alexa.luci.LSSDPNodeDB
import com.libre.alexa.luci.LSSDPNodes
import com.libre.alexa.luci.LUCIControl
import com.libre.alexa.luci.LUCIPacket
import com.libre.alexa.netty.LibreDeviceInteractionListner
import com.libre.alexa.netty.NettyData
import com.libre.alexa.util.LibreLogger
import kotlinx.android.synthetic.main.activity_vodafone_things_to_try_activity.*
import kotlinx.android.synthetic.main.toolbar_white_bg_amazon.*

class VodafoneThingsToTryActivity : DeviceDiscoveryActivity(), LibreDeviceInteractionListner {

    var bundle = Bundle()

    var node: LSSDPNodes? = null

    val TAG = VodafoneThingsToTryActivity::class.java.simpleName

    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vodafone_things_to_try_activity)

        disableNetworkChangeCallBack()

        registerForDeviceEvents(this)

        bundle = intent.extras!!
        if (bundle != null) {
            node = bundle.getSerializable("deviceDetails") as LSSDPNodes
        }

        tvDone.setOnClickListener {
            onBackPressed()
        }

        tvLogout.setOnClickListener {
            showLoader()
            sendSignOutCommandToDevice()
        }

        Rl_sign_in.setOnClickListener {
            //launch the amazon app
            launchTheApp("com.amazon.dee.app")
        }

        createNewProgressDialog()
    }

    fun showLoader() {
        progressDialog?.show()
    }

    fun dismissLoader() {
        progressDialog?.dismiss()
    }


    fun createNewProgressDialog() {
        progressDialog = ProgressDialog(this@VodafoneThingsToTryActivity)
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Please Wait...")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,
                R.anim.right_to_left_anim_transition);
    }

    fun sendSignOutCommandToDevice() {
        val luciControl = LUCIControl(node?.ip)
        luciControl.SendCommand(MIDCONST.ALEXA_COMMAND, "SIGN_OUT", LSSDPCONST.LUCI_SET)
        val mNode = LSSDPNodeDB.getInstance().getTheNodeBasedOnTheIpAddress(node?.ip)
        if (mNode != null) {
            mNode.alexaRefreshToken = ""
        }
        val readAlexaRefreshToken = "READ_AlexaRefreshToken"
        luciControl.SendCommand(208, readAlexaRefreshToken, LSSDPCONST.LUCI_GET)
        checkAlexaStatus()
    }

    fun checkAlexaStatus() {
        val luciControl = LUCIControl(node?.ip)
        luciControl.SendCommand(MIDCONST.CHECK_ALEXA_LOGIN_STATUS, "GETLOGINSTAT", LSSDPCONST.LUCI_SET)
    }

    fun launchTheApp(appPackageName: String?) {
        val intent = packageManager.getLaunchIntentForPackage(appPackageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            redirectingToPlayStore(intent, appPackageName!!)
        }
    }

    fun redirectingToPlayStore(intent: Intent, appPackageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=$appPackageName")
            startActivity(intent)
        } catch (anfe: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")
            startActivity(intent)
        }
    }

    override fun newDeviceFound(node: LSSDPNodes?) {

    }

    override fun deviceGotRemoved(ipaddress: String?) {

    }

    override fun messageRecieved(packet: NettyData?) {
        LibreLogger.d(this, "New message appeared for the device " + packet!!.getRemotedeviceIp())
        val messagePacket = LUCIPacket(packet!!.getMessage())
        when (messagePacket.command) {
            MIDCONST.CHECK_ALEXA_LOGIN_STATUS -> {
                val payload: String = messagePacket.getpayload().toString(Charsets.UTF_8)
                Log.d(TAG, "receivedSkillPayloadMessage: $payload")
                when (payload) {
                    "LOGGINGOUT" -> {
                        checkAlexaStatus()
                    }

                    "NOLOGIN" -> {
                        updateAlexaEnablementStatus(false)
                        gototSwfPairedScreen()
                    }
                }
            }
        }
    }


    fun gototSwfPairedScreen() {
        dismissLoader()
        val intent = Intent(this@VodafoneThingsToTryActivity, VodafoneDinnerTimePairedActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("deviceDetails", node)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right_anim_tranistion,
                R.anim.right_to_left_anim_transition);
    }

    fun updateAlexaEnablementStatus(status: Boolean) {
        val sharedPreferences = getSharedPreferences("Libre", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("alexaSkillEnablement", status)
        editor.apply()
    }

    override fun deviceDiscoveryAfterClearingTheCacheStarted() {

    }

}