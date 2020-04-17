package leo15.type

import leo.base.assertEqualTo
import leo.base.assertNull
import leo15.lambda.at
import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		emptyScope
			.plus(type("foo").key bindingTo typed("bar").value)
			.plus(type("zoo").key bindingTo typed("zar").value)
			.run {
				apply(typed("foo")).assertEqualTo(at(1).dynamicExpression of type("bar"))
				apply(typed("zoo")).assertEqualTo(at(0).dynamicExpression of type("zar"))
				apply(typed("goo")).assertNull
			}
	}
}