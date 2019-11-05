package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.lambda.term
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class DecompileTest {
	@Test
	fun string() {
		term("Hello, world!")
			.of(nativeType)
			.decompile
			.assertEqualTo(script("Hello, world!"))
	}

	@Test
	fun field() {
		term("Hello, world!")
			.of(type("text" fieldTo nativeType))
			.decompile
			.assertEqualTo(script("text" fieldTo "Hello, world!"))
	}

	@Test
	fun fields() {
		term("zero")
			.typedPlus(term("one"))
			.of(
				type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType))
			.decompile
			.assertEqualTo(
				script(
					"x" fieldTo "zero",
					"y" fieldTo "one"))
	}

	@Test
	fun struct() {
		term("zero")
			.typedPlus(term("one"))
			.of(
				type(
					"vec" fieldTo type(
						"x" fieldTo nativeType,
						"y" fieldTo nativeType)))
			.decompile
			.assertEqualTo(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one")))
	}
}