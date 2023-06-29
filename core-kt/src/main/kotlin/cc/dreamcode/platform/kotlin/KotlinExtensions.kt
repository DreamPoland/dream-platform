package cc.dreamcode.platform.kotlin

import cc.dreamcode.platform.DreamPlatform
import cc.dreamcode.platform.component.ComponentClassResolver
import cc.dreamcode.platform.component.ComponentManager
import eu.okaeri.configs.serdes.DeserializationData
import eu.okaeri.injector.Injector
import java.util.Optional
import java.util.function.Consumer
import kotlin.reflect.KClass
fun ComponentManager.registerComponent(`class`: KClass<*>) {
    this.registerComponent(`class`.java)
}

fun <T : Any> ComponentManager.registerComponent(`class`: KClass<T>, consumer: Consumer<T>) {
    this.registerComponent(`class`.java, consumer)
}

fun ComponentManager.registerResolver(`class`: KClass<out ComponentClassResolver<out Class<*>>>) {
    this.registerResolver(`class`.java)
}

fun <T : Any> DreamPlatform.getInject(`class`: KClass<T>) = this.getInject(`class`.java)

fun <T : Any> Injector.streamOf(type: KClass<T>) = this.streamOf(type.java)

fun <T : Any> DeserializationData.get(key: String, valueType: KClass<T>): T? = this.get(key, valueType.java)

fun <T : Any> DeserializationData.getAsList(key: String, valueType: KClass<T>): List<T> = this.getAsList(key, valueType.java)

fun <T: Any> Injector.get(name: String, type: KClass<T>): Optional<T> = this.get(name, type.java)

fun <T : Any> DreamPlatform.createInstance(type: KClass<T>): T = this.createInstance(type.java)
