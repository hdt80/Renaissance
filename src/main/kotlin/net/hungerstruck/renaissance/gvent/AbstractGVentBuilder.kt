package net.hungerstruck.renaissance.gvent

import net.hungerstruck.renaissance.xml.module.RModule
import java.util.*

/**
 * Represents a property that is "settable" via the builder.
 */
data class BuildableProperty<out T>(
        val name: String,
        val module: Class<out RModule>,
        val value: T
)

/**
 * Type parameter X should be the class itself, as in class Foo : AbstractMapBuilder<Foo>, so we can return ourselves.
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractGVentBuilder<X : AbstractGVentBuilder<X>> {
    val properties: MutableList<BuildableProperty<*>> = arrayListOf()

    protected inline fun <reified T : RModule> register(prop: String, value: Any): X {
        properties.add(BuildableProperty(prop, T::class.java, value))
        return this as X
    }

    protected inline fun <reified T : RModule> register(value: BuilderPropertySet<*>): X {
        for (field in value.javaClass.declaredFields) {
            field.isAccessible = true
            val v: Any? = field.get(value) ?: throw RuntimeException("Required property ${field.name} has no value.")
            register<T>(field.name, v!!)
        }

        return this as X
    }
}

/**
 * Type parameter T should be the class itself, for the closure type.
 * Type parameter B should be the type _B_efore transformation.
 * Type parameter A should be the type _A_fter transformation.
 *
 * The unaryMinus operator is applied to B.
 */
@Suppress("UNCHECKED_CAST")
abstract class SingleTypeListBuilder<T : SingleTypeListBuilder<T, B, A>, B, A>(val transform: (B) -> A = { it as A }) {
    private val values: MutableList<A> = arrayListOf()

    operator fun B.unaryMinus(): A {
        val v = transform(this)
        values.add(v)
        return v
    }

    fun run(x: T.() -> Unit): Collection<A> {
        (this as T).x()
        return values()
    }

    fun values(): Collection<A> = ArrayList(values)
}

/**
 * Type parameter T should be the class itself, for the closure type.
 */
@Suppress("UNCHECKED_CAST")
abstract class BuilderPropertySet<T : BuilderPropertySet<T>> {
    fun build(x: T.() -> Unit): T {
        (this as T).x()
        return this
    }
}

