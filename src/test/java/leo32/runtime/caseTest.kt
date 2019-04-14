package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class CaseTest {
	@Test
	fun caseOrNull() {
		("case" to term()).caseOrNull.assertEqualTo(null)
		("case" to term("to")).caseOrNull.assertEqualTo(null)
		("case" to term("to" to term("foo"))).caseOrNull.assertEqualTo(null)
		("case" to term("foo" to term(), "to" to term())).caseOrNull.assertEqualTo(null)
		("case" to term("one" to term(), "to" to term("two"))).caseOrNull.assertEqualTo(term("one") caseTo term("two"))
		("cas" to term("one" to term(), "to" to term("two"))).caseOrNull.assertEqualTo(null)
	}
}