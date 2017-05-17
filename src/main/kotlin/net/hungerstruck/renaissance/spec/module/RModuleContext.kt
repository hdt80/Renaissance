package net.hungerstruck.renaissance.spec.module

import net.hungerstruck.renaissance.match.RMatch
import net.hungerstruck.renaissance.spec.mapspec.AbstractMapBuilder
import net.hungerstruck.renaissance.spec.mapspec.MapBuilder
import net.hungerstruck.renaissance.spec.inject
import kotlin.reflect.KClass

class RModuleContext(private val match: RMatch) {
    val modules: MutableSet<RModule> = hashSetOf()

    init {
        for (info in RModuleRegistry.MODULES) {
            loadModule(info)
        }
    }

    inline fun <reified T : RModule> getModule(): T? {
        return modules.filterIsInstance<T>().firstOrNull()
    }

    inline fun <reified T : RModule> hasModule(): Boolean {
        return hasModule(T::class)
    }

    fun hasModule(clazz: KClass<out RModule>): Boolean {
        return modules.filterIsInstance(clazz.java).any()
    }

    private fun loadModule(info: RModuleInfo): Boolean {
        if (hasModule(info.clazz)) return true

        for (dep in info.dependencies) {
            if (!loadModule(dep)) {
                return false
            }
        }

        val instance = info.constructor.newInstance(match, this)
        
        injectProperties(match.map.mapBuilder, instance)
        instance.init()

        modules.add(instance)
        return true
    }

    private fun injectProperties(builder: AbstractMapBuilder<MapBuilder>, mod: RModule) {
        for (field in mod.javaClass.declaredFields) {
            if (!field.isAnnotationPresent(inject::class.java)) continue
            field.isAccessible = true

            val value = builder.properties.filter { it.module == mod.javaClass && it.name == field.name }.firstOrNull()
            if (value == null && field.get(mod) == null) {
                throw IllegalStateException("No value passed for required field ${field.name} in module ${mod.javaClass.simpleName}")
            } else if (value != null) {
                field.set(mod, value.value)
            }
        }
    }
}