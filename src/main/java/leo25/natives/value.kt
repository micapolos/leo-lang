package leo25.natives

import leo14.Number
import leo14.literal
import leo25.*
import java.lang.reflect.Method

fun Dictionary.nativeValue(name: String): Value =
	resolutionOrNull(value(name))!!.bindingOrNull!!.valueOrNull!!

fun Value.nativeValue(name: String): Value =
	getOrNull(name)!!

val Value.nativeText: String get() = textOrThrow
val Value.nativeNumber: Number get() = numberOrThrow
val Value.nativeObject: Any? get() = fieldOrNull!!.rhs.nativeOrNull!!.any
val Value.nativeClass: Class<*> get() = fieldOrNull!!.rhs.nativeOrNull!!.any as Class<*>
val Value.nativeMethod: Method get() = fieldOrNull!!.rhs.nativeOrNull!!.any as Method

val String.nativeValue get() = value(field(literal(this)))
val Number.nativeValue get() = value(field(literal(this)))
