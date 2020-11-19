package leo21.type

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun empty() {
		type().script.assertEqualTo(script())
	}

	@Test
	fun text() {
		stringType.script.assertEqualTo(script("text"))
	}

	@Test
	fun number() {
		numberType.script.assertEqualTo(script("number"))
	}

	@Test
	fun struct() {
		type("x" lineTo numberType, "y" lineTo numberType)
			.script
			.assertEqualTo(script("x" lineTo script("number"), "y" lineTo script("number")))
	}

	@Test
	fun choice_() {
		type(choice(stringLine, numberLine))
			.script
			.assertEqualTo(
				script(
					"choice" lineTo script(
						"text" lineTo script(),
						"number" lineTo script())))
	}

	@Test
	fun function() {
		type(line(numberType arrowTo stringType))
			.script
			.assertEqualTo(
				script(
					"function" lineTo script(
						"number" lineTo script(),
						"does" lineTo script("text"))))
	}
}