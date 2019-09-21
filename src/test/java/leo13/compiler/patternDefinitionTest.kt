package leo13.compiler

import leo.base.assertEqualTo
import leo13.pattern.choice
import leo13.pattern.lineTo
import leo13.pattern.pattern
import kotlin.test.Test

class PatternDefinitionTest {
	@Test
	fun resolveOrNull() {
		definition(
			"bit" lineTo pattern(),
			pattern(
				choice(
					"zero" lineTo pattern(),
					"one" lineTo pattern())))
			.resolveOrNull("bit" lineTo pattern())
			.assertEqualTo(
				"bit" lineTo pattern(
					choice(
						"zero" lineTo pattern(),
						"one" lineTo pattern())))
	}
}