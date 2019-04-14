package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ScopeTest {
	@Test
	fun define() {
		empty.scope
			.invoke(
				"bit" to script(),
				"has" to script(
					"either" to script("zero"),
					"either" to script("one")),
				"negate" to script("bit" to script("zero")),
				"gives" to script("bit" to script("one")),
				"negate" to script("bit" to script("one")),
				"gives" to script("bit" to script("zero")))
			.scope
			.assertEqualTo(1)
	}
}