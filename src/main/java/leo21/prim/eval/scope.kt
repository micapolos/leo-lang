package leo21.prim.eval

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.Value
import leo14.lambda.VariableTerm
import leo14.lambda.evalTerm
import leo14.lambda.native
import leo14.lambda.term
import leo14.lambda.value
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.MinusDoublePrim
import leo21.prim.PlusDoublePrim
import leo21.prim.PlusStringPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.TimesDoublePrim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string

data class Scope(val evaluatedList: PersistentList<Evaluated>)

val PersistentList<Evaluated>.scope get() = Scope(this)
val emptyScope: Scope = persistentListOf<Evaluated>().scope
fun Scope.push(evaluated: Evaluated) = evaluatedList.add(evaluated).scope
fun Scope.at(depth: Int): Evaluated = evaluatedList[evaluatedList.size - depth - 1]

fun Scope.evaluate(term: Term<Prim>): Evaluated =
	when (term) {
		is NativeTerm -> evaluated(term.native)
		is AbstractionTerm -> evaluated(function(term.abstraction.body))
		is ApplicationTerm -> evaluate(term.application.lhs).apply(evaluate(term.application.rhs))
		is VariableTerm -> at(term.variable.index)
	}

fun Evaluated.apply(evaluated: Evaluated): Evaluated =
	when (this) {
		is ValueEvaluated -> evaluated(aPrim.applyOrNull(evaluated.value)!!)
		is FunctionEvaluated -> function.apply(evaluated)
	}

fun Prim.apply(value: Value<Prim>): Value<Prim> =
	term(applyOrNull(value.evalTerm.native)!!).value

fun Prim.applyOrNull(aPrim: Prim): Prim? =
	when (this) {
		is StringPrim -> null
		is DoublePrim -> null
		DoublePlusDoublePrim -> PlusDoublePrim(aPrim.double)
		DoubleMinusDoublePrim -> MinusDoublePrim(aPrim.double)
		DoubleTimesDoublePrim -> TimesDoublePrim(aPrim.double)
		StringPlusStringPrim -> PlusStringPrim(aPrim.string)
		is PlusDoublePrim -> prim(double + aPrim.double)
		is MinusDoublePrim -> prim(double - aPrim.double)
		is TimesDoublePrim -> prim(double * aPrim.double)
		is PlusStringPrim -> prim(string + aPrim.string)
	}