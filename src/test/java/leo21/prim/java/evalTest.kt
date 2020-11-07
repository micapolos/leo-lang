package leo21.prim.java

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.java.eval
import leo14.lambda.map
import leo14.lambda.term
import leo21.prim.DoubleSinusPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import leo21.term.plus
import kotlin.math.sin
import kotlin.test.Test

class EvalTest {
	@Test
	fun stringPlusString() {
		term<Prim>(StringPlusStringPrim)
			.invoke(term(prim("Hello, ")).plus(term(prim("world!"))))
			.map(Prim::native)
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun doubleSinus() {
		term<Prim>(DoubleSinusPrim)
			.invoke(term(prim(1)))
			.map(Prim::native)
			.eval
			.assertEqualTo("${sin(1.0)}")
	}
}