@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo13.*
import leo14.*
import leo14.lambda.runtime.Value
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object ClassLoaderSource

val classLoader = ClassLoaderSource::class.java.classLoader

val Value.valueArray
	get() =
		(this as Stack<Value>).array

fun Value.arrayAt(rhs: Value) =
	(this as Array<*>)[rhs as Int]

val Value.arrayValue
	get() =
		stack(*(this as Array<*>))

// === Primitives ===

val Value.applyTextPlusText: Value
	get() =
		asPair.first.asString + asPair.second.asString

val Value.applyTextNumber: Value
	get() =
		asNumber.toString()

val Value.applyNumberLengthText: Value
	get() =
		asString.length.number

val Value.applyMinusNumber: Value
	get() =
		-asNumber

val Value.applyNumberPlusNumber: Value
	get() =
		asPair.first.asNumber + asPair.second.asNumber

val Value.applyNumberMinusNumber: Value
	get() =
		asPair.first.asNumber - asPair.second.asNumber

val Value.applyNumberTimesNumber: Value
	get() =
		asPair.first.asNumber * asPair.second.asNumber

// === Reflection ===

val Value.stringClass
	get() =
		classLoader.loadClass(this as String)

fun Value.classConstructor(rhs: Value) =
	(this as Class<*>).getConstructor(*(rhs as Stack<Value>).map { this as Class<*> }.array)

fun Value.classMethod(rhs: Value) =
	(this as Class<*>).getMethod(
		(rhs as Stack<Value>).link.stack.top as String,
		*(rhs.top as Stack<Value>).map { this as Class<*> }.array)

fun Value.classField(rhs: Value) =
	(this as Class<*>).getField(rhs as String)

fun Value.constructorInvoke(rhs: Value) =
	(this as Constructor<Value>).newInstance(*(rhs as Stack<Value>).array)

fun Value.methodInvoke(rhs: Value) =
	(this as Method).invoke((rhs as Stack<Value>).link.stack.top, *(rhs.top as Stack<Value>).array)

fun Value.fieldGet(rhs: Value) =
	(this as Field).get(rhs)
