package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.*

typealias ValueFn = () -> Value

fun valueFn(fn: ValueFn) = fn

data class Typed(val type: Value, val valueFn: ValueFn)

fun typed(type: Value, valueFn: () -> Value) = Typed(type, valueFn)
val Typed.value get() = valueFn()
val Value.typedValue get() = (this as Typed).value

val Value.valueSelfTyped get() = typed(selfType) { this }
val nullTyped = null.valueSelfTyped
val String.typed get() = typed(textType) { this }
val Boolean.typed get() = typed(booleanType) { this }
val Int.typed get() = typed(intType) { this }
val Number.typed get() = typed(numberType) { this }
val Value.nativeTyped get() = typed(nativeType) { this }

val Field.typed
	get() =
		when (rhs) {
			is Typed -> typed(name fieldTo rhs.type) { rhs.value }
			else -> typed(name fieldTo rhs) { rhs } // TODO: Is it what we want?
		}

val Value.valueTyped
	get() =
		when (this) {
			is Typed -> this
			is String -> typed
			is Boolean -> typed
			is Int -> typed
			is Number -> typed
			is Field -> typed
			else -> nativeTyped
		}

fun Typed.apply(rhs: Typed): Typed =
	when (type) {
		null ->
			when (rhs.type) {
				is Field ->
					when (rhs.type.name) {
						minusName ->
							when (rhs.type.rhs) {
								intName -> typed(intName) { -(rhs.value as Int) }
								numberName -> typed(numberName) { -(rhs.value as Number) }
								else -> append(rhs)
							}
						textName ->
							when (rhs.type.rhs) {
								intName -> typed(textName) { (rhs.value as Int).toString() }
								numberName -> typed(textName) { (rhs.value as Number).toString() }
								else -> append(rhs)
							}
						intName ->
							when (rhs.type.rhs) {
								textName -> (rhs.value as String).toIntOrNull()?.typed ?: append(rhs)
								else -> append(rhs)
							}
						numberName ->
							when (rhs.type.rhs) {
								textName -> (rhs.value as String).numberOrNull?.typed ?: append(rhs)
								else -> append(rhs)
							}
						else -> append(rhs)
					}
				else -> append(rhs)
			}
		textName ->
			when (rhs.type) {
				is Field ->
					when (rhs.type.name) {
						plusName ->
							when (rhs.type.rhs) {
								textName -> typed(textName) { (value as String) + (rhs.value as String) }
								else -> append(rhs)
							}
						else -> append(rhs)
					}
				else -> append(rhs)
			}
		intName ->
			when (rhs.type) {
				is Field ->
					when (rhs.type.name) {
						plusName ->
							when (rhs.type.rhs) {
								intName -> typed(intName) { (value as Int) + (rhs.value as Int) }
								else -> append(rhs)
							}
						minusName ->
							when (rhs.type.rhs) {
								intName -> typed(intName) { (value as Int) - (rhs.value as Int) }
								else -> append(rhs)
							}
						timesName ->
							when (rhs.type.rhs) {
								intName -> typed(intName) { (value as Int) * (rhs.value as Int) }
								else -> append(rhs)
							}
						else -> append(rhs)
					}
				else -> append(rhs)
			}
		numberName ->
			when (rhs.type) {
				is Field ->
					when (rhs.type.name) {
						plusName ->
							when (rhs.type.rhs) {
								numberName -> typed(intName) { (value as Number) + (rhs.value as Number) }
								else -> append(rhs)
							}
						minusName ->
							when (rhs.type.rhs) {
								numberName -> typed(intName) { (value as Number) - (rhs.value as Number) }
								else -> append(rhs)
							}
						timesName ->
							when (rhs.type.rhs) {
								numberName -> typed(intName) { (value as Number) * (rhs.value as Number) }
								else -> append(rhs)
							}
						else -> append(rhs)
					}
				else -> append(rhs)
			}
		else -> append(rhs)
	}

fun Typed.append(rhs: Typed): Typed =
	typed(type.valueAppend(rhs.type)) { value.valueAppend(rhs.value) }

fun Value.valueTypedFunction(fn: (Value) -> Value): Typed {
	var binding: Value = nullValue
	return fn(typed(this) { binding }).valueTyped.let { result ->
		typed(this.valueAppend(doingName fieldTo result.type)) {
			{ given: Value ->
				binding = given
				val ret = result.value
				binding = null
				ret
			}
		}
	}
}
