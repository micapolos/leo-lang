package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.lineTo
import leo13.type.options
import leo13.type.type
import kotlin.test.Test

class TypeDefinitionTest {
	@Test
	fun resolveOrNull() {
		definition(
			"bit" lineTo type(),
			type(
				options(
					"zero" lineTo type(),
					"one" lineTo type())))
			.resolveOrNull("bit" lineTo type())
			.assertEqualTo(
				"bit" lineTo type(
					options(
						"zero" lineTo type(),
						"one" lineTo type())))
	}
}