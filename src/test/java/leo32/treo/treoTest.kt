package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class TreoTest {
	@Test
	fun string() {
		treo(leaf).string.assertEqualTo("|")
		treo(newVar(bit0), treo(leaf)).string.assertEqualTo("|0")
		treo(newVar(bit1), treo(leaf)).string.assertEqualTo("|1")
		capture(newVar(), treo(leaf)).string.assertEqualTo("|_")
		treo0(treo1(treo(leaf))).string.assertEqualTo("|01")
		capture(
			newVar(),
			treo(
				call(fn(treo0(treo(leaf))), param(treo1(treo(leaf)))),
				capture(newVar(), treo(leaf))))
			.string
			.assertEqualTo("|_.0(1)_")
		treo(back.back.back).string.assertEqualTo("|<<<")
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
		negTreo.invoke("0").assertEqualTo("?|1")
		negTreo.invoke("1").assertEqualTo("?|0")
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
		nandTreo.invoke("00").assertEqualTo("??|1")
		nandTreo.invoke("01").assertEqualTo("??|1")
		nandTreo.invoke("10").assertEqualTo("??|1")
		nandTreo.invoke("11").assertEqualTo("??|0")
	}

	private val selfTreo =
		treo01(
			treo0(treo(leaf)),
			treo1(treo(leaf)))

	@Test
	fun self() {
		selfTreo.invoke("0").assertEqualTo("?|0")
		selfTreo.invoke("1").assertEqualTo("?|1")
	}

	private val dupTreo =
		treo01(
			treo0(treo0(treo(leaf))),
			treo1(treo1(treo(leaf))))

	@Test
	fun dup() {
		dupTreo.invoke("0").assertEqualTo("?|00")
		dupTreo.invoke("1").assertEqualTo("?|11")
	}

	@Test
	fun expandResolve() {
		expand(treo1(treo(leaf)), treo(leaf)).resolve().cut.string.assertEqualTo("|1")
		expand(nandTreo, treo0(treo0(treo(leaf)))).resolve().cut.string.assertEqualTo("|1")
		expand(nandTreo, treo0(treo1(treo(leaf)))).resolve().cut.string.assertEqualTo("|1")
		expand(nandTreo, treo1(treo0(treo(leaf)))).resolve().cut.string.assertEqualTo("|1")
		expand(nandTreo, treo1(treo1(treo(leaf)))).resolve().cut.string.assertEqualTo("|0")
	}

	@Test
	fun invokeResolve() {
		treo(call(fn(treo0(treo0(treo(leaf)))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo0(treo1(treo(leaf)))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo1(treo0(treo(leaf)))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo1(treo1(treo(leaf)))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|0")
	}

	@Test
	fun capture() {
		val variable1 = newVar(bit1)
		val variable2 = newVar(bit0)
		val variable3 = newVar(bit0)
		capture(variable1, capture(variable2, capture(variable3, treo(leaf))))
			.invoke("01")
			.assertEqualTo("01|_")
		variable1.bit.assertEqualTo(bit0)
		variable2.bit.assertEqualTo(bit1)
		variable3.bit.assertEqualTo(bit0)
	}

	@Test
	fun captureExpand() {
		val variable = newVar()

		capture(variable, expand(negTreo, treo(variable, treo(leaf))))
			.invoke("0")
			.assertEqualTo("?|1")

		capture(variable, expand(negTreo, treo(variable, treo(leaf))))
			.invoke("1")
			.assertEqualTo("?|0")
	}

	@Test
	fun captureInvoke() {
		val variable = newVar()

		capture(variable, treo(call(fn(negTreo), param(treo(variable, treo(leaf)))), selfTreo))
			.invoke("0")
			.assertEqualTo("0i?|1")

		capture(variable, treo(call(fn(negTreo), param(treo(variable, treo(leaf)))), selfTreo))
			.invoke("1")
			.assertEqualTo("1i?|0")
	}

	@Test
	fun negUsingNandTreo() {
		val lhsVar = newVar()
		val rhsVar = newVar()
		val neg =
			capture(
				lhsVar,
				treo(
					call(
						fn(selfTreo),
						param(treo(lhsVar, treo(leaf)))),
					capture(
						rhsVar,
						expand(nandTreo, treo(lhsVar, treo(rhsVar, treo(leaf)))))))

		neg.invoke("0").assertEqualTo("??|1")
	}

	@Test
	fun captureForever() {
		val variable = newVar()
		val captureForever = capture(variable, treo(back))
		captureForever.invoke("1").assertEqualTo("|_<")
		captureForever.invoke("10").assertEqualTo("|_<")
		captureForever.invoke("101").assertEqualTo("|_<")
	}

	@Test
	fun negateForever() {
		val resultVar = newVar()
		val inputVar = newVar()
		val negateForever = capture(
			resultVar,
			capture(
				inputVar,
				treo(
					call(
						fn(negTreo),
						param(treo(inputVar, treo(leaf)))),
					treo(back.back.back))))
		negateForever.string.assertEqualTo("|__.?(0)<<<")
		negateForever.invoke("01").assertEqualTo("0|_.?(1)<<<")
		resultVar.bit.assertEqualTo(bit0)
		negateForever.invoke("010").assertEqualTo("1|_.?(0)<<<")
		resultVar.bit.assertEqualTo(bit1)
		negateForever.invoke("0101").assertEqualTo("0|_.?(1)<<<")
		resultVar.bit.assertEqualTo(bit0)
	}
}
