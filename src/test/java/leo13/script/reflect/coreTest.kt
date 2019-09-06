package leo13.script.reflect

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.assertFails

class CoreTest {
	@Test
	fun zeroTest() {
		zeroType
			.scriptLine(zero)
			.assertEqualTo("zero" lineTo script())

		zeroType
			.unsafeValue("zero" lineTo script())
			.assertEqualTo(zero)

		assertFails { zeroType.unsafeValue("one" lineTo script()) }
	}

	@Test
	fun bitTest() {
		bitType
			.scriptLine(zero.bit)
			.assertEqualTo("bit" lineTo script("zero"))

		bitType
			.scriptLine(one.bit)
			.assertEqualTo("bit" lineTo script("one"))

		bitType
			.unsafeValue("bit" lineTo script("zero"))
			.assertEqualTo(zero.bit)

		bitType
			.unsafeValue("bit" lineTo script("one"))
			.assertEqualTo(one.bit)

		assertFails { bitType.unsafeValue("bit" lineTo script("two")) }
		assertFails { bitType.unsafeValue("boolean" lineTo script("zero")) }
	}
}

