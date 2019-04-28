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
		treo(variable(bit0), treo(unit)).string.assertEqualTo("_0")
		treo(variable(bit1), treo(unit)).string.assertEqualTo("_1")
		capture(variable(), treo(unit)).string.assertEqualTo("_0")
		treo0(treo1(treo(unit))).string.assertEqualTo("01")
		capture(
			variable(),
			invoke(
				treo0(treo(unit)),
				treo1(treo(unit)),
				capture(variable(), treo(unit))))
			.string
			.assertEqualTo("_0.0(1)_0")
		treo(back.back.back).string.assertEqualTo("<<<")
	}

	@Test
	fun rewind() {
		val result = negTreo.invoke(bit0)
		result.rewind()
		negTreo.invoke(bit0)
	}

	private val negTreo =
		treo01(
			treo1(treo(unit)),
			treo0(treo(unit)))

	@Test
	fun neg() {
		negTreo.invoke("0").assertEqualTo("1")
		negTreo.invoke("1").assertEqualTo("0")
	}

	private val nandTreo =
		treo01(
			treo01(
					treo1(treo(unit)),
					treo1(treo(unit))),
			treo01(
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
		treo01(
			treo0(treo(unit)),
			treo1(treo(unit)))

	@Test
	fun self() {
		selfTreo.invoke("0").assertEqualTo("0")
		selfTreo.invoke("1").assertEqualTo("1")
	}

	private val dupTreo =
		treo01(
			treo0(treo0(treo(unit))),
			treo1(treo1(treo(unit))))

	@Test
	fun dup() {
		dupTreo.invoke("0").assertEqualTo("00")
		dupTreo.invoke("1").assertEqualTo("11")
	}

	@Test
	fun expandResolve() {
		expand(treo1(treo(unit)), treo(unit)).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo0(treo0(treo(unit)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo0(treo1(treo(unit)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo1(treo0(treo(unit)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo1(treo1(treo(unit)))).resolve().cut.string.assertEqualTo("0")
	}

	@Test
	fun invokeResolve() {
		invoke(treo0(treo0(treo(unit))), treo(unit), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo0(treo1(treo(unit))), treo(unit), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo1(treo0(treo(unit))), treo(unit), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo1(treo1(treo(unit))), treo(unit), nandTreo).resolve().cut.string.assertEqualTo("0")
	}

	@Test
	fun capture() {
		val variable1 = variable(bit1)
		val variable2 = variable(bit0)
		val variable3 = variable(bit0)
		capture(variable1, capture(variable2, capture(variable3, treo(unit))))
			.invoke("01")
			.assertEqualTo("_0")
		variable1.bit.assertEqualTo(bit0)
		variable2.bit.assertEqualTo(bit1)
		variable3.bit.assertEqualTo(bit0)
	}

	@Test
	fun captureExpand() {
		val variable = variable()

		capture(variable, expand(negTreo, treo(variable, treo(unit))))
			.invoke("0")
			.assertEqualTo("1")

		capture(variable, expand(negTreo, treo(variable, treo(unit))))
			.invoke("1")
			.assertEqualTo("0")
	}

	@Test
	fun captureInvoke() {
		val variable = variable()

		capture(variable, invoke(negTreo, treo(variable, treo(unit)), selfTreo))
			.invoke("0")
			.assertEqualTo("1")

		capture(variable, invoke(negTreo, treo(variable, treo(unit)), selfTreo))
			.invoke("1")
			.assertEqualTo("0")
	}

	@Test
	fun negUsingNandTreo() {
		val lhsVar = variable()
		val rhsVar = variable()
		val neg =
			capture(
				lhsVar,
				invoke(
					selfTreo,
					treo(lhsVar, treo(unit)),
					capture(
						rhsVar,
						expand(nandTreo, treo(lhsVar, treo(rhsVar, treo(unit)))))))

		neg.invoke("0").assertEqualTo("1")
	}

	@Test
	fun captureForever() {
		val variable = variable()
		val captureForever = capture(variable, treo(back))
		captureForever.invoke("1").assertEqualTo("_1<")
		captureForever.invoke("10").assertEqualTo("_0<")
		captureForever.invoke("101").assertEqualTo("_1<")
	}

	@Test
	fun negateForever() {
		val variable = variable()
		val negateForever = capture(variable, invoke(negTreo, treo(variable, treo(unit)), treo(back.back)))
		negateForever.string.assertEqualTo("_0.?(_0)<<")
		negateForever.invoke("1").assertEqualTo("_1<")
		negateForever.invoke("10").assertEqualTo("_0<")
		negateForever.invoke("101").assertEqualTo("_1<")
	}
}
