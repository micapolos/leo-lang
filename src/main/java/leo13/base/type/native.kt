package leo13.base.type

import leo13.script.Script

data class TypeNative<V : Any, A : Any>(
	val type: Type<A>,
	val getFn: V.() -> A,
	val newFn: A.() -> V)

fun <V : Any, A : Any> native(type: Type<A>, getFn: V.() -> A, newFn: A.() -> V) =
	TypeNative(type, getFn, newFn)

fun <V : Any, A : Any> TypeNative<V, A>.unsafeBodyValue(script: Script): V =
	type.unsafeBodyValue(script).newFn()

fun <V : Any, A : Any> TypeNative<V, A>.bodyScript(value: A): Script =
	type.bodyScript(value.newFn())
