package leo25.natives

import leo.base.map
import leo.base.stack
import leo13.array
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
val Value.nativeObject: Any?
	get() = fieldOrNull?.rhs?.nativeOrNull.notNullOrThrow {
		plus(
			notName fieldTo value(
				nativeName
			)
		)
	}.any
val Value.nativeClass: Class<*> get() = fieldOrNull!!.rhs.nativeOrNull!!.any as Class<*>
val Value.nativeMethod: Method get() = fieldOrNull!!.rhs.nativeOrNull!!.any as Method

val String.nativeValue get() = value(field(literal(this)))
val Number.nativeValue get() = value(field(literal(this)))

val Value.nativeArray: Array<Any?>
	get() =
		fieldOrNull!!.rhs.valueOrNull!!.fieldSeq.map { value(this).nativeArrayElement }.stack.array

val Value.nativeArrayElement: Any?
	get() =
		nativeValue(javaName).nativeValue(objectName).nativeObject

