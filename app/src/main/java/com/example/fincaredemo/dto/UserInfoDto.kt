package com.example.fincaredemo.dto

import java.io.Serializable

class UserInfoDto : Serializable {

    var id: String = ""
    var title: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var gender: String = ""
    var doorNo: String = ""
    var streetName: String = ""
    var city: String = ""
    var state: String = ""
    var country: String = ""
    var postcode: String = ""
    var address: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var email: String = ""
    var dob: String = ""
    var phone: String = ""
    var cell: String = ""
    var acceptStatus: Int = 0
    var declineStatus: Int = 0
    var pictureLarge: String = ""
    var pictureMedium: String = ""
    var pictureThumbnail: String = ""

}