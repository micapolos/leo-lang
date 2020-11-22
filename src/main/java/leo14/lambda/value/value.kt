package leo14.lambda.value

import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.lineTo
import leo14.orError
import leo14.script
import leo22.dsl.*

sealed class Value<out T> : Scriptable() {
	override fun toString() = scriptLine { anyReflectScriptLine }.toString()
	override val reflectScriptLine: ScriptLine
		get() = "value" lineTo script(
			when (this) {
				is NativeValue -> "native" lineTo script(native.anyReflectScriptLine)
				is FunctionValue -> function.reflectScriptLine
			})
}

data class NativeValue<T>(val native: T) : Value<T>() {
	override fun toString() = super.toString()
}

data class FunctionValue<T>(val function: Function<T>) : Value<T>() {
	override fun toString() = super.toString()
}

fun <T> Value<T>.scriptLine(nativeScriptLine: T.() -> ScriptLine): ScriptLine =
	"value" lineTo script(
		when (this) {
			is NativeValue -> "native" lineTo script(native.nativeScriptLine())
			is FunctionValue -> function.scriptLine(nativeScriptLine)
		}
	)

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(function: Function<T>): Value<T> = FunctionValue(function)

val <T> T.nativeValue: Value<T> get() = value(this)
val <T> Function<T>.value: Value<T> get() = value(this)

val <T> Value<T>.native: T get() = nativeOrNull.orError(anyReflectScriptLine, native())
val <T> Value<T>.function: Function<T> get() = functionOrNull.orError(anyReflectScriptLine, function())

val <T> Value<T>.nativeOrNull: T? get() = (this as? NativeValue)?.native
val <T> Value<T>.functionOrNull: Function<T>? get() = (this as? FunctionValue)?.function

fun <T> Value<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (this) {
		is NativeValue -> native.nativeApply(value)
		is FunctionValue -> function.apply(value, nativeApply)
	}

val <T> Value<T>.term: Term<T>
	get() =
		when (this) {
			is NativeValue -> term(native)
			is FunctionValue -> function.term
		}

fun Any.anyApply(value: Value<Any>): Value<Any> =
	value((this as (Any) -> Any).invoke(value.native))

fun <T> Value<T>.plus(value: Value<T>): Value<T> =
	value(scope(this, value).function(arg<T>(0).invoke(arg(2)).invoke(arg(1))))

val <T> Value<T>.eitherFirst: Value<T>
	get() =
		value(scope(this).function(fn(arg<T>(0).invoke(arg(2)))))

val <T> Value<T>.eitherSecond: Value<T>
	get() =
		value(scope(this).function(fn(arg<T>(1).invoke(arg(2)))))
