package leo13

import leo.base.assertEqualTo
import leo.base.notNullIf
import leo9.stack
import org.junit.Test

fun <V : Scriptable> V.assertEqualsToScript(code: String) =
	scriptableBody.assertEqualTo(unsafeScript(code))

fun <V : Scriptable> V.assertEqualsToScriptLine(code: String) =
	scriptableLine.assertEqualTo(code.unsafeScriptLine)

fun <V : Scriptable> V.assertAsScriptLineWorks(fn: ScriptLine.() -> V?) =
	scriptableLine.fn().assertEqualTo(this)

class ScriptableTest {
	@Test
	fun asSeparatedScript() {
		stack<Script>()
			.asSeparatedScript("or")
			.assertEqualTo("".unsafeScript)

		stack(script("zero"), script("one"))
			.asSeparatedScript("or")
			.assertEqualTo("script(zero())or(script(one()))".unsafeScript)
	}

	@Test
	fun asScript() {
		stack<Script>()
			.asScript
			.assertEqualTo("null()".unsafeScript)

		stack(script("zero"), script("one"))
			.asScript
			.assertEqualTo("script(zero())script(one())".unsafeScript)
	}

	@Test
	fun asScriptLine() {
		stack<Script>()
			.asScriptLine("stack")
			.assertEqualTo("stack(null())".unsafeScriptLine)

		stack(script("zero"), script("one"))
			.asScriptLine("stack")
			.assertEqualTo("stack(script(zero())script(one()))".unsafeScriptLine)
	}

	@Test
	fun asSeparatedStackOrNull() {
		"".unsafeScript
			.asSeparatedStackOrNull("or") { null }
			.assertEqualTo(stack())

		"zero()or(one())".unsafeScript
			.asSeparatedStackOrNull("or") { name }
			.assertEqualTo(stack("zero", "one"))

		"zero()or(fail())or(one())".unsafeScript
			.asSeparatedStackOrNull("or") { notNullIf(name == "fail") { name } }
			.assertEqualTo(null)
	}

	@Test
	fun asStackOrNull() {
		"".unsafeScript
			.asStackOrNull { null }
			.assertEqualTo(null)

		"null()".unsafeScript
			.asStackOrNull { null }
			.assertEqualTo(stack())

		"zero()one()"
			.unsafeScript
			.asStackOrNull { name }
			.assertEqualTo(stack("zero", "one"))

		"zero()fail()one()"
			.unsafeScript
			.asStackOrNull { notNullIf(name == "fail") { name } }
			.assertEqualTo(null)
	}

	@Test
	fun lineAsStackOrNull() {
		"stack()".unsafeScriptLine
			.asStackOrNull("stack") { null }
			.assertEqualTo(null)

		"stack(null())".unsafeScriptLine
			.asStackOrNull("stack") { null }
			.assertEqualTo(stack())

		"stack(zero()one())"
			.unsafeScriptLine
			.asStackOrNull("stack") { name }
			.assertEqualTo(stack("zero", "one"))

		"list(zero()one())"
			.unsafeScriptLine
			.asStackOrNull("stack") { name }
			.assertEqualTo(null)

		"stack(zero()fail()one())"
			.unsafeScriptLine
			.asStackOrNull("stack") { notNullIf(name == "fail") { name } }
			.assertEqualTo(null)
	}
}
