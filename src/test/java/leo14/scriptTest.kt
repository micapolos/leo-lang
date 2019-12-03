package leo14

import leo.base.assertEqualTo
import leo13.assertContains
import leo13.base.linesString
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script(
			line(literal(2)),
			line("plus" fieldTo literal(3)))
			.code
			.assertEqualTo("2.plus(3)")

		script(
			line(literal(2.5)),
			line("plus" fieldTo literal(3.5)))
			.code
			.assertEqualTo("2.5.plus(3.5)")

		script(
			"vec" fieldTo script(
				"x" fieldTo literal(1),
				"y" fieldTo literal(2),
				"name" fieldTo literal("my vector")))
			.code
			.assertEqualTo("vec(x(1).y(2).name(\"my vector\"))")
	}

	@Test
	fun indentString() {
		script()
			.indentString
			.assertEqualTo("")

		script("zero")
			.indentString
			.assertEqualTo("zero")

		script("zero", "one")
			.indentString
			.assertEqualTo(linesString("zero", "one"))

		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"))
			.indentString
			.assertEqualTo(linesString("zero", "plus: one"))

		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"),
			"plus" lineTo script("two"))
			.indentString
			.assertEqualTo(
				linesString(
					"zero",
					"plus: one",
					"plus: two"))

		script(
			"point" lineTo script(
				"x" lineTo script("zero")))
			.indentString
			.assertEqualTo("point: x: zero")

		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.indentString
			.assertEqualTo(
				linesString(
					"point",
					"  x: zero",
					"  y: one"))

		script(
			"circle" lineTo script(
				"name" lineTo script(literal("big")),
				"radius" lineTo script(literal(10.2)),
				"center" lineTo script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(15))))))
			.indentString
			.assertEqualTo(
				linesString(
					"circle",
					"  name: \"big\"",
					"  radius: 10.2",
					"  center: point",
					"    x: 10",
					"    y: 15"))

		script(
			"define" lineTo script(
				"my" lineTo script(),
				"favourite" lineTo script(),
				"color" lineTo script(),
				"is" lineTo script("red")))
			.indentString
			.assertEqualTo(linesString("define", "  my", "  favourite", "  color", "  is: red"))

	}

	@Test
	fun tokenStack() {
		script().tokenStack.assertContains()

		script("foo").tokenStack.assertContains(token(begin("foo")), token(end))

		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.2)),
				"y" lineTo script(literal(13.5))))
			.tokenStack
			.assertContains(
				token(begin("point")),
				token(begin("x")),
				token(literal(10.2)),
				token(end),
				token(begin("y")),
				token(literal(13.5)),
				token(end),
				token(end))
	}
}