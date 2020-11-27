package leo23.term

import leo14.Number
import leo14.Scriptable
import leo14.lineTo
import leo23.term.type.Type

sealed class Term : Scriptable() {
	override val reflectScriptLine get() = "native" lineTo script
}

object NilTerm : Term()

data class BooleanTerm(val boolean: Boolean) : Term() {
	override fun toString() = super.toString()
}

data class StringTerm(val string: String) : Term() {
	override fun toString() = super.toString()
}

data class NumberTerm(val number: Number) : Term() {
	override fun toString() = super.toString()
}

data class IsNilTerm(val lhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class NumberPlusTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class NumberMinusTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class NumberTimesTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class NumberEqualsTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class NumberStringTerm(val number: Expr) : Term() {
	override fun toString() = super.toString()
}

data class StringAppendTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class StringEqualsTerm(val lhs: Expr, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class StringNumberOrNilTerm(val string: Expr) : Term() {
	override fun toString() = super.toString()
}

data class TupleTerm(val list: List<Expr>) : Term() {
	override fun toString() = super.toString()
}

data class TupleAtTerm(val vector: Expr, val index: Int) : Term() {
	override fun toString() = super.toString()
}

data class ConditionalTerm(val cond: Expr, val caseTrue: Expr, val caseFalse: Expr) : Term() {
	override fun toString() = super.toString()
}

data class FunctionTerm(val paramTypes: List<Type>, val body: Expr) : Term() {
	override fun toString() = super.toString()
}

data class RecursiveFunctionTerm(val paramTypes: List<Type>, val body: Expr) : Term() {
	override fun toString() = super.toString()
}

data class ApplyTerm(val function: Expr, val paramList: List<Expr>) : Term() {
	override fun toString() = super.toString()
}

data class VariableTerm(val index: Int) : Term() {
	override fun toString() = super.toString()
}

data class IndexedTerm(val index: Int, val rhs: Expr) : Term() {
	override fun toString() = super.toString()
}

data class SwitchTerm(val lhs: Expr, val cases: List<Expr>) : Term() {
	override fun toString() = super.toString()
}
