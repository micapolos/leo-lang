package leo21.prim.evaluate

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo14.lambda.value.evaluate
import leo14.lambda.value.value
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val Term<Prim>.value: Value<Prim>
	get() =
		value(Prim::apply)

val Term<Prim>.evaluate: Term<Prim>
	get() =
		evaluate(Prim::apply)

fun Prim.apply(rhs: Value<Prim>): Value<Prim> =
	when (this) {
		is NilPrim -> null
		is StringPrim -> null
		is DoublePrim -> null
		DoublePlusDoublePrim -> rhs.applyDoublePlusDouble
		DoubleMinusDoublePrim -> rhs.applyDoubleMinusDouble
		DoubleTimesDoublePrim -> rhs.applyDoubleTimesDouble
		StringPlusStringPrim -> rhs.applyStringPlusString
	}!!
