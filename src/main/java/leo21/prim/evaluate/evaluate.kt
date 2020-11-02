package leo21.prim.evaluate

import leo14.lambda.Term
import leo14.lambda.Value
import leo14.lambda.evalTerm
import leo14.lambda.evaluate
import leo14.lambda.evaluator
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

val Term<Prim>.evaluate: Term<Prim>
	get() =
		evaluator(Prim::apply)
			.evaluate(value)
			.evalTerm

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