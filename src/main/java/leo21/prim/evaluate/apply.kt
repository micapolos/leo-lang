package leo21.prim.evaluate

import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string

val Value<Prim>.applyDoublePlusDouble
	get() =
		applyOp2(Prim::double, Prim::double, Double::prim, Double::plus)
val Value<Prim>.applyDoubleMinusDouble
	get() =
		applyOp2(Prim::double, Prim::double, Double::prim, Double::minus)
val Value<Prim>.applyDoubleTimesDouble
	get() =
		applyOp2(Prim::double, Prim::double, Double::prim, Double::times)
val Value<Prim>.applyStringPlusString
	get() =
		applyOp2(Prim::string, Prim::string, String::prim, String::plus)

inline fun <L, R, O> Value<Prim>.applyOp2(
	lfn: Prim.() -> L,
	rfn: Prim.() -> R,
	ofn: O.() -> Prim,
	fn: L.(R) -> O
): Value<Prim> =
	pair.run { value(first.native.lfn().fn(second.native.rfn()).ofn()) }
