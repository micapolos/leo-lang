package leo13.script

import leo.base.assertEqualTo
import leo.base.notNullIf
import leo13.*
import org.junit.Test

fun <V : LeoObject> V.assertEqualsToScript(code: String) =
	scriptableBody.assertEqualTo(unsafeScript(code))

fun <V : LeoObject> V.assertEqualsToScriptLine(code: String) =
	scriptableLine.assertEqualTo(code.unsafeScriptLine)

fun <V : LeoObject> V.assertScriptableLineWorks(fn: ScriptLine.() -> V?) =
	scriptableLine.fn().assertEqualTo(this)

fun <V : LeoObject> V.assertScriptableBodyWorks(fn: Script.() -> V?) =
	scriptableBody.fn().assertEqualTo(this)

class ScriptableTest {
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
