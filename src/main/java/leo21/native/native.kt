package leo21.native

import leo14.Number

sealed class Native
object NilNative : Native()
data class BooleanNative(val boolean: Boolean) : Native()
data class StringNative(val string: String) : Native()
data class NumberNative(val number: Number) : Native()
data class IsNilNative(val native: Native) : Native()
data class PlusNative(val lhs: Native, val rhs: Native) : Native()
data class MinusNative(val lhs: Native, val rhs: Native) : Native()
data class TimesNative(val lhs: Native, val rhs: Native) : Native()
data class EqualsNative(val lhs: Native, val rhs: Native) : Native()
data class NumberStringNative(val number: Native) : Native()
data class StringAppendNative(val lhs: Native, val rhs: Native) : Native()
data class StringEqualsNative(val lhs: Native, val rhs: Native) : Native()
data class StringNumberOrNilNative(val string: Native) : Native()
data class PairNative(val lhs: Native, val rhs: Native) : Native()
data class LhsNative(val pair: Native) : Native()
data class RhsNative(val pair: Native) : Native()
data class VectorNative(val list: List<Native>) : Native()
data class VectorAtNative(val vector: Native, val index: Native) : Native()
data class ConditionalNative(val cond: Native, val caseTrue: Native, val caseFalse: Native) : Native()
data class FunctionNative(val arity: Int, val body: Native) : Native()
data class ApplyNative(val function: Native, val paramList: List<Native>) : Native()
data class VariableNative(val index: Int) : Native()

