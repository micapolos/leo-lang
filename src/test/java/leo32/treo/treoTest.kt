package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import leo32.base.unit
import kotlin.test.Test

class TreoTest {
	@Test
	fun string() {
		treo(unit).string.assertEqualTo("")
		treo(variable(bit0), treo(unit)).string.assertEqualTo("0")
		treo(variable(bit1), treo(unit)).string.assertEqualTo("1")
		capture(variable(), treo(unit)).string.assertEqualTo("_")
		treo0(treo1(treo(unit))).string.assertEqualTo("01")
		capture(
			variable(),
			invoke(
				treo0(treo(unit)),
				treo1(treo(unit)),
				capture(variable(), treo(unit))))
			.string
			.assertEqualTo("_.0(1)_")
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

	private val selfTreo =
		branch(
			treo0(treo(unit)),
			treo1(treo(unit)))

	@Test
	fun self() {
		selfTreo.invoke("0").assertEqualTo("0")
		selfTreo.invoke("1").assertEqualTo("1")
	}

	private val dupTreo =
		branch(
			treo0(treo0(treo(unit))),
			treo1(treo1(treo(unit))))

	@Test
	fun dup() {
		dupTreo.invoke("0").assertEqualTo("00")
		dupTreo.invoke("1").assertEqualTo("11")
	}

	@Test
	fun expand() {
		expand(treo1(treo(unit)), treo(unit)).resolve().string.assertEqualTo("1")
		expand(nandTreo, treo0(treo0(treo(unit)))).resolve().string.assertEqualTo("1")
		expand(nandTreo, treo0(treo1(treo(unit)))).resolve().string.assertEqualTo("1")
		expand(nandTreo, treo1(treo0(treo(unit)))).resolve().string.assertEqualTo("1")
		expand(nandTreo, treo1(treo1(treo(unit)))).resolve().string.assertEqualTo("0")
	}

	@Test
	fun negTreo() {
		val lhsVar = variable()
		val rhsVar = variable()
		val neg =
			capture(
				lhsVar,
				invoke(
					dupTreo,
					treo(lhsVar, treo(unit)),
					capture(
						rhsVar,
						expand(nandTreo, treo(lhsVar, treo(rhsVar, treo(unit)))))))

		neg.invoke("0").assertEqualTo("1")
	}
}
