package com.example.bookmyhealth.utils

import android.content.Context
import com.example.bookmyhealth.data.model.Disease
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtils {

    fun loadDiseases(context: Context): List<Disease> {
        return try {
            val inputStream = context.assets.open("diseases.json")
            val json = inputStream.bufferedReader().use { it.readText() }

            val type = object : TypeToken<List<Disease>>() {}.type
            Gson().fromJson(json, type)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}