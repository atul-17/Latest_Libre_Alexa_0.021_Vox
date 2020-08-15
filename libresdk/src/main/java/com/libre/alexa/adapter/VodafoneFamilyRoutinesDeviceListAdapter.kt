package com.libre.alexa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.libre.alexa.R
import kotlinx.android.synthetic.main.adapter_select_dinner_time_devices.view.*

class VodafoneFamilyRoutinesDeviceListAdapter(val context : Context,val deviceList: MutableList<String>) : RecyclerView.Adapter<VodafoneFamilyRoutinesDeviceListAdapter.VodafoneFamilyRountinesDevicesHolder>()  {

    inner class  VodafoneFamilyRountinesDevicesHolder(itemView: View) : RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VodafoneFamilyRountinesDevicesHolder {
        return VodafoneFamilyRountinesDevicesHolder(LayoutInflater.from(context).inflate(R.layout.adapter_select_dinner_time_devices,parent,false))
    }

    override fun getItemCount(): Int {
      return deviceList.size
    }

    override fun onBindViewHolder(holder: VodafoneFamilyRountinesDevicesHolder, position: Int) {
        when {
            deviceList[position]=="Mobile" -> {
                holder.itemView.ivDeviceType.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_mobile))
                holder.itemView.tvDeviceType.text = "Mobile Phone"
                holder.itemView.tvDeviceName.text = "iPhone"
            }
            deviceList[position]=="Tv" -> {
                holder.itemView.ivDeviceType.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_tv))
                holder.itemView.tvDeviceType.text = "Tv"
                holder.itemView.tvDeviceName.text = "Samsung Tv"
            }
            deviceList[position]=="Tablet" -> {
                holder.itemView.ivDeviceType.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_tablet))
                holder.itemView.tvDeviceType.text = "Tablet"
                holder.itemView.tvDeviceName.text = "iPad"
            }
            deviceList[position]=="Laptop" -> {
                holder.itemView.ivDeviceType.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_laptop))
                holder.itemView.tvDeviceType.text = "Laptop"
                holder.itemView.tvDeviceName.text = "MacbookPro"
            }
        }
    }

}