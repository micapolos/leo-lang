package leo13.compiler

import leo.base.assertEqualTo
import leo13.pattern.lineTo
import leo13.pattern.pattern
import kotlin.test.Test

class PatternDefinitionsTest {
	@Test
	fun resolve() {
		patternDefinitions()
			.plus(definition("zero" lineTo pattern(), pattern("resolved")))
			.plus(definition("one" lineTo pattern(), pattern("resolved")))
			.resolve("zero" lineTo pattern())
			.assertEqualTo("zero" lineTo pattern("resolved"))
	}
}