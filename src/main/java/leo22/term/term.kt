package leo22.term

import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo21.prim.NilPrim
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberStringPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.nilPrim
import leo21.prim.prim
import leo22.dsl.*

val termDef =
	term(
		choice(
			native(),
			abstraction(term()),
			application(lhs(term()), rhs(term())),
			variable(number())))

fun nativeTerm(int: Int) =
	term(native(number(int)))

fun nativeTerm(string: String) =
	term(native(text(string)))

fun fnTerm(term: X): X =
	term(abstraction(term))

fun argTerm(index: Int): X =
	term(variable(number(index)))

fun X.termApply(rhs: X): X =
	term(application(lhs(this), rhs(rhs)))

val X.termNative_: Term<Prim>
	get() =
		switch_(
			native { nativeTerm(it.termPrim_) },
			abstraction { fn(it.term.termNative_) },
			application { it.lhs.term.termNative_.invoke(it.rhs.term.termNative_) },
			variable { arg<Prim>(it.number.int_) })

val X.termPrim_: Prim
	get() =
		switch_(
			nil { nilPrim },
			text { prim(it.string_) },
			number { prim(it.int_) })

val Term<Prim>.x_: X
	get() =
		term(
			when (this) {
				is NativeTerm -> native(native.x_)
				is AbstractionTerm -> term(abstraction(abstraction.body.x_))
				is ApplicationTerm -> term(application(lhs(application.lhs.x_), rhs(application.rhs.x_)))
				is VariableTerm -> term(variable(number(variable.index)))
			}
		)

val Prim.x_: X
	get() =
		when (this) {
			NilPrim -> native(nil())
			is StringPrim -> native(text(string))
			is NumberPrim -> native(number(number))
			NumberPlusNumberPrim -> TODO()
			NumberMinusNumberPrim -> TODO()
			NumberTimesNumberPrim -> TODO()
			NumberSinusPrim -> TODO()
			NumberCosinusPrim -> TODO()
			StringPlusStringPrim -> TODO()
			NumberEqualsNumberPrim -> TODO()
			StringLengthPrim -> TODO()
			NumberStringPrim -> TODO()
		}

val nilTerm = term(native(nil()))

fun X.termPlus(rhs: X): X =
	fnTerm(
		fnTerm(
			fnTerm(
				argTerm(0)
					.termApply(argTerm(2))
					.termApply(argTerm(1)))))
		.termApply(this)
		.termApply(rhs)
