package leo21.prim.runtime

import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.value
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string

fun Prim.apply(rhs: Value<Prim>): Value<Prim> =
	when (this) {
		is NilPrim -> null
		is StringPrim -> null
		is DoublePrim -> null
		DoublePlusDoublePrim -> rhs.apply(Prim::double, Double::plus, Prim::double, Double::prim)
		DoubleMinusDoublePrim -> rhs.apply(Prim::double, Double::minus, Prim::double, Double::prim)
		DoubleTimesDoublePrim -> rhs.apply(Prim::double, Double::times, Prim::double, Double::prim)
		StringPlusStringPrim -> rhs.apply(Prim::string, String::plus, Prim::string, String::prim)
	}!!

inline fun <L, R, O> Value<Prim>.apply(
	lhs: Prim.() -> L,
	op: L.(R) -> O,
	rhs: Prim.() -> R,
	ret: O.() -> Prim
): Value<Prim> =
	pair.run { value(first.native.lhs().op(second.native.rhs()).ret()) }
