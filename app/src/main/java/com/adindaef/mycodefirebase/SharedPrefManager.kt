package com.adindaef.mycodefirebase

import android.content.SharedPreferences

class SharedPrefManager {
    companion object {
        val SP_MAHASISWA_APP = "spMahasiswaApp"

        val SP_SUDAH_LOGIN = "spSudahLogin"

        lateinit var sp: SharedPreferences
        lateinit var spEditor: SharedPreferences.Editor
    }


    fun saveSPBoolean(keySP: String, value: Boolean) {
        spEditor.putBoolean(keySP, value)
        spEditor.commit()
    }


    fun getSPSudahLogin(): Boolean? {
        return sp.getBoolean(SP_SUDAH_LOGIN, false)
    }
}