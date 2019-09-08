package leo13

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class LetterTest {
	@Test
	fun char() {
		letter('a').char.assertEqualTo('a')
		letter('z').char.assertEqualTo('z')
		assertFails { letter('0') }
	}
}