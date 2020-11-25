package leo21.native

import leo14.Number
import leo14.Scriptable
import leo14.lineTo

sealed class Native : Scriptable() {
	override val reflectScriptLine get() = "native" lineTo script
}

object NilNative : Native()

data class BooleanNative(val boolean: Boolean) : Native() {
	override fun toString() = super.toString()
}

data class StringNative(val string: String) : Native() {
	override fun toString() = super.toString()
}

data class NumberNative(val number: Number) : Native() {
	override fun toString() = super.toString()
}

data class IsNilNative(val lhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class PlusNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class MinusNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class TimesNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class EqualsNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class NumberStringNative(val number: Native) : Native() {
	override fun toString() = super.toString()
}

data class StringAppendNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class StringEqualsNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class StringNumberOrNilNative(val string: Native) : Native() {
	override fun toString() = super.toString()
}

data class PairNative(val lhs: Native, val rhs: Native) : Native() {
	override fun toString() = super.toString()
}

data class LhsNative(val pair: Native) : Native() {
	override fun toString() = super.toString()
}

data class RhsNative(val pair: Native) : Native() {
	override fun toString() = super.toString()
}

data class VectorNative(val list: List<Native>) : Native() {
	override fun toString() = super.toString()
}

data class VectorAtNative(val vector: Native, val index: Native) : Native() {
	override fun toString() = super.toString()
}

data class ConditionalNative(val cond: Native, val caseTrue: Native, val caseFalse: Native) : Native() {
	override fun toString() = super.toString()
}

data class FunctionNative(val arity: Int, val body: Native) : Native() {
	override fun toString() = super.toString()
}

data class ApplyNative(val function: Native, val paramList: List<Native>) : Native() {
	override fun toString() = super.toString()
}

data class VariableNative(val index: Int) : Native() {
	override fun toString() = super.toString()
}
