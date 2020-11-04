package leo21.prim.scheme

import leo.base.assertEqualTo
import leo14.lambda.map
import leo14.lambda.scheme.code
import leo14.lambda.term
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.Prim
import kotlin.test.Test

class CodeTest {
	@Test
	fun twoPlusTwo() {
		term<Prim>(DoublePlusDoublePrim)
			.map(Prim::code)
			.code
			.assertEqualTo(code("(lambda (x) (+ (x (lambda (a) (lambda (b) a))) (x (lambda (a) (lambda (b) b)))))"))
	}
}