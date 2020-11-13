package leo22.term

import leo.base.assertEqualTo
import leo22.dsl.*
import kotlin.test.Test

class ValueTest {
	@Test
	fun valueApply() {
		value(function(scope(), term(variable(number(0)))))
			.valueApply(value(native(text("Hello, world!"))))
			.assertEqualTo(value(native(text("Hello, world!"))))
	}
}
