package dev.minimul.toolkit.arch.data.store

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface IKeyValueStore {
    fun getStringOrNull(key: String): String?
    fun getBooleanOrNull(key: String): Boolean?
    fun getLongOrNull(key: String): Long?
    fun getIntOrNull(key: String): Int?
    fun getFloatOrNull(key: String): Float?
    fun getDoubleOrNull(key: String): Double?

    fun setString(key: String, value: String)
    fun setBoolean(key: String, value: Boolean)
    fun setLong(key: String, value: Long)
    fun setInt(key: String, value: Int)
    fun setFloat(key: String, value: Float)
    fun setDouble(key: String, value: Double)

    fun remove(key: String)
}

class KeyValueStore(
    private val settings: Settings
) : IKeyValueStore {
    override fun getStringOrNull(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        return settings.getBooleanOrNull(key)
    }

    override fun getLongOrNull(key: String): Long? {
        return settings.getLongOrNull(key)
    }

    override fun getIntOrNull(key: String): Int? {
        return settings.getIntOrNull(key)
    }

    override fun getFloatOrNull(key: String): Float? {
        return settings.getFloatOrNull(key)
    }

    override fun getDoubleOrNull(key: String): Double? {
        return settings.getDoubleOrNull(key)
    }

    override fun setString(key: String, value: String) {
        settings[key] = value
    }

    override fun setBoolean(key: String, value: Boolean) {
        settings[key] = value
    }

    override fun setLong(key: String, value: Long) {
        settings[key] = value
    }

    override fun setInt(key: String, value: Int) {
        settings[key] = value
    }

    override fun setFloat(key: String, value: Float) {
        settings[key] = value
    }

    override fun setDouble(key: String, value: Double) {
        settings[key] = value
    }

    override fun remove(key: String) {
        settings.remove(key)
    }

}