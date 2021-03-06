package leo14.lambda.value

import leo13.fold
import leo13.reverse
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.script

data class Function<out T>(val scope: Scope<T>, val bodyTerm: Term<T>) : Scriptable() {
	override fun toString() = scriptLine { anyReflectScriptLine }.toString()
	override val reflectScriptLine get() = scriptLine { anyReflectScriptLine }
}

fun <T> Function<T>.scriptLine(nativeScriptLine: T.() -> ScriptLine): ScriptLine =
	"function" lineTo script(
		scope.scriptLine(nativeScriptLine),
		bodyTerm.scriptLine(nativeScriptLine))

fun <T> Scope<T>.function(term: Term<T>): Function<T> = Function(this, term)

fun <T> Function<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	scope.push(value).value(bodyTerm, nativeApply)

val <T> Function<T>.term: Term<T>
	get() =
		fn(bodyTerm)
			.fold(scope.valueStack) { fn(this) }
			.fold(scope.valueStack.reverse) { invoke(it.term) }
