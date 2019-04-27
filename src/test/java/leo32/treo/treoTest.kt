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
		treo0(capture(variable, treo1(invoke(treo(unit), variable, treo0(treo(unit)))))).string.assertEqualTo("0_1.(!)0")
	}

	private val nandTreo
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

	@Test
	fun neg() {
		val nand = nandTreo
		val nand1Var = variable(treo(unit))
		val nand0Var = variable(nand1Var)
		val neg = capture(
			nand0Var,
			invoke(
				treo(unit),
				treo(unit),
				capture(
					nand1Var,
					invoke(
						nandTreo,
						nand0Var,
						treo(unit)))))
		neg.invoke("0").assertEqualTo("1")
	}
}
