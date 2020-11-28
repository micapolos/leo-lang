package leo23.typed.value

import leo.base.assertEqualTo
import leo.base.indexed
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.number
import leo14.script
import leo14.scriptLine
import leo23.term.nilExpr
import leo23.type.booleanType
import leo23.type.cases
import leo23.type.choice
import leo23.type.fields
import leo23.type.numberType
import leo23.type.struct
import leo23.type.textType
import leo23.typed.of
import leo23.value.value
import kotlin.test.Test

class ScriptTest {
	@Test
	fun number() {
		10.value
			.of(numberType)
			.scriptLine
			.assertEqualTo(line(literal(10)))
	}

	@Test
	fun text() {
		"Hello"
			.of(textType)
			.scriptLine
			.assertEqualTo(line(literal("Hello")))
	}

	@Test
	fun boolean() {
		true
			.of(booleanType)
			.scriptLine
			.assertEqualTo("true".scriptLine)
	}

	@Test
	fun struct0() {
		nilExpr.of("point" struct fields())
			.scriptLine
			.assertEqualTo("point" lineTo script())
	}

	@Test
	fun struct1() {
		10.value
			.of("radius" struct fields(numberType))
			.scriptLine
			.assertEqualTo("radius" lineTo script(literal(10)))
	}

	@Test
	fun struct2() {
		listOf(10.value, 20.value)
			.of(
				"point" struct fields(
					"x" struct fields(numberType),
					"y" struct fields(numberType)))
			.scriptLine
			.assertEqualTo(
				"point" lineTo script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20))))
	}

	@Test
	fun choice_0() {
		0.indexed("hello")
			.of("a" choice cases(textType, numberType))
			.scriptLine
			.assertEqualTo("a" lineTo script(literal("hello")))
	}

	@Test
	fun choice_1() {
		1.indexed(10.number)
			.of("a" choice cases(textType, numberType))
			.scriptLine
			.assertEqualTo("a" lineTo script(literal(10)))
	}
}