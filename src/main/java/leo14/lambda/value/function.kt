package leo14.lambda.value

import leo.base.iterate
import leo13.fold
import leo13.reverse
import leo13.size
import leo13.takeOrNull
import leo14.ScriptLine
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.freeVariableCount
import leo14.lambda.invoke
import leo14.lambda.script
import leo14.lineTo
import leo14.anyReflectScriptLine
import leo14.script
import kotlin.math.min

data class Function<out T>(val scope: Scope<T>, val bodyTerm: Term<T>) {
	override fun toString() = scriptLine { anyReflectScriptLine }.toString()
}

fun <T> Function<T>.scriptLine(nativeScriptLine: T.() -> ScriptLine): ScriptLine =
	"function" lineTo script(
		scope.scriptLine(nativeScriptLine),
		"body" lineTo bodyTerm.script(nativeScriptLine))

fun <T> Scope<T>.function(term: Term<T>): Function<T> = Function(this, term)

fun <T> Function<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	scope.push(value).value(bodyTerm, nativeApply)

val <T> Function<T>.term: Term<T>
	get() =
		bodyTerm
			.iterate(bodyTerm.freeVariableCount) { fn(this) }
			.fold(scope.valueStack.takeOrNull(min(bodyTerm.freeVariableCount, (scope.valueStack.size)))!!.reverse) {
				invoke(it.term)
			}
