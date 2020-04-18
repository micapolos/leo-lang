package leo15.type.core

import leo15.core.java
import leo15.core.word
import kotlin.test.Test

class WordTest {
	@Test
	fun word() {
		"foo".java.word.javaString.assertGives("foo".java)
	}
}

