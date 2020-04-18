package leo15.core

import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import leo.base.assertNull
import kotlin.test.Test

class OptionalTest {
	@Test
	fun equality() {
		intTyp.absent.assertGives(intTyp.absent)
		10.java.present.assertGives(10.java.present)
		intTyp.absent.assertNotEqualTo(10.java.present)
		10.java.present.assertNotEqualTo(intTyp.absent)
	}

	@Test
	fun logic() {
		intTyp.absent.unsafeOrNull.assertNull
		10.java.present.unsafeOrNull.assertEqualTo(10.java)
	}
}