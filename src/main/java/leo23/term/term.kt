package leo23.term

import leo14.Number
import leo14.Scriptable
import leo14.lineTo

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

data class IsNilTerm(val lhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class PlusTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class MinusTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class TimesTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class EqualsTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class NumberStringTerm(val number: Term) : Term() {
	override fun toString() = super.toString()
}

data class StringAppendTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class StringEqualsTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class StringNumberOrNilTerm(val string: Term) : Term() {
	override fun toString() = super.toString()
}

data class PairTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class LhsTerm(val pair: Term) : Term() {
	override fun toString() = super.toString()
}

data class RhsTerm(val pair: Term) : Term() {
	override fun toString() = super.toString()
}

data class VectorTerm(val list: List<Term>) : Term() {
	override fun toString() = super.toString()
}

data class VectorAtTerm(val vector: Term, val index: Term) : Term() {
	override fun toString() = super.toString()
}

data class ConditionalTerm(val cond: Term, val caseTrue: Term, val caseFalse: Term) : Term() {
	override fun toString() = super.toString()
}

data class FunctionTerm(val arity: Int, val body: Term) : Term() {
	override fun toString() = super.toString()
}

data class ApplyTerm(val function: Term, val paramList: List<Term>) : Term() {
	override fun toString() = super.toString()
}

data class VariableTerm(val index: Int) : Term() {
	override fun toString() = super.toString()
}
