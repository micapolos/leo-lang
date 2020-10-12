package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.type.giving
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct
import kotlin.test.Test

class ScriptTypeTest {
	@Test
	fun structType() {
		script(
			"x" lineTo script(),
			"y" lineTo script())
			.type
			.assertEqualTo(
				struct(
					"x" fieldTo struct(),
					"y" fieldTo struct()))

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
					"zero" caseTo struct(),
					"one" caseTo struct()))

	}

	@Test
	fun functionType() {
		script(
			"zero" lineTo script(),
			"giving" lineTo script(
				"one" lineTo script()))
			.type
			.assertEqualTo(
				struct("zero" fieldTo struct()).giving(struct("one" fieldTo struct())))

	}
}