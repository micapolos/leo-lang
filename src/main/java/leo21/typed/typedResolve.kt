package leo21.typed

import leo.base.ifOrNull
import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.type.Type
import leo21.type.doubleType
import leo21.type.op2Type
import leo21.type.stringType
import leo21.type.type

val Typed.resolveGetOrNull: Typed?
	get() =
		linkOrNull?.run {
			head.fieldTypedOrNull?.let { fieldTyped ->
				ifOrNull(fieldTyped.rhsTyped.type == type()) {
					tail.getOrNull(fieldTyped.field.name)
				}
			}
		}

val Typed.resolve: Typed
	get() =
		resolvePrimOrNull ?: this

val Type.fn2Typed: Typed get() = fn(arg<Prim>(0)) of this

fun Typed.resolveFn2OrNull(lhs: Type, name: String, rhs: Type, prim: Prim, result: Type): Typed? =
	notNullIf(type == op2Type(lhs, name, rhs)) {
		fn(nativeTerm(prim).invoke(arg(0))).invoke(term).of(result)
	}

val Typed.resolvePrimOrNull: Typed?
	get() =
		null
			?: resolveFn2OrNull(doubleType, "plus", doubleType, DoublePlusDoublePrim, doubleType)
			?: resolveFn2OrNull(doubleType, "minus", doubleType, DoubleMinusDoublePrim, doubleType)
			?: resolveFn2OrNull(doubleType, "times", doubleType, DoubleTimesDoublePrim, doubleType)
			?: resolveFn2OrNull(stringType, "plus", stringType, StringPlusStringPrim, stringType)
