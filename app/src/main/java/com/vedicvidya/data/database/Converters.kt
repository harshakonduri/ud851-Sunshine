package com.vedicvidya.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vedicvidya.data.model.SourceCitation

/**
 * Type converters for Room database
 */
class Converters {
    private val gson = Gson()

    // List<String> converters
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, type)
        }
    }

    // List<SourceCitation> converters
    @TypeConverter
    fun fromSourceCitationList(value: List<SourceCitation>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSourceCitationList(value: String?): List<SourceCitation>? {
        return value?.let {
            val type = object : TypeToken<List<SourceCitation>>() {}.type
            gson.fromJson(it, type)
        }
    }
}
