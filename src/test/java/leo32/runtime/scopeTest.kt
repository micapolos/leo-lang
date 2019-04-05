package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo32.base.leafOrNull
import kotlin.test.Test

class ScopeTest {
	@Test
	fun define() {
		empty.scope
			.define(term("not" to term("true")) to function("false"))
			.define(term("not" to term("false")) to function("true"))
			.plus("not" to term("false"))
			.functionTree
			.leafOrNull!!
			.value!!
			.invoke(parameter(term("not")))
			.assertEqualTo(null)
	}
}