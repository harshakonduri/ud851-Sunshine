package com.vedicvidya.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vedicvidya.data.model.*

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

    // CachePriority enum converters
    @TypeConverter
    fun fromCachePriority(value: CachePriority): String {
        return value.name
    }

    @TypeConverter
    fun toCachePriority(value: String): CachePriority {
        return CachePriority.valueOf(value)
    }

    // QualityTier enum converters
    @TypeConverter
    fun fromQualityTier(value: QualityTier): String {
        return value.name
    }

    @TypeConverter
    fun toQualityTier(value: String): QualityTier {
        return QualityTier.valueOf(value)
    }

    // StorageTier enum converters
    @TypeConverter
    fun fromStorageTier(value: StorageTier): String {
        return value.name
    }

    @TypeConverter
    fun toStorageTier(value: String): StorageTier {
        return StorageTier.valueOf(value)
    }

    // SummaryLevel enum converters
    @TypeConverter
    fun fromSummaryLevel(value: SummaryLevel): String {
        return value.name
    }

    @TypeConverter
    fun toSummaryLevel(value: String): SummaryLevel {
        return SummaryLevel.valueOf(value)
    }
}
