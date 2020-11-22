package leo21.type

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test
import kotlin.test.assertFails

class ScriptTypeTest {
	@Test
	fun empty() {
		script().type.assertEqualTo(type())
	}

	@Test
	fun number() {
		script("number").type.assertEqualTo(numberType)
	}

	@Test
	fun text() {
		script("text").type.assertEqualTo(stringType)
	}

	@Test
	fun literals() {
		assertFails { script(literal(20.0)).type }
		assertFails { script(literal("foo")).type }
	}

	@Test
	fun struct() {
		script(
			"name" lineTo script("text"),
			"age" lineTo script("number"))
			.type
			.assertEqualTo(
				type(
					"name" lineTo stringType,
					"age" lineTo numberType))
	}

	@Test
	fun choice() {
		script(
			"choice" lineTo script(
				"number" lineTo script(),
				"text" lineTo script()))
			.type
			.assertEqualTo(type(choice(numberLine, stringLine)))
	}

	@Test
	fun function() {
		script(
			"function" lineTo script(
				"number" lineTo script(),
				"doing" lineTo script("text")))
			.type
			.assertEqualTo(type(numberType arrowTo stringType))
	}

	@Test
	fun function_empty() {
		assertFails {
			script("function" lineTo script()).type
		}
	}

	@Test
	fun function_noDoing() {
		assertFails {
			script(
				"function" lineTo script(
					"number" lineTo script(),
					"making" lineTo script("text")))
				.type
		}
	}
}