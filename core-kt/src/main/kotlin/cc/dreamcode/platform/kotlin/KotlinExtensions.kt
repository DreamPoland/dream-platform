package cc.dreamcode.platform.kotlin

import cc.dreamcode.platform.DreamPlatform
import cc.dreamcode.platform.component.ComponentClassResolver
import cc.dreamcode.platform.component.ComponentExtension
import cc.dreamcode.platform.component.ComponentMethodResolver
import cc.dreamcode.platform.component.ComponentService
import eu.okaeri.injector.Injector
import java.util.Optional
import java.util.function.Consumer
import java.util.stream.Stream
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

fun ComponentService.registerMethodResolver(`class`: KClass<out ComponentMethodResolver<*>>) {
    this.registerMethodResolver(`class`.java)
}

fun <T : Any> DreamPlatform.getInject(`class`: KClass<T>): Optional<T> = this.getInject(`class`.java)

fun <T : Any> Injector.streamOf(type: KClass<T>): Stream<T> = this.streamOf(type.java)

fun <T : Any> Injector.get(name: String, type: KClass<T>): Optional<T> = this.get(name, type.java)

fun <T : Any> DreamPlatform.createInstance(type: KClass<T>): T = this.createInstance(type.java)

inline fun <reified T : Any> ComponentService.registerComponent(): Unit =
    this.registerComponent(T::class.java)

inline fun <reified T : Any> ComponentService.registerComponent(noinline consumer: (T) -> Unit): Unit =
    this.registerComponent(T::class.java, Consumer(consumer))

inline fun <reified T : ComponentExtension> ComponentService.registerExtension(): Unit =
    this.registerExtension(T::class.java)

inline fun <reified T : ComponentClassResolver<*>> ComponentService.registerResolver(): Unit =
    this.registerResolver(T::class.java)

inline fun <reified T : ComponentMethodResolver<*>> ComponentService.registerMethodResolver(): Unit =
    this.registerMethodResolver(T::class.java)

inline fun <reified T : Any> ComponentService.registerComponentObject(componentObject: T): Unit =
    this.registerComponentObject(componentObject)

inline fun <reified T : Any> ComponentService.registerComponentObject(componentObject: T, noinline consumer: (T) -> Unit): Unit =
    this.registerComponentObject(componentObject, Consumer(consumer))

inline fun <reified T : Any> DreamPlatform.getInject(name: String = ""): Optional<T> =
    if (name.isEmpty()) this.getInject(T::class.java) else this.getInject(name, T::class.java)

inline fun <reified T : Any> DreamPlatform.getInjectOrNull(name: String = ""): T? =
    if (name.isEmpty()) this.getInjectOrNull(T::class.java) else this.getInjectOrNull(name, T::class.java)

inline fun <reified T : Any> DreamPlatform.getInjectOrThrow(name: String = ""): T =
    if (name.isEmpty()) this.getInjectOrThrow(T::class.java) else this.getInjectOrThrow(name, T::class.java)

inline fun <reified T : Any> DreamPlatform.createInstance(): T =
    this.createInstance(T::class.java)

inline fun <reified T : Any> DreamPlatform.registerInjectable(name: String = "", instance: T): T =
    if (name.isEmpty()) this.registerInjectable(instance) else this.registerInjectable(name, instance)

inline fun <reified T : Any> Injector.streamOf(): Stream<T> =
    this.streamOf(T::class.java)

inline fun <reified T : Any> Injector.get(name: String = ""): Optional<T> =
    if (name.isEmpty()) this.get("", T::class.java) else this.get(name, T::class.java)
