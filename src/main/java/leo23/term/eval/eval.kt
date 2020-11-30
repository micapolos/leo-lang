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
import leo23.term.type.Type
import leo23.typed.Typed
import leo23.typed.of
import leo23.value.Scope
import leo23.value.Value
import leo23.value.indexed

val Expr.eval: Typed<Value, Type> get() = value.of(type)
val Expr.value: Value get() = stakOf<Value>().value(this)
fun Scope.value(expr: Expr): Value = value(expr.term)

// TODO: Implement tail recursion
fun Scope.value(term: Term): Value =
	when (term) {
		NilTerm -> Unit
		is BooleanTerm -> term.boolean
		is StringTerm -> term.string
		is NumberTerm -> term.number
		is IsNilTerm -> value(term.lhs) == Unit
		is NumberPlusTerm -> (value(term.lhs) as Number).plus(value(term.rhs) as Number)
		is NumberMinusTerm -> (value(term.lhs) as Number).minus(value(term.rhs) as Number)
		is NumberTimesTerm -> (value(term.lhs) as Number).times(value(term.rhs) as Number)
		is NumberEqualsTerm -> (value(term.lhs) as Number) == (value(term.rhs) as Number)
		is NumberStringTerm -> (value(term.number) as Number).string
		is StringAppendTerm -> (value(term.lhs) as String).plus(value(term.rhs) as String)
		is StringEqualsTerm -> (value(term.lhs) as String) == (value(term.rhs) as String)
		is StringNumberOrNilTerm -> (value(term.string) as String).numberOrNull?.let { 0 indexed it } ?: 1 indexed Unit
		is TupleTerm -> (term.list.map { value(it) })
		is TupleAtTerm -> (value(term.vector) as List<Value>).get(term.index)
		is ConditionalTerm -> if (value(term.cond) as Boolean) value(term.caseTrue) else value(term.caseFalse)
		is FunctionTerm -> Fn(this, false, term.body)
		is RecursiveFunctionTerm -> Fn(this, true, term.body)
		is ApplyTerm -> (value(term.function) as Fn).apply(term.paramList.map { value(it) })
		is VariableTerm -> top(term.index)!!
		is IndexedTerm -> term.index indexed value(term.rhs)
		is SwitchTerm -> value(term.lhs).indexed.let { push(it.value).value(term.cases[it.index]) }
	}
