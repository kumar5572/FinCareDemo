package com.example.fincaredemo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.fincaredemo.dto.UserInfoDto
import java.util.*

class DbHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    var db1: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DB_CREATE_USER_INFO)
    }

    @Throws(SQLException::class)
    fun open(): DbHelper {
        db1 = this.writableDatabase
        return this
    }

    override fun close() {
        if (db1!!.isOpen) {
            db1!!.close()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun getUserInfoList(): ArrayList<UserInfoDto> {
        val userInfoList: ArrayList<UserInfoDto> = ArrayList<UserInfoDto>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            cursor = db.rawQuery(
                "SELECT * FROM $TABLE_USER_INFO", null
            )
            if (cursor != null) {
                if (cursor.count > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            val userInfoDto: UserInfoDto = getUserInfoDto(cursor)
                            userInfoList.add(userInfoDto)
                        } while (cursor.moveToNext())
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return userInfoList
    }

    private fun getUserInfoDto(cursor: Cursor): UserInfoDto {
        val userInfoDto = UserInfoDto()
        try {
            userInfoDto.id = cursor.getString(cursor.getColumnIndexOrThrow(USER_ID))
            userInfoDto.title = cursor.getString(cursor.getColumnIndexOrThrow(USER_TITLE))
            userInfoDto.firstName = cursor.getString(cursor.getColumnIndexOrThrow(USER_FIRST_NAME))
            userInfoDto.lastName = cursor.getString(cursor.getColumnIndexOrThrow(USER_LAST_NAME))
            userInfoDto.doorNo = cursor.getString(cursor.getColumnIndexOrThrow(USER_DOOR_NO))
            userInfoDto.streetName =
                cursor.getString(cursor.getColumnIndexOrThrow(USER_STREET_NAME))
            userInfoDto.city = cursor.getString(cursor.getColumnIndexOrThrow(USER_CITY))
            userInfoDto.state = cursor.getString(cursor.getColumnIndexOrThrow(USER_STATE))
            userInfoDto.country = cursor.getString(cursor.getColumnIndexOrThrow(USER_COUNTRY))
            userInfoDto.postcode = cursor.getString(cursor.getColumnIndexOrThrow(USER_POSTCODE))
            userInfoDto.address = cursor.getString(cursor.getColumnIndexOrThrow(USER_ADDRESS))
            userInfoDto.latitude = cursor.getString(cursor.getColumnIndexOrThrow(USER_LATITUDE))
            userInfoDto.longitude = cursor.getString(cursor.getColumnIndexOrThrow(USER_LONGITUDE))
            userInfoDto.email = cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL))
            userInfoDto.dob = cursor.getString(cursor.getColumnIndexOrThrow(USER_DOB))
            userInfoDto.phone = cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE))
            userInfoDto.cell = cursor.getString(cursor.getColumnIndexOrThrow(USER_CELL))
            userInfoDto.pictureLarge =
                cursor.getString(cursor.getColumnIndexOrThrow(USER_PICTURE))
            userInfoDto.acceptStatus =
                cursor.getInt(cursor.getColumnIndexOrThrow(USER_ACCEPT_STATUS))
            userInfoDto.declineStatus =
                cursor.getInt(cursor.getColumnIndexOrThrow(USER_DECLINE_STATUS))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return userInfoDto
    }

    fun insertDataList(userList: ArrayList<UserInfoDto>) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            for (userInfoDto in userList) {
                val contentValues = ContentValues()
                contentValues.put(USER_ID, userInfoDto.id)
                contentValues.put(USER_TITLE, userInfoDto.title)
                contentValues.put(USER_FIRST_NAME, userInfoDto.firstName)
                contentValues.put(USER_LAST_NAME, userInfoDto.lastName)
                contentValues.put(USER_DOOR_NO, userInfoDto.doorNo)
                contentValues.put(USER_STREET_NAME, userInfoDto.streetName)
                contentValues.put(USER_CITY, userInfoDto.city)
                contentValues.put(USER_STATE, userInfoDto.state)
                contentValues.put(USER_COUNTRY, userInfoDto.country)
                contentValues.put(USER_POSTCODE, userInfoDto.postcode)
                contentValues.put(USER_ADDRESS, userInfoDto.address)
                contentValues.put(USER_LATITUDE, userInfoDto.latitude)
                contentValues.put(USER_LONGITUDE, userInfoDto.longitude)
                contentValues.put(USER_EMAIL, userInfoDto.email)
                contentValues.put(USER_DOB, userInfoDto.dob)
                contentValues.put(USER_PHONE, userInfoDto.phone)
                contentValues.put(USER_CELL, userInfoDto.cell)
                contentValues.put(USER_PICTURE, userInfoDto.pictureLarge)
                contentValues.put(USER_ACCEPT_STATUS, userInfoDto.acceptStatus)
                contentValues.put(USER_DECLINE_STATUS, userInfoDto.declineStatus)

                db.insertOrThrow(TABLE_USER_INFO, null, contentValues)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun updateStatus(userInfoDto: UserInfoDto, isAccept: Boolean) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val contentValues = ContentValues()
            contentValues.put(USER_ID, userInfoDto.id)
            if (isAccept) {
                contentValues.put(USER_ACCEPT_STATUS, userInfoDto.acceptStatus)
            } else {
                contentValues.put(USER_DECLINE_STATUS, userInfoDto.declineStatus)
            }
            db.update(
                TABLE_USER_INFO, contentValues, USER_ID + " = " + "'" + userInfoDto.id + "'",
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db?.close()
        }
    }

    fun getAcceptStatus(id: String): Int {
        var acceptStatus = 0
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Log.e("database", "getTransactionFormData");
            db = readableDatabase
            cursor = db.rawQuery(
                "SELECT  * FROM $TABLE_USER_INFO WHERE $USER_ID='$id'",
                null
            )
            if (cursor != null) {
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    acceptStatus = cursor.getInt(cursor.getColumnIndexOrThrow(USER_ACCEPT_STATUS))
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return acceptStatus
    }

    fun getDeclineStatus(id: String): Int {
        var declineStatus = 0
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Log.e("database", "getTransactionFormData");
            db = readableDatabase
            cursor = db.rawQuery(
                "SELECT  * FROM $TABLE_USER_INFO WHERE $USER_ID='$id'",
                null
            )
            if (cursor != null) {
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    declineStatus = cursor.getInt(cursor.getColumnIndexOrThrow(USER_DECLINE_STATUS))
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db?.close()
        }
        return declineStatus
    }

    companion object {
        const val DB_NAME = "fincaredemo"
        const val DB_VERSION = 1
        private const val ID = BaseColumns._ID

        // TABLE LIST
        private const val TABLE_USER_INFO = "userInfo"

        // TABLE_USER_INFO
        private const val USER_ID = "userId"
        private const val USER_TITLE = "title"
        private const val USER_FIRST_NAME = "firstName"
        private const val USER_LAST_NAME = "lastName"
        private const val USER_DOOR_NO = "doorNo"
        private const val USER_STREET_NAME = "streetName"
        private const val USER_CITY = "city"
        private const val USER_STATE = "state"
        private const val USER_COUNTRY = "country"
        private const val USER_POSTCODE = "postcode"
        private const val USER_ADDRESS = "address"
        private const val USER_LATITUDE = "latitude"
        private const val USER_LONGITUDE = "longitude"
        private const val USER_EMAIL = "email"
        private const val USER_DOB = "dob"
        private const val USER_PHONE = "phone"
        private const val USER_CELL = "cell"
        private const val USER_PICTURE = "picture"
        private const val USER_ACCEPT_STATUS = "acceptStatus"
        private const val USER_DECLINE_STATUS = "declineStatus"

        //TABLE_USER_INFO
        private const val DB_CREATE_USER_INFO = ("CREATE TABLE IF NOT EXISTS "
                + TABLE_USER_INFO + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID + " TEXT,"
                + USER_TITLE + " TEXT,"
                + USER_FIRST_NAME + " TEXT,"
                + USER_LAST_NAME + " TEXT,"
                + USER_DOOR_NO + " TEXT,"
                + USER_STREET_NAME + " TEXT,"
                + USER_CITY + " TEXT,"
                + USER_STATE + " TEXT,"
                + USER_COUNTRY + " TEXT,"
                + USER_POSTCODE + " TEXT,"
                + USER_ADDRESS + " TEXT,"
                + USER_LATITUDE + " TEXT,"
                + USER_LONGITUDE + " TEXT,"
                + USER_EMAIL + " TEXT,"
                + USER_DOB + " TEXT,"
                + USER_PHONE + " TEXT,"
                + USER_CELL + " TEXT,"
                + USER_PICTURE + " TEXT,"
                + USER_ACCEPT_STATUS + " INTEGER," + USER_DECLINE_STATUS + " INTEGER);")
    }
}