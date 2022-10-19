package com.example.fincaredemo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fincaredemo.adapter.UserInfoListAdapter
import com.example.fincaredemo.databinding.ActivityMainBinding
import com.example.fincaredemo.db.DbHelper
import com.example.fincaredemo.dto.UserInfoDto
import com.example.fincaredemo.listener.ModuleListener
import com.example.fincaredemo.utils.AppUtils
import com.example.fincaredemo.viewmodel.UserInfoViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ModuleListener {

    private val TAG: String = MainActivity::class.java.getSimpleName()
    private val context: Context = this@MainActivity

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var dbHelper: DbHelper
    private lateinit var userInfoViewModel: UserInfoViewModel
    private var userInfoList: ArrayList<UserInfoDto>? = null
    private lateinit var userInfoListAdapter: UserInfoListAdapter

    private val serverDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    private val appDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    val count = 10

    private var serviceFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClassReference()
        initToolbar()
        checkForLocalData()
    }

    private fun initToolbar() {
        try {
            binding.toolbar.toolbar.title = "User List"
            setSupportActionBar(binding.toolbar.toolbar)
            assert(supportActionBar != null)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
            binding.toolbar.toolbar.setNavigationOnClickListener({ onBackPressed() })
        } catch (bug: Exception) {
            bug.printStackTrace()
        }
    }

    private fun initClassReference() {
        try {
            dbHelper = DbHelper(context)
            userInfoViewModel = ViewModelProvider(this)[UserInfoViewModel::class.java]

            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.lvUserInfo.layoutManager = linearLayoutManager
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkForLocalData() {
        try {
            if (userInfoList != null) {
                userInfoList!!.clear()
            }
            userInfoList = dbHelper.getUserInfoList()

            if (userInfoList!!.isNotEmpty()) {
                populateList()
                if (!serviceFlag) {
                    getUserList()
                }
            } else {
                getUserList()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUserList() {
        try {
            if (AppUtils.isConnectedToInternet(context)) {
                serviceFlag = true
                showProgressBar()
                val params: MutableMap<String, String> = HashMap()
                params["results"] = count.toString()
                userInfoViewModel.getUserList(params).observe(this) { jsonObject1 ->
                    if (jsonObject1 != null) {
                        dismissProgressBar()
                        parseUserList(jsonObject1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseUserList(obj: JSONObject) {
        try {
            if (null != obj) {
                if (obj.has("results") && !obj.isNull("results")) {
                    val resultsArray = obj.getJSONArray("results")
                    for (i in 0 until resultsArray.length()) {
                        val resultObj = resultsArray.getJSONObject(i)
                        val userInfoDto = UserInfoDto()

                        if (resultObj.has("gender") && !resultObj.isNull("gender")) {
                            userInfoDto.gender = resultObj.getString("gender")
                        }

                        if (resultObj.has("name") && !resultObj.isNull("name")) {
                            val nameObj = resultObj.getJSONObject("name")
                            if (nameObj.has("title") && !nameObj.isNull("title")) {
                                userInfoDto.title = nameObj.getString("title")
                            }
                            if (nameObj.has("first") && !nameObj.isNull("first")) {
                                userInfoDto.firstName = nameObj.getString("first")
                            }
                            if (nameObj.has("last") && !nameObj.isNull("last")) {
                                userInfoDto.lastName = nameObj.getString("last")
                            }
                        }

                        if (resultObj.has("location") && !resultObj.isNull("location")) {
                            var address = ""
                            val locationObj = resultObj.getJSONObject("location")
                            if (locationObj.has("street") && !locationObj.isNull("street")) {
                                val streetObj = locationObj.getJSONObject("street")

                                if (streetObj.has("number") && !streetObj.isNull("number")) {
                                    address += streetObj.getString("number")
                                    userInfoDto.doorNo = streetObj.getString("number")
                                }

                                if (streetObj.has("name") && !streetObj.isNull("name")) {
                                    if (address.isNotEmpty()) {
                                        address += ", "
                                    }
                                    address += streetObj.getString("name")
                                    userInfoDto.streetName = streetObj.getString("name")
                                }
                            }

                            if (locationObj.has("city") && !locationObj.isNull("city")) {
                                if (address.isNotEmpty()) {
                                    address += ", "
                                }
                                address += locationObj.getString("city")
                                userInfoDto.city = locationObj.getString("city")
                            }

                            if (locationObj.has("state") && !locationObj.isNull("state")) {
                                if (address.isNotEmpty()) {
                                    address += ", "
                                }
                                address += locationObj.getString("state")
                                userInfoDto.state = locationObj.getString("state")
                            }

                            if (locationObj.has("country") && !locationObj.isNull("country")) {
                                if (address.isNotEmpty()) {
                                    address += ", "
                                }
                                address += locationObj.getString("country")
                                userInfoDto.country = locationObj.getString("country")
                            }

                            if (locationObj.has("postcode") && !locationObj.isNull("postcode")) {
                                if (address.isNotEmpty()) {
                                    address += ", "
                                }
                                address += locationObj.getString("postcode")
                                userInfoDto.postcode = locationObj.getString("postcode")
                            }
                            userInfoDto.address = address

                            if (locationObj.has("coordinates") && !locationObj.isNull("coordinates")) {
                                val coordinatesObj = locationObj.getJSONObject("coordinates")

                                if (coordinatesObj.has("latitude") && !coordinatesObj.isNull("latitude")) {
                                    userInfoDto.latitude = coordinatesObj.getString("latitude")
                                }

                                if (coordinatesObj.has("longitude") && !coordinatesObj.isNull("longitude")) {
                                    userInfoDto.longitude = coordinatesObj.getString("longitude")
                                }
                            }

                        }

                        if (resultObj.has("email") && !resultObj.isNull("email")) {
                            userInfoDto.email = resultObj.getString("email")
                        }

                        if (resultObj.has("dob") && !resultObj.isNull("dob")) {
                            val dobObj = resultObj.getJSONObject("dob")
                            if (dobObj.has("age") && !dobObj.isNull("age")) {
                                userInfoDto.dob = dobObj.getString("age")
                            }
                        }

                        if (resultObj.has("phone") && !resultObj.isNull("phone")) {
                            userInfoDto.phone = resultObj.getString("phone")
                        }

                        if (resultObj.has("cell") && !resultObj.isNull("cell")) {
                            userInfoDto.cell = resultObj.getString("cell")
                        }

                        if (resultObj.has("id") && !resultObj.isNull("id")) {
                            val idObj = resultObj.getJSONObject("id")
                            if (idObj.has("value") && !idObj.isNull("value")) {
                                userInfoDto.id = idObj.getString("value")
                            }
                        }

                        userInfoDto.acceptStatus = dbHelper.getAcceptStatus(userInfoDto.id)
                        userInfoDto.declineStatus = dbHelper.getDeclineStatus(userInfoDto.id)

                        if (resultObj.has("picture") && !resultObj.isNull("picture")) {
                            val pictureObj = resultObj.getJSONObject("picture")
                            if (pictureObj.has("large") && !pictureObj.isNull("large")) {
                                userInfoDto.pictureLarge = pictureObj.getString("large")
                            }
                            if (pictureObj.has("medium") && !pictureObj.isNull("medium")) {
                                userInfoDto.pictureMedium = pictureObj.getString("medium")
                            }
                            if (pictureObj.has("thumbnail") && !pictureObj.isNull("thumbnail")) {
                                userInfoDto.pictureThumbnail = pictureObj.getString("thumbnail")
                            }
                        }

                        userInfoList!!.add(userInfoDto)

                    }
                }
            }
            populateList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateList() {
        try {
            if (userInfoList!!.isNotEmpty()) {
                dbHelper.insertDataList(userInfoList!!)
                binding.lvUserInfo.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                userInfoListAdapter = UserInfoListAdapter(
                    context,
                    userInfoList!!,
                    this@MainActivity
                )
                binding.lvUserInfo.adapter = userInfoListAdapter
            } else {
                binding.lvUserInfo.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showProgressBar() {
        try {
            binding.lnProgressbar.root.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dismissProgressBar() {
        try {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            binding.lnProgressbar.root.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun selectItem(userInfoDto: UserInfoDto, isAccept: Boolean) {
        try {
            dbHelper.updateStatus(userInfoDto, isAccept)
            checkForLocalData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}