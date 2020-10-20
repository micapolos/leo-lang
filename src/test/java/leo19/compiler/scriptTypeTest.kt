package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.type.giving
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.recurse
import leo19.type.recursive
import leo19.type.type
import kotlin.test.Test
import kotlin.test.assertFails

class ScriptTypeTest {
	@Test
	fun structType() {
		script(
			"x" lineTo script(),
			"y" lineTo script())
			.type
			.assertEqualTo(
				type(
					"x" fieldTo type(),
					"y" fieldTo type()))

	}

	@Test
	fun choiceType() {
		script(
			"choice" lineTo script(
				"zero" lineTo script(),
				"one" lineTo script()))
			.type
			.assertEqualTo(
				choice(
					"zero" caseTo type(),
					"one" caseTo type()))

	}

	@Test
	fun functionType() {
		script(
			"zero" lineTo script(),
			"giving" lineTo script(
				"one" lineTo script()))
			.type
			.assertEqualTo(
				type("zero" fieldTo type()).giving(type("one" fieldTo type())))

	}

	@Test
	fun recursiveEmpty() {
		script("recursive" lineTo script())
			.type
			.assertEqualTo(recursive(type()))
	}

	@Test
	fun recurseOnce() {
		emptyTypeCompiler
			.beginRecursive
			.type(script("recurse"))
			.assertEqualTo(recurse(0))
	}

	@Test
	fun recurseError() {
		assertFails {
			script("recurse").type
		}
	}

	@Test
	fun recursiveChoiceType() {
		script(
			"recursive" lineTo script(
				"choice" lineTo script(
					"zero" lineTo script(),
					"succ" lineTo script("recurse"))))
			.type
			.assertEqualTo(recursive(choice("zero" caseTo type(), "succ" caseTo recurse(0))))
	}
}