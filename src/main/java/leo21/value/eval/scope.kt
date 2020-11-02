package leo21.value.eval

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo21.value.DoubleMinusDoubleValue
import leo21.value.DoublePlusDoubleValue
import leo21.value.DoubleTimesDoubleValue
import leo21.value.DoubleValue
import leo21.value.MinusDoubleValue
import leo21.value.PlusDoubleValue
import leo21.value.PlusStringValue
import leo21.value.StringPlusStringValue
import leo21.value.StringValue
import leo21.value.TimesDoubleValue
import leo21.value.Value
import leo21.value.double
import leo21.value.string
import leo21.value.value

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