package leo21.prim.scheme

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.map
import leo14.lambda.scheme.code
import leo14.lambda.syntax.eitherSwitch
import leo14.lambda.syntax.term
import leo14.lambda.term
import leo16.term.chez.eval
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberStringPrim
import leo21.prim.Prim
import leo21.prim.StringEqualsStringPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import leo21.syntax.boolean
import leo21.syntax.booleanSwitch
import leo21.syntax.countdown
import leo21.syntax.fn
import leo21.syntax.number
import leo21.syntax.numberEqualsNumber
import leo21.syntax.switch
import leo21.syntax.text
import leo21.term.plus
import kotlin.math.sin
import kotlin.test.Test

class EvalTest {
	@Test
	fun stringPlusString() {
		term<Prim>(StringPlusStringPrim)
			.invoke(term(prim("Hello, ")).plus(term(prim("world!"))))
			.map(Prim::code)
			.code
			.string
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun doubleSinus() {
		term<Prim>(NumberSinusPrim)
			.invoke(term(prim(1)))
			.map(Prim::code)
			.code
			.string
			.eval
			.assertEqualTo(sin(1.0).toString())
	}

	@Test
	fun numberString() {
		term<Prim>(NumberStringPrim)
			.invoke(term(prim(3.14)))
			.map(Prim::code)
			.code
			.string
			.eval
			.assertEqualTo("3.14")
	}

	@Test
	fun countdown() {
		number(3)
			.countdown
			.term
			.map(Prim::code)
			.code
			.string
			.eval
			.assertEqualTo("Countdown: 3, 2, 1, GO!!!")
	}
}