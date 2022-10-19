package com.example.fincaredemo.listener

import com.example.fincaredemo.dto.UserInfoDto

interface ModuleListener {
    fun selectItem(userInfoDto: UserInfoDto,isAccept: Boolean)
}