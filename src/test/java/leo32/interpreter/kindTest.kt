package leo32.interpreter

import leo.base.assertEqualTo
import leo32.runtime.term
import leo32.runtime.to
import kotlin.test.Test

class KindTest {
	@Test
	fun kindTerm() {
		kind("bit" to kind(either("zero"), either("one")))
			.term
			.assertEqualTo(
				term(
					"bit" to term(
						"either" to term("zero"),
						"either" to term("one"))))
	}
}