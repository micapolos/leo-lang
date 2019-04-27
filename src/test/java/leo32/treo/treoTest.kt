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
		treo0(capture(variable, treo1(invoke(treo(unit), variable, 2)))).string.assertEqualTo("0_1.(!)<<")
	}

	val nandTreo
		get() =
			branch(
				branch(
					treo1(treo(unit)),
					treo1(treo(unit))),
				branch(
					treo1(treo(unit)),
					treo0(treo(unit))))

	@Test
	fun nand() {
		nandTreo.invoke("00").assertEqualTo("1")
		nandTreo.invoke("01").assertEqualTo("1")
		nandTreo.invoke("10").assertEqualTo("1")
		nandTreo.invoke("11").assertEqualTo("0")
	}
}

