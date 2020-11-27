package leo23.term.chez

import leo14.literalString
import leo14.string
import leo23.term.ApplyTerm
import leo23.term.BooleanTerm
import leo23.term.ConditionalTerm
import leo23.term.Expr
import leo23.term.FunctionTerm
import leo23.term.IndexedTerm
import leo23.term.IsNilTerm
import leo23.term.NilTerm
import leo23.term.NumberEqualsTerm
import leo23.term.NumberMinusTerm
import leo23.term.NumberPlusTerm
import leo23.term.NumberStringTerm
import leo23.term.NumberTerm
import leo23.term.NumberTimesTerm
import leo23.term.RecursiveFunctionTerm
import leo23.term.StringAppendTerm
import leo23.term.StringEqualsTerm
import leo23.term.StringNumberOrNilTerm
import leo23.term.StringTerm
import leo23.term.SwitchTerm
import leo23.term.Term
import leo23.term.TupleAtTerm
import leo23.term.TupleTerm
import leo23.term.VariableTerm

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
		is NumberPlusTerm -> "(+ ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberMinusTerm -> "(- ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberTimesTerm -> "(* ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberEqualsTerm -> "(= ${lhs.string(depth)} ${rhs.string(depth)})"
		is NumberStringTerm -> "(number->string ${number.string(depth)})"
		is StringAppendTerm -> "(string-append ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringEqualsTerm -> "(string=? ${lhs.string(depth)} ${rhs.string(depth)})"
		is StringNumberOrNilTerm -> "(let ((number-or-false (string->number ${string.string(depth)}))) (if number-or-false (cons 0 number-or-false) (cons 1 '())))"
		is TupleTerm -> "(vector ${list.joinToString(" ") { it.string(depth) }})"
		is TupleAtTerm -> "(vector-ref ${vector.string(depth)} ${index})"
		is ConditionalTerm -> "(if ${cond.string(depth)} ${caseTrue.string(depth)} ${caseFalse.string(depth)})"
		is FunctionTerm -> "(lambda (${(paramTypes.indices).joinToString(" ") { it.plus(depth).varString }}) ${body.string(depth.plus(paramTypes.size))})"
		is RecursiveFunctionTerm -> "(letrec ((${depth.varString} (lambda (${(paramTypes.indices).joinToString(" ") { it.plus(depth).inc().varString }}) ${body.string(depth.plus(paramTypes.size).inc())}))) ${depth.varString})"
		is ApplyTerm -> "(${listOf(function).plus(paramList).joinToString(" ") { it.string(depth) }})"
		is VariableTerm -> depth.minus(index).dec().varString
		is IndexedTerm -> "(cons $index ${rhs.string(depth)})"
		is SwitchTerm -> "(let* ((indexed ${lhs.string(depth)}) (index (car indexed)) (${depth.varString} (cdr indexed))) (case index ${cases.mapIndexed { index, case -> "(($index) ${case.string(depth.inc())})" }.joinToString(" ")} ))"
	}

val Int.varString get() = "v$this"