package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.*
import leo14.Number
import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		emptyScope
			.plus(definition(rule(numberType2, numberType2) { (it as Number).plus(number(1)) }))
			.apply(numberType2.compiled { number(10) })!!
			.typed
			.assertEqualTo(numberType2.with(number(11)))
	}

	@Test
	fun compiled() {
		emptyScope.compiled(leo(2, "plus"(3))).value.assertEqualTo(number(5))
		emptyScope.compiled(leo("Hello, ", "plus"("world!"))).value.assertEqualTo("Hello, world!")
	}
}