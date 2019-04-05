package leo32.dsl

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.*
import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		val scope = empty.scope
			.define(term("not" to term("zero")) to function("one"))
			.define(term("not" to term("one")) to function("zero"))

		scope.eval(zero).assertEqualTo(term(zero))
		scope.eval(not(zero)).assertEqualTo(term(one))
		scope.eval(not(one)).assertEqualTo(term(zero))
		scope.eval(not(not(zero))).assertEqualTo(term(zero))
	}
}