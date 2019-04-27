package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo32.base.unit
import kotlin.test.Test

class ValueTest {
	@Test
	fun string() {
		treo(unit).string.assertEqualTo("")
		variable(treo(unit)).string.assertEqualTo("!")
		capture(variable(treo(unit)), treo(unit)).string.assertEqualTo("_")
		treo0(treo1(treo(unit))).string.assertEqualTo("01")

		val variable = variable(treo(unit))
		treo0(capture(variable, treo1(invoke(treo(unit), variable)))).string.assertEqualTo("0_1.(!)")
	}
}

