package leo21.prim.runtime

import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string

val Value<Prim>.resolveDoublePlusDouble
	get() =
		resolve(Prim::double, Double::plus, Prim::double, Double::prim)

val Value<Prim>.resolveDoubleMinusDouble
	get() =
		resolve(Prim::double, Double::minus, Prim::double, Double::prim)

val Value<Prim>.resolveDoubleTimesDouble
	get() =
		resolve(Prim::double, Double::times, Prim::double, Double::prim)

val Value<Prim>.resolveStringPlusString
	get() =
		resolve(Prim::string, String::plus, Prim::string, String::prim)

inline fun <L, R, O> Value<Prim>.resolve(
	lhs: Prim.() -> L,
	op: L.(R) -> O,
	rhs: Prim.() -> R,
	ret: O.() -> Prim
): Value<Prim> =
	pair.run { value(first.native.lhs().op(second.native.rhs()).ret()) }
