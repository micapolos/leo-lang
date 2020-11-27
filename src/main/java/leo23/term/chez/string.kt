package leo23.term.chez

import leo14.literalString
import leo14.string
import leo23.term.ApplyTerm
import leo23.term.BooleanTerm
import leo23.term.ConditionalTerm
import leo23.term.EqualsTerm
import leo23.term.FunctionTerm
import leo23.term.IndexedTerm
import leo23.term.IsNilTerm
import leo23.term.MinusTerm
import leo23.term.NilTerm
import leo23.term.NumberStringTerm
import leo23.term.NumberTerm
import leo23.term.PlusTerm
import leo23.term.StringAppendTerm
import leo23.term.StringEqualsTerm
import leo23.term.StringNumberOrNilTerm
import leo23.term.StringTerm
import leo23.term.SwitchTerm
import leo23.term.Term
import leo23.term.TimesTerm
import leo23.term.TupleAtTerm
import leo23.term.TupleTerm
import leo23.term.VariableTerm
import leo23.term.Expr
import leo23.term.RecursiveFunctionTerm

val Expr.string: String get() = string(0)

fun Expr.string(depth: Int): String =
	term.string(depth)

fun Term.string(depth: Int): String =
	when (this) {
		NilTerm -> "'()"
		is BooleanTerm -> if (boolean) "#t" else "#f"
		is StringTerm -> string.literalString
		is NumberTerm -> number.string
		is IsNilTerm -> "(null? ${lhs.string(depth)})"
		is PlusTerm -> "(+ ${lhs.string(depth)} ${rhs.string(depth)})"
		is MinusTerm -> "(- ${lhs.string(depth)} ${rhs.string(depth)})"
		is TimesTerm -> "(* ${lhs.string(depth)} ${rhs.string(depth)})"
		is EqualsTerm -> "(= ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberStringTerm -> "(number->string ${number.string(depth)})"
		is StringAppendTerm -> "(string-append ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringEqualsTerm -> "(string=? ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringNumberOrNilTerm -> "((lambda (x) (if x (cons 0 x) (cons 1 '()))) (string->number ${string.string(depth)}))"
		is TupleTerm -> "(vector ${list.joinToString(" ") { it.string(depth) }})"
		is TupleAtTerm -> "(vector-ref ${vector.string(depth)} ${index})"
		is ConditionalTerm -> "(if ${cond.string(depth)} ${caseTrue.string(depth)} ${caseFalse.string(depth)})"
		is FunctionTerm -> "(lambda (${(paramTypes.indices).joinToString(" ") { "v${it + depth}" }}) ${body.string(depth.plus(paramTypes.size))})"
		is RecursiveFunctionTerm -> TODO()
		is ApplyTerm -> "(${listOf(function).plus(paramList).joinToString(" ") { it.string(depth) }})"
		is VariableTerm -> "v${depth - index - 1}"
		is IndexedTerm -> "(cons $index ${rhs.string(depth)})"
		is SwitchTerm -> TODO()
	}
