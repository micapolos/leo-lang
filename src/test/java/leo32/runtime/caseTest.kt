package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class CaseTest {
	@Test
	fun caseOrNull() {
		("case" to term()).caseOrNull.assertEqualTo(null)
		("case" to term("gives")).caseOrNull.assertEqualTo(null)
		("case" to term("gives" to term("foo"))).caseOrNull.assertEqualTo(null)
		("case" to term("foo" to term(), "gives" to term())).caseOrNull.assertEqualTo(null)
		("case" to term("one" to term(), "gives" to term("two"))).caseOrNull.assertEqualTo(term("one") caseTo term("two"))
		("cas" to term("one" to term(), "gives" to term("two"))).caseOrNull.assertEqualTo(null)
	}
}