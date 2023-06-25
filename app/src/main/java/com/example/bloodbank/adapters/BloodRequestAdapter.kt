package com.example.bloodbank.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.databinding.RequestListItemBinding
import com.example.bloodbank.models.CustomUser
import com.example.bloodbank.ui.fragments.HomeFragment

 class BloodRequestAdapter(
    private val context: HomeFragment,
    private val customUserList: ArrayList<CustomUser>
    ) : RecyclerView.Adapter<BloodRequestAdapter.BloodRequestAdapterViewHolder>() {

    val REQUEST_PHONE_CALL = 1
    inner class BloodRequestAdapterViewHolder(val binding: RequestListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BloodRequestAdapter.BloodRequestAdapterViewHolder {
       return BloodRequestAdapterViewHolder(
           RequestListItemBinding.inflate(
               LayoutInflater.from(parent.context),parent,false
           )
       )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: BloodRequestAdapter.BloodRequestAdapterViewHolder,
        position: Int
    ) {
        val customUser = customUserList[position]

        holder.binding.neededForName.text = customUser.name
        holder.binding.locationName.text = "${customUser.address}, ${customUser.state}"
        holder.binding.contactNumberText.text = customUser.phone
        holder.binding.bloodTypeText.text = customUser.bloodGroup
        holder.binding.timeAndDate.text = "${customUser.time}, ${customUser.date}"

        holder.binding.contact.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(context.requireContext(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context.requireActivity(), arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel: ${customUser.phone}")
                context.startActivity(callIntent)
            }
        }



        holder.binding.share.setOnClickListener {
            val shareText = "تطبيق بنك الدم: " +
                    "\nمطلوب ل: ${customUser.name}" +
                    "\nالموقع: ${customUser.address},${customUser.state}" +
                    "\nرقم الإتصال: ${customUser.phone}" +
                    "\nفصيلة الدم: ${customUser.bloodGroup}"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

            context.startActivity(Intent.createChooser(shareIntent,"مشاركة على: "))
        }
    }

    override fun getItemCount(): Int {
        return customUserList.size
    }


}