package leo14.lambda.value

import leo13.Stack
import leo13.get
import leo13.push
import leo13.stack
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo14.orError
import leo14.reflectOrEmptyScriptLine
import leo22.dsl.*

val applyTailCallOptimization = true

data class Scope<out T>(val valueStack: Stack<Value<T>>) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = valueStack.reflectOrEmptyScriptLine("scope") { anyReflectScriptLine }
}

fun <T> Scope<T>.scriptLine(nativeScriptLine: T.() -> ScriptLine): ScriptLine =
	valueStack.reflectOrEmptyScriptLine("scope") { scriptLine(nativeScriptLine) }

fun <T> emptyScope(): Scope<T> = Scope(stack())
fun <T> Scope<T>.push(value: Value<T>): Scope<T> = Scope(valueStack.push(value))
fun <T> Scope<T>.at(index: Int): Value<T> = valueStack.get(index).orError(anyReflectScriptLine, plus(number(index)))
fun <T> scope(vararg values: Value<T>) = Scope(stack(*values))

fun <T> Scope<T>.value(term: Term<T>, nativeApply: NativeApply<T>): Value<T> =
	when (term) {
		is NativeTerm -> value(term.native)
		is AbstractionTerm -> value(function(term.abstraction.body))
		is ApplicationTerm -> apply(
			term.application.lhs,
			value(term.application.rhs, nativeApply),
			nativeApply)
		is VariableTerm -> at(term.variable.index)
	}

tailrec fun <T> Scope<T>.apply(lhs: Term<T>, rhsValue: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	if (applyTailCallOptimization && lhs is AbstractionTerm && lhs.abstraction.body is ApplicationTerm) {
		val rhsScope = push(rhsValue)
		rhsScope.apply(
			lhs.abstraction.body.application.lhs,
			rhsScope.value(lhs.abstraction.body.application.rhs, nativeApply),
			nativeApply)
	} else {
		value(lhs, nativeApply).apply(rhsValue, nativeApply)
	}
