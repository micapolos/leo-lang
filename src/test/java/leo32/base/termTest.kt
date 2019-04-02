package leo32.base

import kotlin.test.Test

class TermTest {
	@Test
	fun test() {
		"zero".term.plus("bit".term)
			.plus("and".term.plus("zero".term.plus("bit".term)))
			.plus("gives".term.plus("zero".term.plus("bit".term)))
	}
}