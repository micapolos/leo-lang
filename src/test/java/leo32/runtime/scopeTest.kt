package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import leo32.base.leafOrNull
import kotlin.test.Test

class ScopeTest {
	@Test
	fun define() {
		val booleanType = type(either("true"), either("false"))

		empty.scope
			.define(term("not" to term("true")) to (booleanType to template("false")))
			.define(term("not" to term("false")) to (booleanType to template("true")))
			.plus("not" to term("false"))
			.functionTree
			.leafOrNull!!
			.value!!
			.invoke(parameter(term("not")))
			.assertEqualTo(term("true"))
	}
}