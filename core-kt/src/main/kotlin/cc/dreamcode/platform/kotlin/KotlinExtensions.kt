package cc.dreamcode.platform.kotlin

import cc.dreamcode.platform.DreamPlatform
import cc.dreamcode.platform.component.ComponentClassResolver
import cc.dreamcode.platform.component.ComponentExtension
import cc.dreamcode.platform.component.ComponentService
import eu.okaeri.injector.Injector
import java.util.Optional
import java.util.function.Consumer
import kotlin.reflect.KClass

fun ComponentService.registerComponent(`class`: KClass<*>) {
    this.registerComponent(`class`.java)
}

fun <T : Any> ComponentService.registerComponent(`class`: KClass<T>, consumer: Consumer<T>) {
    this.registerComponent(`class`.java, consumer)
}

fun ComponentService.registerExtension(`class`: KClass<out ComponentExtension>) {
    this.registerExtension(`class`.java)
}

fun ComponentService.registerResolver(`class`: KClass<out ComponentClassResolver<*>>) {
    this.registerResolver(`class`.java)
}

fun <T : Any> DreamPlatform.getInject(`class`: KClass<T>) = this.getInject(`class`.java)

fun <T : Any> Injector.streamOf(type: KClass<T>) = this.streamOf(type.java)

fun <T: Any> Injector.get(name: String, type: KClass<T>): Optional<T> = this.get(name, type.java)

fun <T : Any> DreamPlatform.createInstance(type: KClass<T>): T = this.createInstance(type.java)