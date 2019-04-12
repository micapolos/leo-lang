package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import org.junit.Test

class DictionaryTest {
	@Test
	fun string() {
		empty.dictionary<Int>()
			.string
			.assertEqualTo("dictionary")

		empty.dictionary<Int>()
			.put(term("zero"), 0)
			.put(term("inc" to term("zero")), 1)
			.string
			.assertEqualTo("dictionary.put(inc zero to int 1).put(zero to int 0)")
	}
}