package leo14.lambda.value

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm

data class Scope(val valueList: PersistentList<Value>)

val PersistentList<Value>.scope get() = Scope(this)
val emptyScope: Scope = persistentListOf<Value>().scope
fun Scope.push(value: Value) = valueList.add(value).scope
fun Scope.at(depth: Int): Value = valueList[valueList.size - depth - 1]

fun Scope.eval(term: Term<Value>): Value =
	when (term) {
		is NativeTerm -> apply(term.native)
		is AbstractionTerm -> FunctionValue(Function(this, term.abstraction.body))
		is ApplicationTerm -> eval(term.application.lhs).function.apply(eval(term.application.rhs))
		is VariableTerm -> at(term.variable.index)
	}

fun Scope.apply(value: Value): Value =
	when (value) {
		is FunctionValue -> value
		is StringValue -> value
		is DoubleValue -> value
		DoublePlusValue -> value(at(1).double + at(0).double)
		DoubleMinusValue -> value(at(1).double - at(0).double)
		DoubleTimesValue -> value(at(1).double * at(0).double)
		StringPlusValue -> value(at(1).string + at(0).string)
	}