package leo13.compiler

import leo.base.assertEqualTo
import leo13.type.lineTo
import leo13.type.type
import kotlin.test.Test

class TypeDefinitionsTest {
	@Test
	fun resolve() {
		typeDefinitions()
			.plus(definition("zero" lineTo type(), type("resolved")))
			.plus(definition("one" lineTo type(), type("resolved")))
			.resolve("zero" lineTo type())
			.assertEqualTo("zero" lineTo type("resolved"))
	}
}