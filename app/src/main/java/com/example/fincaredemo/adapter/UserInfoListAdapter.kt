package com.example.fincaredemo.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fincaredemo.R
import com.example.fincaredemo.databinding.UserInfoListItemTmplBinding
import com.example.fincaredemo.dto.UserInfoDto
import com.example.fincaredemo.listener.ModuleListener
import com.squareup.picasso.Picasso

import java.util.*

class UserInfoListAdapter(
    private val context: Context,
    private val dataList: ArrayList<UserInfoDto>,
    private val moduleListener: ModuleListener
) : RecyclerView.Adapter<UserInfoListAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserInfoListItemTmplBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(dataList[position], position)

    inner class ViewHolder(val binding: UserInfoListItemTmplBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rowItem: UserInfoDto, position: Int) {
            try {
                if (rowItem.pictureLarge.isNotEmpty()) {
                    Picasso.with(context).load(Uri.parse(rowItem.pictureLarge))
                        .placeholder(R.drawable.no_media)
                        .error(R.drawable.no_media)
                        .into(binding.ivProfile)
                }
                var name = ""
                if (rowItem.title.isNotEmpty()) {
                    name += rowItem.title.plus(" ")
                }
                if (name.isNotEmpty() && rowItem.firstName.isNotEmpty()) {
                    name += rowItem.firstName.plus(" ")
                }
                if (name.isNotEmpty() && rowItem.lastName.isNotEmpty()) {
                    name += rowItem.lastName
                }
                if (name.isNotEmpty()) {
                    binding.tvName.text = name
                } else {
                    binding.tvName.text = "N/A"
                }
                if (rowItem.dob.isNotEmpty()) {
                    binding.tvDob.text = rowItem.dob
                } else {
                    binding.tvDob.text = "N/A"
                }

                var address = ""
                if (rowItem.city.isNotEmpty()) {
                    address += rowItem.city
                }
                if (rowItem.state.isNotEmpty()) {
                    if (address.isNotEmpty()) {
                        address += ", "
                    }
                    address += rowItem.state
                }
                if (rowItem.country.isNotEmpty()) {
                    if (address.isNotEmpty()) {
                        address += ", "
                    }
                    address += rowItem.country
                }
                if (address.isNotEmpty()) {
                    binding.tvAddress.text = address
                } else {
                    binding.tvAddress.text = "N/A"
                }

                if (rowItem.acceptStatus == 1 || rowItem.declineStatus == 1) {
                    binding.lnButton.visibility = View.GONE
                    binding.tvStatus.visibility = View.VISIBLE
                    if (rowItem.acceptStatus == 1) {
                        binding.tvStatus.text = context.getString(R.string.member_accepted)
                    }
                    if (rowItem.declineStatus == 1) {
                        binding.tvStatus.text = context.getString(R.string.member_declined)
                    }
                } else {
                    binding.lnButton.visibility = View.VISIBLE
                    binding.tvStatus.visibility = View.GONE
                }

                binding.btnAccept.setOnClickListener {
                    val userInfoDto = dataList[position]
                    userInfoDto.acceptStatus = 1
                    moduleListener.selectItem(userInfoDto, true)
                }

                binding.btnDecline.setOnClickListener {
                    val userInfoDto = dataList[position]
                    userInfoDto.declineStatus = 1
                    moduleListener.selectItem(userInfoDto, false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}