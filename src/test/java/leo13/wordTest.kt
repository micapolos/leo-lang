package leo13

import leo.base.assertEqualTo
import leo.base.assertFails
import org.junit.Test

class WordTest {
	@Test
	fun string() {
		word("foo").toString().assertEqualTo("foo")
		assertFails { word("") }
		assertFails { word("utf8") }
	}
}