package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class TreoTest {
	@Test
	fun string() {
		treo(leaf).string.assertEqualTo("")
		treo(variable(bit0), treo(leaf)).string.assertEqualTo("_0")
		treo(variable(bit1), treo(leaf)).string.assertEqualTo("_1")
		capture(variable(), treo(leaf)).string.assertEqualTo("_0")
		treo0(treo1(treo(leaf))).string.assertEqualTo("01")
		capture(
			variable(),
			invoke(
				treo0(treo(leaf)),
				treo1(treo(leaf)),
				capture(variable(), treo(leaf))))
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
			treo1(treo(leaf)),
			treo0(treo(leaf)))

	@Test
	fun neg() {
		negTreo.invoke("0").assertEqualTo("1")
		negTreo.invoke("1").assertEqualTo("0")
	}

	private val nandTreo =
		treo01(
			treo01(
				treo1(treo(leaf)),
				treo1(treo(leaf))),
			treo01(
				treo1(treo(leaf)),
				treo0(treo(leaf))))

	@Test
	fun nand() {
		nandTreo.invoke("00").assertEqualTo("1")
		nandTreo.invoke("01").assertEqualTo("1")
		nandTreo.invoke("10").assertEqualTo("1")
		nandTreo.invoke("11").assertEqualTo("0")
	}

	private val selfTreo =
		treo01(
			treo0(treo(leaf)),
			treo1(treo(leaf)))

	@Test
	fun self() {
		selfTreo.invoke("0").assertEqualTo("0")
		selfTreo.invoke("1").assertEqualTo("1")
	}

	private val dupTreo =
		treo01(
			treo0(treo0(treo(leaf))),
			treo1(treo1(treo(leaf))))

	@Test
	fun dup() {
		dupTreo.invoke("0").assertEqualTo("00")
		dupTreo.invoke("1").assertEqualTo("11")
	}

	@Test
	fun expandResolve() {
		expand(treo1(treo(leaf)), treo(leaf)).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo0(treo0(treo(leaf)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo0(treo1(treo(leaf)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo1(treo0(treo(leaf)))).resolve().cut.string.assertEqualTo("1")
		expand(nandTreo, treo1(treo1(treo(leaf)))).resolve().cut.string.assertEqualTo("0")
	}

	@Test
	fun invokeResolve() {
		invoke(treo0(treo0(treo(leaf))), treo(leaf), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo0(treo1(treo(leaf))), treo(leaf), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo1(treo0(treo(leaf))), treo(leaf), nandTreo).resolve().cut.string.assertEqualTo("1")
		invoke(treo1(treo1(treo(leaf))), treo(leaf), nandTreo).resolve().cut.string.assertEqualTo("0")
	}

	@Test
	fun capture() {
		val variable1 = variable(bit1)
		val variable2 = variable(bit0)
		val variable3 = variable(bit0)
		capture(variable1, capture(variable2, capture(variable3, treo(leaf))))
			.invoke("01")
			.assertEqualTo("_0")
		variable1.bit.assertEqualTo(bit0)
		variable2.bit.assertEqualTo(bit1)
		variable3.bit.assertEqualTo(bit0)
	}

	@Test
	fun captureExpand() {
		val variable = variable()

		capture(variable, expand(negTreo, treo(variable, treo(leaf))))
			.invoke("0")
			.assertEqualTo("1")

		capture(variable, expand(negTreo, treo(variable, treo(leaf))))
			.invoke("1")
			.assertEqualTo("0")
	}

	@Test
	fun captureInvoke() {
		val variable = variable()

		capture(variable, invoke(negTreo, treo(variable, treo(leaf)), selfTreo))
			.invoke("0")
			.assertEqualTo("1")

		capture(variable, invoke(negTreo, treo(variable, treo(leaf)), selfTreo))
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
					treo(lhsVar, treo(leaf)),
					capture(
						rhsVar,
						expand(nandTreo, treo(lhsVar, treo(rhsVar, treo(leaf)))))))

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
		val resultVar = variable()
		val inputVar = variable()
		val negateForever = capture(
			resultVar,
			capture(
				inputVar,
				invoke(
					negTreo,
					treo(inputVar, treo(leaf)),
					treo(back.back.back))))
		negateForever.string.assertEqualTo("_0_0.?(_0)<<<")
		negateForever.invoke("01").assertEqualTo("_1.?(_1)<<<")
		resultVar.bit.assertEqualTo(bit0)
		negateForever.invoke("010").assertEqualTo("_0.?(_0)<<<")
		resultVar.bit.assertEqualTo(bit1)
		negateForever.invoke("0101").assertEqualTo("_1.?(_1)<<<")
		resultVar.bit.assertEqualTo(bit0)
	}
}
