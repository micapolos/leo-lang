package leo23.term.eval

import leo.stak.Stak
import leo.stak.push
import leo.stak.stakOf
import leo.stak.top
import leo14.Number
import leo14.minus
import leo14.number
import leo14.plus
import leo14.string
import leo14.times
import leo23.term.ApplyTerm
import leo23.term.BooleanTerm
import leo23.term.ConditionalTerm
import leo23.term.EqualsTerm
import leo23.term.FunctionTerm
import leo23.term.IsNilTerm
import leo23.term.LhsTerm
import leo23.term.MinusTerm
import leo23.term.Term
import leo23.term.NilTerm
import leo23.term.NumberTerm
import leo23.term.NumberStringTerm
import leo23.term.PairTerm
import leo23.term.PlusTerm
import leo23.term.RhsTerm
import leo23.term.StringAppendTerm
import leo23.term.StringEqualsTerm
import leo23.term.StringTerm
import leo23.term.StringNumberOrNilTerm
import leo23.term.TimesTerm
import leo23.term.VariableTerm
import leo23.term.VectorAtTerm
import leo23.term.VectorTerm

typealias Value = Any
typealias Scope = Stak<Value>

data class Fn(val scope: Scope, val body: Term)

fun Fn.apply(params: List<Value>): Value = params.fold(scope) { acc, value -> acc.push(value) }.eval(body)

val Term.eval: Value get() = stakOf<Value>().eval(this)

// TODO: Tail recursion!!!
fun Scope.eval(term: Term): Value =
	when (term) {
		NilTerm -> Unit
		is BooleanTerm -> term.boolean
		is StringTerm -> term.string
		is NumberTerm -> term.number
		is IsNilTerm -> eval(term.lhs) == Unit
		is PlusTerm -> (eval(term.lhs) as Number).plus(eval(term.rhs) as Number)
		is MinusTerm -> (eval(term.lhs) as Number).minus(eval(term.rhs) as Number)
		is TimesTerm -> (eval(term.lhs) as Number).times(eval(term.rhs) as Number)
		is EqualsTerm -> (eval(term.lhs) as Number) == (eval(term.rhs) as Number)
		is NumberStringTerm -> (eval(term.number) as Number).string
		is StringAppendTerm -> (eval(term.lhs) as String).plus(eval(term.rhs) as String)
		is StringEqualsTerm -> (eval(term.lhs) as String) == (eval(term.rhs) as String)
		is StringNumberOrNilTerm -> (eval(term.string) as String).toIntOrNull()?.number ?: Unit
		is PairTerm -> eval(term.lhs) to eval(term.rhs)
		is LhsTerm -> (eval(term.pair) as Pair<Value, Value>).first
		is RhsTerm -> (eval(term.pair) as Pair<Value, Value>).second
		is VectorTerm -> (term.list.map { eval(it) })
		is VectorAtTerm -> (eval(term.vector) as List<Value>).get((eval(term.index) as Number).bigDecimal.intValueExact())
		is ConditionalTerm -> if (eval(term.cond) as Boolean) eval(term.caseTrue) else eval(term.caseFalse)
		is FunctionTerm -> Fn(this, term.body)
		is ApplyTerm -> (eval(term.function) as Fn).apply(term.paramList.map { eval(it) })
		is VariableTerm -> top(term.index)!!
	}
