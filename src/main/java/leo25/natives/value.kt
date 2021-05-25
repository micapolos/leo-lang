package leo25.natives

import leo14.*
import leo14.Number
import leo25.*

fun Dictionary.nativeValue(name: String): Value =
	resolutionOrNull(value(name))!!.bindingOrNull!!.valueOrNull!!

fun Value.nativeValue(name: String): Value =
	getOrNull(name)!!

val Value.nativeText: String get() = textOrThrow
val Value.nativeNumber: Number get() = numberOrThrow

val String.nativeValue get() = value(field(literal(this)))
val Number.nativeValue get() = value(field(literal(this)))
