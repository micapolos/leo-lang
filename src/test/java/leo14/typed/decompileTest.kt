package leo14.typed

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.lambda.term
import leo14.literal
import leo14.script
import kotlin.test.Test

class DecompileTest {
	@Test
	fun string() {
		term("Hello, world!")
			.of(nativeType)
			.anyDecompile
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun field() {
		term("Hello, world!")
			.of(type("text" fieldTo nativeType))
			.anyDecompile
			.assertEqualTo(script("text" fieldTo literal("Hello, world!")))
	}

	@Test
	fun fields() {
		term("zero")
			.plus(term("one"))
			.of(
				type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType))
			.anyDecompile
			.assertEqualTo(
				script(
					"x" fieldTo literal("zero"),
					"y" fieldTo literal("one")))
	}

	@Test
	fun struct() {
		term("zero")
			.plus(term("one"))
			.of(
				type(
					"vec" fieldTo type(
						"x" fieldTo nativeType,
						"y" fieldTo nativeType)))
			.anyDecompile
			.assertEqualTo(
				script(
					"vec" fieldTo script(
						"x" fieldTo literal("zero"),
						"y" fieldTo literal("one"))))
	}
}