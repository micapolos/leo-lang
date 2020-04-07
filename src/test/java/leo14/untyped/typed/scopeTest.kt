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
//			.plus(definition(rule(numberType, numberType) { (it as Number).plus(number(1)) }))
//			.apply(numberType.compiled { number(10) })!!
//			.typed
//			.assertEqualTo(numberType.with(number(11)))
//	}

	@Test
	fun compiled() {
		emptyScope.compiled(leo(2, "plus"(3))).value.assertEqualTo(number(5))
		emptyScope.compiled(leo("Hello, ", "plus"("world!"))).value.assertEqualTo("Hello, world!")
	}
}