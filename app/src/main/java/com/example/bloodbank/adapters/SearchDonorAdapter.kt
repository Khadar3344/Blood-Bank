package com.example.bloodbank.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.databinding.SearchDonorItemBinding
import com.example.bloodbank.models.Donor
import com.example.bloodbank.ui.fragments.SearchDonorFragment

class SearchDonorAdapter(
    private val context: SearchDonorFragment,
    private val donorList: ArrayList<Donor>
) : RecyclerView.Adapter<SearchDonorAdapter.SearchDonorAdapterViewHolder>() {

    val REQUEST_PHONE_CALL = 1
    inner class SearchDonorAdapterViewHolder(val binding: SearchDonorItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchDonorAdapterViewHolder {
        return SearchDonorAdapterViewHolder(
            SearchDonorItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: SearchDonorAdapterViewHolder,
        position: Int
    ) {
        val donor = donorList[position]

        holder.binding.apply {
            donorName.text = donor.fullName
            contactNumberDonor.text =donor.mobile
            locationDonor.text = "${donor.address}, ${donor.state}"
            totalDonation.text = donor.totalDonate.toString()
            lastDonation.text = donor.lastDonate

            contactDonor.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(context.requireContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context.requireActivity(), arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
                } else {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: ${donor.mobile}")
                    context.startActivity(callIntent)
                }
            }

            shareDonor.setOnClickListener {
                val shareText = "تطبيق بنك الدم: " +
                        "\nإسم المتبرع: ${donor.fullName}" +
                        "\nرقم الإتصال: ${donor.mobile}" +
                        "\nالموقع: ${donor.address},${donor.state}" +
                        "\nمجموع التبرعات: ${donor.totalDonate}" +
                        "\nآخر تبرع: ${donor.lastDonate}"

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

                context.startActivity(Intent.createChooser(shareIntent,"مشاركة على: "))
            }
        }
    }

    override fun getItemCount(): Int {
        return donorList.size
    }
}