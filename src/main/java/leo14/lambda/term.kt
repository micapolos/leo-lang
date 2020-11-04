package leo14.lambda

import leo13.Index
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.indentString
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.anyReflectScriptLine
import leo14.plus
import leo14.script

sealed class Term<out T> : Scriptable() {
	override fun toString() = script { line(literal(toString())) }.indentString
	override val reflectScriptLine: ScriptLine get() = "term" lineTo script { anyReflectScriptLine }
}

data class NativeTerm<T>(val native: T) : Term<T>() {
	override fun toString() = super.toString()
}

data class AbstractionTerm<T>(val abstraction: Abstraction<Term<T>>) : Term<T>() {
	override fun toString() = super.toString()
}

data class ApplicationTerm<T>(val application: Application<Term<T>>) : Term<T>() {
	override fun toString() = super.toString()
}

data class VariableTerm<T>(val variable: Variable<T>) : Term<T>() {
	override fun toString() = super.toString()
}

fun <T> term(native: T): Term<T> = NativeTerm(native)
fun <T> nativeTerm(native: T): Term<T> = NativeTerm(native)
fun <T> term(abstraction: Abstraction<Term<T>>): Term<T> = AbstractionTerm(abstraction)
fun <T> term(application: Application<Term<T>>): Term<T> = ApplicationTerm(application)
fun <T> term(variable: Variable<T>): Term<T> = VariableTerm(variable)

// === matching

fun <T, R> Term<T>.abstraction(fn: (Term<T>) -> R): R =
	when (this) {
		is AbstractionTerm -> fn(abstraction.body)
		else -> error("$this as abstraction")
	}

fun <T, R> Term<T>.application(fn: (Term<T>, Term<T>) -> R): R =
	when (this) {
		is ApplicationTerm -> fn(application.lhs, application.rhs)
		else -> error("$this as application")
	}

fun <T, R> Term<T>.variable(fn: (Index) -> R): R =
	when (this) {
		is VariableTerm -> fn(variable.index)
		else -> error("$this as variable")
	}

fun <T, R> Term<T>.native(fn: (T) -> R): R =
	when (this) {
		is NativeTerm -> fn(native)
		else -> error("$this as native")
	}

fun <T, R> Term<T>.abstraction(index: Index, fn: (Term<T>) -> R): R =
	if (index == 0) fn(this)
	else abstraction { body ->
		body.abstraction(index.dec(), fn)
	}

val <T : Any> Term<T>.nativeOrNull: T?
	get() =
		(this as? NativeTerm)?.native

val <T> Term<T>.native: T
	get() =
		(this as NativeTerm).native

// === script

fun <T> Term<T>.script(nativeFn: T.() -> ScriptLine): Script =
		when (this) {
			is NativeTerm -> script("native" lineTo script(native.nativeFn()))
			is AbstractionTerm -> abstraction.script(nativeFn)
			is ApplicationTerm -> application.script(nativeFn)
			is VariableTerm -> variable.script
		}

fun <T> Term<T>.scriptLine(nativeFn: T.() -> ScriptLine) =
	"term" lineTo script(nativeFn)

fun <T> Abstraction<Term<T>>.script(nativeFn: T.() -> ScriptLine) =
	script("lambda" lineTo body.script(nativeFn))

fun <T> Application<Term<T>>.script(nativeFn: T.() -> ScriptLine) =
	lhs.script(nativeFn).plus("apply" lineTo rhs.script(nativeFn))

val <T> Variable<T>.script
	get() =
		script("variable" lineTo script(literal(index)))

fun nativeFn(fn: (Any) -> Any) = term(fn)

// === native helpers

val Any.anyIntInc: Any get() = (this as Int).inc()
fun Any.anyIntPlus(rhs: Any): Any = (this as Int) + (rhs as Int)

fun <I, O> Term<I>.map(fn: (I) -> O): Term<O> =
	when (this) {
		is NativeTerm -> term(fn(native))
		is AbstractionTerm -> fn(abstraction.body.map(fn))
		is ApplicationTerm -> application.lhs.map(fn).invoke(application.rhs.map(fn))
		is VariableTerm -> arg(variable.index)
	}