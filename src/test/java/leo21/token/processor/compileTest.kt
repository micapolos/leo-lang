package leo21.token.processor

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.first
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.typed.plus
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.type
import kotlin.test.Test

class CompileTest {

	@Test
	fun get() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.0)),
				"y" lineTo script(literal(20.0))),
			"x" lineTo script())
			.compiled
			.assertEqualTo(
				nativeTerm(prim(10.0))
					.plus(nativeTerm(prim(20.0)))
					.first
					.of(type("x" lineTo doubleType)))
	}

	@Test
	fun do_() {
		script(
			"x" lineTo script(literal(10.0)),
			"do" lineTo script("given"))
			.compiled
			.assertEqualTo(
				fn(arg<Prim>(0)).invoke(nativeTerm(prim(10)))
					.of(type("given" lineTo type("x" lineTo doubleType))))
	}
}