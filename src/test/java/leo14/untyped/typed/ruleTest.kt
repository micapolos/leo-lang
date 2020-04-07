package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.Number
import leo14.number
import leo14.plus
import kotlin.test.Test

class RuleTest {
	@Test
	fun applyCompiled() {
		rule(numberType, numberType) { (it as Number).plus(number(1)) }
			.apply(numberType.compiled { number(10) })!!
			.typed
			.assertEqualTo(numberType.with(number(11)))
	}
}