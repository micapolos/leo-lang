package leo14.lambda.value.eval

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo14.lambda.value.DoubleMinusValue
import leo14.lambda.value.DoublePlusValue
import leo14.lambda.value.DoubleTimesValue
import leo14.lambda.value.DoubleValue
import leo14.lambda.value.StringPlusValue
import leo14.lambda.value.StringValue
import leo14.lambda.value.Value
import leo14.lambda.value.double
import leo14.lambda.value.string
import leo14.lambda.value.value

data class Scope(val evaluatedList: PersistentList<Evaluated>)

val PersistentList<Evaluated>.scope get() = Scope(this)
val emptyScope: Scope = persistentListOf<Evaluated>().scope
fun Scope.push(evaluated: Evaluated) = evaluatedList.add(evaluated).scope
fun Scope.at(depth: Int): Evaluated = evaluatedList[evaluatedList.size - depth - 1]

fun Scope.evaluate(term: Term<Value>): Evaluated =
	when (term) {
		is NativeTerm -> evaluate(term.native)
		is AbstractionTerm -> evaluated(function(term.abstraction.body))
		is ApplicationTerm -> evaluate(term.application.lhs).function.apply(evaluate(term.application.rhs))
		is VariableTerm -> at(term.variable.index)
	}

fun Scope.evaluate(value: Value): Evaluated =
	when (value) {
		is StringValue -> evaluated(value)
		is DoubleValue -> evaluated(value)
		DoublePlusValue -> evaluated(value(at(1).value.double + at(0).value.double))
		DoubleMinusValue -> evaluated(value(at(1).value.double - at(0).value.double))
		DoubleTimesValue -> evaluated(value(at(1).value.double * at(0).value.double))
		StringPlusValue -> evaluated(value(at(1).value.string + at(0).value.string))
	}