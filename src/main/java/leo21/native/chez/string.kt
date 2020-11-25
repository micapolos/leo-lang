package leo21.native.chez

import leo14.literalString
import leo14.string
import leo21.native.ApplyNative
import leo21.native.BooleanNative
import leo21.native.ConditionalNative
import leo21.native.FunctionNative
import leo21.native.IsNilNative
import leo21.native.Native
import leo21.native.NilNative
import leo21.native.EqualsNative
import leo21.native.MinusNative
import leo21.native.NumberNative
import leo21.native.PlusNative
import leo21.native.NumberStringNative
import leo21.native.TimesNative
import leo21.native.LhsNative
import leo21.native.PairNative
import leo21.native.RhsNative
import leo21.native.StringEqualsNative
import leo21.native.StringNative
import leo21.native.StringNumberOrNilNative
import leo21.native.StringAppendNative
import leo21.native.VariableNative
import leo21.native.VectorAtNative
import leo21.native.VectorNative

val Native.string: String get() = string(0)

fun Native.string(depth: Int): String =
	when (this) {
		NilNative -> "'()"
		is BooleanNative -> if (boolean) "#t" else "#f"
		is StringNative -> string.literalString
		is NumberNative -> number.string
		is IsNilNative -> "(null? ${native.string(depth)})"
		is PlusNative -> "(+ ${lhs.string(depth)} ${rhs.string(depth)})"
		is MinusNative -> "(- ${lhs.string(depth)} ${rhs.string(depth)})"
		is TimesNative -> "(* ${lhs.string(depth)} ${rhs.string(depth)})"
		is EqualsNative -> "(= ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberStringNative -> "(number->string ${number.string(depth)})"
		is StringAppendNative -> "(string-append ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringEqualsNative -> "(string=? ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringNumberOrNilNative -> "((lambda (x) (if x x '())) (string->number ${string.string(depth)}))"
		is PairNative -> "(cons ${lhs.string(depth)} ${rhs.string(depth)})"
		is LhsNative -> "(car ${pair.string(depth)})"
		is RhsNative -> "(cdr ${pair.string(depth)})"
		is VectorNative -> "(vector ${list.joinToString(" ") { it.string(depth) }})"
		is VectorAtNative -> "(vector-ref ${vector.string(depth)} ${index.string(depth)})"
		is ConditionalNative -> "(if ${cond.string(depth)} ${caseTrue.string(depth)} ${caseFalse.string(depth)})"
		is FunctionNative -> "(lambda (${(0 until arity).joinToString(" ") { "v${it + depth}" }}) ${body.string(depth.plus(arity))})"
		is ApplyNative -> "(${listOf(function).plus(paramList).joinToString(" ") { it.string(depth) }})"
		is VariableNative -> "v${depth - index - 1}"
	}
