package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.nativeTerm
import leo15.dsl.*
import leo21.evaluated.evaluated
import leo21.evaluated.script
import leo21.prim.StringTryNumberPrim
import leo21.prim.prim
import leo21.type.numberType
import leo21.type.try_
import kotlin.test.Test

class CompiledResolveTest {
	@Test
	fun get() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)),
			"x" lineTo compiled())
			.resolveGetOrNull!!
			.assertEqualTo(
				compiled(
					"point" lineTo compiled(
						"x" lineTo compiled(10.0),
						"y" lineTo compiled(20.0)))
					.getOrNull("x")!!)
	}

	@Test
	fun as_() {
		val compiled = compiled(
			"x" lineTo compiled(10.0),
			"y" lineTo compiled(20.0),
			"as" lineTo compiled(
				"point" lineTo compiled()))

		compiled
			.resolve
			.assertEqualTo(compiled.link.tail.make("point"))
	}

	@Test
	fun as_to() {
		val compiled = compiled(
			"x" lineTo compiled(10.0),
			"as" lineTo compiled(
				"point" lineTo compiled(
					"to" lineTo compiled(
						"y" lineTo compiled(20.0)))))

		compiled
			.resolve
			.assertEqualTo(
				compiled.access("x")
					.plus(compiled.access("as").get("point").get("to").get("y").link.head)
					.make("point"))
	}

	@Test
	fun stringTryNumber() {
		compiled(
			line("123"),
			"try" lineTo compiled("number" lineTo compiled()))
			.resolve
			.assertEqualTo(
				nativeTerm(StringTryNumberPrim)
					.invoke(nativeTerm(prim("123")))
					.of(numberType.try_)
			)
	}
}