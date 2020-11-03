package leo21.prim.evaluate

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo14.lambda.value.evaluate
import leo14.lambda.value.native
import leo14.lambda.value.value
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.MinusDoublePrim
import leo21.prim.NilPrim
import leo21.prim.PlusDoublePrim
import leo21.prim.PlusStringPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.TimesDoublePrim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string

val Term<Prim>.value: Value<Prim>
	get() =
		value(Prim::apply)

val Term<Prim>.evaluate: Term<Prim>
	get() =
		evaluate(Prim::apply)

fun Prim.apply(rhs: Value<Prim>): Value<Prim> =
	value(apply(rhs.native))

fun Prim.apply(rhs: Prim): Prim =
	applyOrNull(rhs)!!

fun Prim.applyOrNull(rhs: Prim): Prim? =
	when (this) {
		is NilPrim -> null
		is StringPrim -> null
		is DoublePrim -> null
		DoublePlusDoublePrim -> PlusDoublePrim(rhs.double)
		DoubleMinusDoublePrim -> MinusDoublePrim(rhs.double)
		DoubleTimesDoublePrim -> TimesDoublePrim(rhs.double)
		StringPlusStringPrim -> PlusStringPrim(rhs.string)
		is PlusDoublePrim -> prim(double + rhs.double)
		is MinusDoublePrim -> prim(double - rhs.double)
		is TimesDoublePrim -> prim(double * rhs.double)
		is PlusStringPrim -> prim(string + rhs.string)
	}