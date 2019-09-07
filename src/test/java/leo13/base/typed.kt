package leo13.base

import leo.base.assertEqualTo
import leo13.base.type.scriptLine
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.assertFails

class TypedTest {
	@Test
	fun boolean() {
		booleanType
			.scriptLine(true)
			.assertEqualTo("boolean" lineTo script("true"))

		booleanType
			.scriptLine(false)
			.assertEqualTo("boolean" lineTo script("false"))

		booleanType
			.unsafeValue("boolean" lineTo script("true"))
			.assertEqualTo(true)

		booleanType
			.unsafeValue("boolean" lineTo script("false"))
			.assertEqualTo(false)

		assertFails { booleanType.unsafeValue("boolean" lineTo script("no")) }
		assertFails { booleanType.unsafeValue("bool" lineTo script("true")) }
	}
}