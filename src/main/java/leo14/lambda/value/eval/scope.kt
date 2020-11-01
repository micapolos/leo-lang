package leo14.lambda.value.eval

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo14.lambda.value.DoubleMinusDoubleValue
import leo14.lambda.value.DoublePlusDoubleValue
import leo14.lambda.value.DoubleTimesDoubleValue
import leo14.lambda.value.DoubleValue
import leo14.lambda.value.MinusDoubleValue
import leo14.lambda.value.PlusDoubleValue
import leo14.lambda.value.PlusStringValue
import leo14.lambda.value.StringPlusStringValue
import leo14.lambda.value.StringValue
import leo14.lambda.value.TimesDoubleValue
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
		is NativeTerm -> evaluated(term.native)
		is AbstractionTerm -> evaluated(function(term.abstraction.body))
		is ApplicationTerm -> evaluate(term.application.lhs).apply(evaluate(term.application.rhs))
		is VariableTerm -> at(term.variable.index)
	}

fun Evaluated.apply(evaluated: Evaluated): Evaluated =
	when (this) {
		is ValueEvaluated -> evaluated(value.applyOrNull(evaluated.value)!!)
		is FunctionEvaluated -> function.apply(evaluated)
	}

fun Value.applyOrNull(value: Value): Value? =
	when (this) {
		is StringValue -> null
		is DoubleValue -> null
		DoublePlusDoubleValue -> PlusDoubleValue(value.double)
		DoubleMinusDoubleValue -> MinusDoubleValue(value.double)
		DoubleTimesDoubleValue -> TimesDoubleValue(value.double)
		StringPlusStringValue -> PlusStringValue(value.string)
		is PlusDoubleValue -> value(double + value.double)
		is MinusDoubleValue -> value(double - value.double)
		is TimesDoubleValue -> value(double * value.double)
		is PlusStringValue -> value(string + value.string)
	}