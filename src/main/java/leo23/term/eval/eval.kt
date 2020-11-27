package leo23.term.eval

import leo.base.indexed
import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import leo14.Number
import leo14.minus
import leo14.numberOrNull
import leo14.plus
import leo14.string
import leo14.times
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
import leo23.value.Value
import leo23.value.indexed

val Expr.eval: Value get() = stakOf<Value>().eval(this)

fun Scope.eval(expr: Expr): Value = eval(expr.term)

// TODO: Implement tail recursion
fun Scope.eval(term: Term): Value =
	when (term) {
		NilTerm -> Unit
		is BooleanTerm -> term.boolean
		is StringTerm -> term.string
		is NumberTerm -> term.number
		is IsNilTerm -> eval(term.lhs) == Unit
		is NumberPlusTerm -> (eval(term.lhs) as Number).plus(eval(term.rhs) as Number)
		is NumberMinusTerm -> (eval(term.lhs) as Number).minus(eval(term.rhs) as Number)
		is NumberTimesTerm -> (eval(term.lhs) as Number).times(eval(term.rhs) as Number)
		is NumberEqualsTerm -> (eval(term.lhs) as Number) == (eval(term.rhs) as Number)
		is NumberStringTerm -> (eval(term.number) as Number).string
		is StringAppendTerm -> (eval(term.lhs) as String).plus(eval(term.rhs) as String)
		is StringEqualsTerm -> (eval(term.lhs) as String) == (eval(term.rhs) as String)
		is StringNumberOrNilTerm -> (eval(term.string) as String).numberOrNull?.let { 0 indexed it } ?: 1 indexed Unit
		is TupleTerm -> (term.list.map { eval(it) })
		is TupleAtTerm -> (eval(term.vector) as List<Value>).get(term.index)
		is ConditionalTerm -> if (eval(term.cond) as Boolean) eval(term.caseTrue) else eval(term.caseFalse)
		is FunctionTerm -> Fn(this, false, term.body)
		is RecursiveFunctionTerm -> Fn(this, true, term.body)
		is ApplyTerm -> (eval(term.function) as Fn).apply(term.paramList.map { eval(it) })
		is VariableTerm -> top(term.index)!!
		is IndexedTerm -> term.index indexed eval(term.rhs)
		is SwitchTerm -> eval(term.lhs).indexed.let { push(it.value).eval(term.cases[it.index]) }
	}
