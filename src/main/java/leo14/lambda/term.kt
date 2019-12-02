package leo14.lambda

import leo13.Index
import leo13.NextIndex
import leo13.ZeroIndex
import leo13.int
import leo14.*

sealed class Term<out T> {
	override fun toString() = script.indentString
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
	when (index) {
		is ZeroIndex -> fn(this)
		is NextIndex -> abstraction { body ->
			body.abstraction(index.previous, fn)
		}
	}

val <T : Any> Term<T>.nativeOrNull: T?
	get() =
		(this as? NativeTerm)?.native

val <T> Term<T>.native: T
	get() =
		(this as NativeTerm).native

// === script

val <T> Term<T>.script: Script
	get() =
		when (this) {
			is NativeTerm -> script("native" lineTo script(literal(native.toString())))
			is AbstractionTerm -> abstraction.script
			is ApplicationTerm -> application.script
			is VariableTerm -> variable.script
		}

val <T> Term<T>.scriptLine
	get() =
		"term" lineTo script

val <T> Abstraction<Term<T>>.script
	get() =
		script("lambda" lineTo body.script)

val <T> Application<Term<T>>.script
	get() =
		lhs.script.plus("apply" lineTo rhs.script)

val <T> Variable<T>.script
	get() =
		script("variable" lineTo script(literal(index.int)))
