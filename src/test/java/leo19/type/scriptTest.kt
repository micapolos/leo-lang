package leo19.type

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import leo19.value.indexed
import leo19.value.nativeValue
import leo19.value.value
import kotlin.test.Test

class ScriptTest {
	@Test
	fun nativeScript() {
		native(String::class)
			.script(nativeValue("Hello, world!"))
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun emptyStructScript() {
		struct()
			.script(nativeValue(null))
			.assertEqualTo(script())
	}

	@Test
	fun simpleStructScript() {
		struct("number" to native(Int::class))
			.script(nativeValue(10))
			.assertEqualTo(script("number" lineTo script(literal(10))))
	}

	@Test
	fun complexStructScript() {
		struct(
			"x" to native(Int::class),
			"y" to native(Int::class))
			.script(value(nativeValue(10), nativeValue(20)))
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20))))
	}

	@Test
	fun simpleChoiceScript() {
		val type = choice(
			"false" to struct(),
			"true" to struct())
		type
			.script(value(0))
			.assertEqualTo(script("false"))
		type
			.script(value(1))
			.assertEqualTo(script("true"))
	}

	@Test
	fun complexChoiceScript() {
		val type =
			choice(
				"number" to native(Int::class),
				"text" to native(String::class))
		type
			.script(0 indexed nativeValue(10))
			.assertEqualTo(script("number" lineTo script(literal(10))))
		type
			.script(1 indexed nativeValue("foo"))
			.assertEqualTo(script("text" lineTo script(literal("foo"))))
	}
}