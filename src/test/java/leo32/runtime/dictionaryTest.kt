package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.string
import org.junit.Test

class DictionaryTest {
	@Test
	fun string() {
		empty.dictionary<Term>()
			.string
			.assertEqualTo("dictionary")

		empty.dictionary<Term>()
			.put(term("zero"), term("0"))
			.put(term("inc" to term("zero")), term("1"))
			.string
			.assertEqualTo("dictionary.put(inc(zero()), 1).put(zero(), 0)")
	}
}