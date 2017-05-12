package net.hungerstruck.renaissance.config

import kotlin.reflect.KProperty

class ConfigEntry<T>(private val path: String) {
    /**
     * TODO
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return RConfig.config.get(path) as T
    }

    /**
     * TODO
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        RConfig.config.set(path, value)
        RConfig.config.save(RConfig.configFile)
    }
}

class DefaultConfigEntry<T>(private val path: String, private val default: T) {
    /**
     * TODO
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return RConfig.config.get(path, default) as T
    }

    /**
     * TODO
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        RConfig.config.set(path, value)
        RConfig.config.save(RConfig.configFile)
    }
}

class ComputedConfigEntry<T>(private val path: String, private val read: (String) -> T, private val write: (T) -> String) {
    /**
     * TODO
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return read(RConfig.config.getString(path))
    }

    /**
     * TODO
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        RConfig.config.set(path, write(value))
        RConfig.config.save(RConfig.configFile)
    }
}

/**
 * TODO
 */
fun <T> path(s: String): ConfigEntry<T> {
    return ConfigEntry(s)
}

/**
 * TODO
 */
fun <T> path(s: String, def: T): DefaultConfigEntry<T> {
    return DefaultConfigEntry(s, def)
}

/**
 * TODO
 */
fun <T> path(s: String, fn1: (String) -> T, fn2: (T) -> String): ComputedConfigEntry<T> {
    return ComputedConfigEntry(s, fn1, fn2)
}