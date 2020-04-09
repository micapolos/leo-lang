package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.number
import kotlin.test.Test

class ScopeTest {
//	@Test
//	fun apply() {
//		emptyScope
//			.plus(definition(rule(numberType, numberType) { asNumber + 1.number }))
//			.apply(numberType.compiled { 10.number })!!
//			.typed
//			.assertEqualTo(numberType.typed(11.number))
//	}

	@Test
	fun compiled() {
		emptyScope.compiled(leo(2, "plus"(3))).value.assertEqualTo(number(5))
		emptyScope.compiled(leo("Hello, ", "plus"("world!"))).value.assertEqualTo("Hello, world!")
	}
}