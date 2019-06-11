package leo5

import leo.base.assertEqualTo
import leo.base.empty
import org.junit.Test

class FieldTest {
	@Test
	fun contains() {
		name("bit").of(type(empty)).contains("bit" lineTo value()).assertEqualTo(true)
		name("bit").of(type(empty)).contains("but" lineTo value()).assertEqualTo(false)
	}
}