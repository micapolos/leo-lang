package leo21.prim.runtime

import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.value
import leo21.prim.DoubleCosinusPrim
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleSinusPrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.double
import leo21.prim.prim
import leo21.prim.string
import kotlin.math.cos
import kotlin.math.sin

fun Prim.apply(rhs: Value<Prim>): Value<Prim> =
	when (this) {
		is NilPrim -> null
		is StringPrim -> null
		is DoublePrim -> null
		DoublePlusDoublePrim -> rhs.apply(Prim::double, Double::plus, Prim::double, Double::prim)
		DoubleMinusDoublePrim -> rhs.apply(Prim::double, Double::minus, Prim::double, Double::prim)
		DoubleTimesDoublePrim -> rhs.apply(Prim::double, Double::times, Prim::double, Double::prim)
		StringPlusStringPrim -> rhs.apply(Prim::string, String::plus, Prim::string, String::prim)
		DoubleSinusPrim -> rhs.apply(Prim::double, ::sin, Double::prim)
		DoubleCosinusPrim -> rhs.apply(Prim::double, ::cos, Double::prim)
	}!!

fun <Lhs, Rhs, Out> Value<Prim>.apply(
	lhs: Prim.() -> Lhs,
	op: Lhs.(Rhs) -> Out,
	rhs: Prim.() -> Rhs,
	out: Out.() -> Prim
): Value<Prim> =
	pair { lhs, rhs ->
		value(lhs.native.lhs().op(rhs.native.rhs()).out())
	}

fun <Lhs, Out> Value<Prim>.apply(
	lhs: Prim.() -> Lhs,
	op: Lhs.() -> Out,
	out: Out.() -> Prim
): Value<Prim> =
	value(native.lhs().op().out())
