package leo13.script

import leo.base.assertEqualTo
import leo13.stack
import leo13.token.closing
import leo13.token.opening
import kotlin.test.Test

class ScriptTest {
	@Test
	fun code() {
		script().code.assertEqualTo("")
		script("one" lineTo script()).code.assertEqualTo("one()")
		script("one" lineTo script(), "two" lineTo script()).code.assertEqualTo("one()two()")
		script("one" lineTo script("two" lineTo script())).code.assertEqualTo("one(two())")
	}

	@Test
	fun indentedCode() {
		script().indentedCode.assertEqualTo("")

		script("one" lineTo script())
			.indentedCode
			.assertEqualTo("one")

		script("one" lineTo script(), "two" lineTo script())
			.indentedCode
			.assertEqualTo("one two")

		script("one" lineTo script("two" lineTo script()))
			.indentedCode
			.assertEqualTo("one: two")

		script("one" lineTo script(), "two" lineTo script("three" lineTo script()))
			.indentedCode
			.assertEqualTo("one\ntwo: three")

		script("one" lineTo script("two" lineTo script(), "three" lineTo script()))
			.indentedCode
			.assertEqualTo("one: two three")

		script("one" lineTo script("two" lineTo script(), "plus" lineTo script("three" lineTo script())))
			.indentedCode
			.assertEqualTo("one\n\ttwo\n\tplus: three")

		script("x" lineTo script("one" lineTo script()), "y" lineTo script("two" lineTo script()))
			.indentedCode
			.assertEqualTo("x: one\ny: two")

		script("vec" lineTo script("x" lineTo script("one" lineTo script()), "y" lineTo script("two" lineTo script())))
			.indentedCode
			.assertEqualTo("vec\n\tx: one\n\ty: two")

		script(
			"one" lineTo script(),
			"gives" lineTo script("jeden"),
			"two" lineTo script(),
			"gives" lineTo script("dwa"))
			.indentedCode
			.assertEqualTo("one\ngives: jeden\ntwo\ngives: dwa")

		script(
			"center" lineTo script(
				"point" lineTo script(
					"x" lineTo script("one" lineTo script()),
					"y" lineTo script("two" lineTo script()))))
			.indentedCode
			.assertEqualTo("center: point\n\tx: one\n\ty: two")

	}

	@Test
	fun normalize() {
		script()
			.normalize
			.assertEqualTo(script())

		script("one" lineTo script())
			.normalize
			.assertEqualTo(script("one" lineTo script()))

		script("two" lineTo script("one" lineTo script()))
			.normalize
			.assertEqualTo(script("two" lineTo script("one" lineTo script())))

		script("one" lineTo script(), "two" lineTo script())
			.normalize
			.assertEqualTo(script("two" lineTo script("one" lineTo script())))

		script("one" lineTo script(), "two" lineTo script(), "three" lineTo script())
			.normalize
			.assertEqualTo(script("three" lineTo script("two" lineTo script("one" lineTo script()))))
	}

	@Test
	fun emptyHeadPlus() {
		val head = scriptHead()

		head
			.plus(opening("foo"))
			.assertEqualTo(head(stack(script() openerTo opening("foo")), script()))

		head
			.plus(closing)
			.assertEqualTo(null)
	}

	@Test
	fun nonEmptyHeadPlus() {
		val head = head(
			stack(
				script("one" lineTo script()) openerTo opening("plus"),
				script("two" lineTo script()) openerTo opening("times")),
			script("three" lineTo script()))

		head
			.plus(opening("negate"))
			.assertEqualTo(head(
				stack(
					script("one" lineTo script()) openerTo opening("plus"),
					script("two" lineTo script()) openerTo opening("times"),
					script("three" lineTo script()) openerTo opening("negate")),
				script()))

		head
			.plus(closing)
			.assertEqualTo(head(
				stack(script("one" lineTo script()) openerTo opening("plus")),
				script(
					"two" lineTo script(),
					"times" lineTo script("three" lineTo script()))))
	}

	@Test
	fun unsafeScript() {
		leo13.script.unsafeScript("")
			.assertEqualTo(script())

		leo13.script.unsafeScript("one()")
			.assertEqualTo(script("one" lineTo script()))

		leo13.script.unsafeScript("one()two()")
			.assertEqualTo(script("one" lineTo script(), "two" lineTo script()))

		leo13.script.unsafeScript("one(two())")
			.assertEqualTo(script("one" lineTo script("two" lineTo script())))

		leo13.script.unsafeScript("one()plus(two())")
			.assertEqualTo(script("one" lineTo script(), "plus" lineTo script("two" lineTo script())))
	}

	@Test
	fun stackAsScriptLine() {
		stack<Int>()
			.asScriptLine("ints") { toString() lineTo script() }
			.assertEqualTo("ints" lineTo nullScript)

		stack(1, 2)
			.asScriptLine("ints") { toString() lineTo script() }
			.assertEqualTo("ints" lineTo script(
				"1" lineTo script(),
				"2" lineTo script()))
	}
}