package leo15.core

import kotlin.test.Test

class WordTest {
	@Test
	fun word() {
		"foo".java.word.javaString.assertGives("foo".java)
	}
}

