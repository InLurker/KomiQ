package com.inlurker.komiq.model.data.kotatsu

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.koitharu.kotatsu.parsers.config.ConfigKey
import org.koitharu.kotatsu.parsers.config.MangaSourceConfig
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.model.SortOrder


private const val KEY_SORT_ORDER = "sort_order"
private const val KEY_SLOWDOWN = "slowdown"

class KotatsuSourceSettings(context: Context, source: MangaSource) : MangaSourceConfig {

    private val prefs = context.getSharedPreferences(source.name, Context.MODE_PRIVATE)

    var defaultSortOrder: SortOrder?
        get() = prefs.getEnumValue(KEY_SORT_ORDER, SortOrder::class.java)
        set(value) = prefs.edit { putEnumValue(KEY_SORT_ORDER, value) }

    val isSlowdownEnabled: Boolean
        get() = prefs.getBoolean(KEY_SLOWDOWN, false)

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: ConfigKey<T>): T {
        return when (key) {
            is ConfigKey.UserAgent -> prefs.getString(key.key, key.defaultValue).ifNullOrEmpty { key.defaultValue }
            is ConfigKey.Domain -> prefs.getString(key.key, key.defaultValue).ifNullOrEmpty { key.defaultValue }
            is ConfigKey.ShowSuspiciousContent -> prefs.getBoolean(key.key, key.defaultValue)
        } as T
    }

    operator fun <T> set(key: ConfigKey<T>, value: T) = prefs.edit {
        when (key) {
            is ConfigKey.Domain -> putString(key.key, value as String?)
            is ConfigKey.ShowSuspiciousContent -> putBoolean(key.key, value as Boolean)
            is ConfigKey.UserAgent -> putString(key.key, value as String?)
        }
    }
}

fun <E : Enum<E>> SharedPreferences.getEnumValue(key: String, enumClass: Class<E>): E? {
    val stringValue = getString(key, null) ?: return null
    return enumClass.enumConstants?.find {
        it.name == stringValue
    }
}

fun <E : Enum<E>> SharedPreferences.getEnumValue(key: String, defaultValue: E): E {
    return getEnumValue(key, defaultValue.javaClass) ?: defaultValue
}

fun <E : Enum<E>> SharedPreferences.Editor.putEnumValue(key: String, value: E?) {
    putString(key, value?.name)
}

inline fun <C : CharSequence?> C?.ifNullOrEmpty(defaultValue: () -> C): C {
    return if (this.isNullOrEmpty()) defaultValue() else this
}
