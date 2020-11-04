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
		applyOp2(Prim::double, Double::plus, Prim::double, Double::prim)
val Value<Prim>.applyDoubleMinusDouble
	get() =
		applyOp2(Prim::double, Double::minus, Prim::double, Double::prim)
val Value<Prim>.applyDoubleTimesDouble
	get() =
		applyOp2(Prim::double, Double::times, Prim::double, Double::prim)
val Value<Prim>.applyStringPlusString
	get() =
		applyOp2(Prim::string, String::plus, Prim::string, String::prim)

inline fun <L, R, O> Value<Prim>.applyOp2(
	lhs: Prim.() -> L,
	op: L.(R) -> O,
	rhs: Prim.() -> R,
	ret: O.() -> Prim
): Value<Prim> =
	pair.run { value(first.native.lhs().op(second.native.rhs()).ret()) }
