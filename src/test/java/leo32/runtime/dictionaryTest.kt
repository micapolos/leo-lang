package leo32.runtime

import leo.base.empty
import kotlin.test.Test

class DictionaryTest {
	@Test
	fun foldKeyAndValuePairs() {
		empty
			.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("one"), 1)
	}
}