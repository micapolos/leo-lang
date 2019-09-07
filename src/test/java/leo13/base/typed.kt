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
		boolType
			.scriptLine(true)
			.assertEqualTo("boolean" lineTo script("true"))

		boolType
			.scriptLine(false)
			.assertEqualTo("boolean" lineTo script("false"))

		boolType
			.unsafeValue("boolean" lineTo script("true"))
			.assertEqualTo(true)

		boolType
			.unsafeValue("boolean" lineTo script("false"))
			.assertEqualTo(false)

		assertFails { boolType.unsafeValue("boolean" lineTo script("no")) }
		assertFails { boolType.unsafeValue("bool" lineTo script("true")) }
	}
}