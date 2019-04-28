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
		treo(capture(newVar()), treo(leaf)).string.assertEqualTo("|_")
		treo(at0(treo(at1(treo(leaf))))).string.assertEqualTo("|01")
		treo(
			capture(newVar()),
			treo(
				call(fn(treo(at0(treo(leaf)))), param(treo(at1(treo(leaf))))),
				treo(capture(newVar()), treo(leaf))))
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
		treo(
			at0(treo(
				at1(treo(leaf)))),
			at1(treo(
				at0(treo(leaf)))))

	@Test
	fun neg() {
		negTreo.invoke("0").assertEqualTo("0")
		negTreo.invoke("1").assertEqualTo("1")
	}

	private val nandTreo =
		treo(
			at0(
				treo(
					at0(treo(at1(treo(leaf)))),
					at1(treo(at1(treo(leaf)))))),
			at1(
				treo(
					at0(treo(at1(treo(leaf)))),
					at1(treo(at0(treo(leaf)))))))

	@Test
	fun nand() {
		nandTreo.invoke("00").assertEqualTo("00")
		nandTreo.invoke("01").assertEqualTo("01")
		nandTreo.invoke("10").assertEqualTo("10")
		nandTreo.invoke("11").assertEqualTo("11")
	}

	private val selfTreo =
		treo(
			at0(treo(at0(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))

	@Test
	fun self() {
		selfTreo.invoke("0").assertEqualTo("0")
		selfTreo.invoke("1").assertEqualTo("1")
	}

	private val dupTreo =
		treo(
			at0(treo(at0(treo(at0(treo(leaf)))))),
			at1(treo(at1(treo(at1(treo(leaf)))))))

	@Test
	fun dup() {
		dupTreo.invoke("0").assertEqualTo("0")
		dupTreo.invoke("1").assertEqualTo("1")
	}

	@Test
	fun expandResolve() {
		treo(expand(macro(treo(at1(treo(leaf)))), param(treo(leaf)))).resolve().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at0(treo(at0(treo(leaf)))))))).resolve().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at0(treo(at1(treo(leaf)))))))).resolve().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at1(treo(at0(treo(leaf)))))))).resolve().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at1(treo(at1(treo(leaf)))))))).resolve().cut.string.assertEqualTo("|0")
	}

	@Test
	fun invokeResolve() {
		treo(call(fn(treo(at0(treo(at0(treo(leaf)))))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at0(treo(at1(treo(leaf)))))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at1(treo(at0(treo(leaf)))))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at1(treo(at1(treo(leaf)))))), param(treo(leaf))), nandTreo).resolve().cut.string.assertEqualTo("|0")
	}

	@Test
	fun capturing() {
		val variable1 = newVar(bit1)
		val variable2 = newVar(bit0)
		val variable3 = newVar(bit0)
		treo(capture(variable1), treo(capture(variable2), treo(capture(variable3), treo(leaf))))
			.invoke("01")
			.assertEqualTo("01")
		variable1.bit.assertEqualTo(bit0)
		variable2.bit.assertEqualTo(bit1)
		variable3.bit.assertEqualTo(bit0)
	}

	@Test
	fun captureExpand() {
		val variable = newVar()

		treo(capture(variable), treo(expand(macro(negTreo), param(treo(variable, treo(leaf))))))
			.invoke("0")
			.assertEqualTo("0")

		treo(capture(variable), treo(expand(macro(negTreo), param(treo(variable, treo(leaf))))))
			.invoke("1")
			.assertEqualTo("1")
	}

	@Test
	fun negUsingNandTreo() {
		val lhsVar = newVar()
		val rhsVar = newVar()
		val neg =
			treo(
				capture(lhsVar),
				treo(
					call(
						fn(selfTreo),
						param(treo(lhsVar, treo(leaf)))),
					treo(
						capture(rhsVar),
						treo(expand(macro(nandTreo), param(treo(lhsVar, treo(rhsVar, treo(leaf)))))))))

		neg.invoke("0").assertEqualTo("00")
	}

	@Test
	fun captureForever() {
		val variable = newVar()
		val captureForever = treo(capture(variable), treo(back))
		captureForever.invoke("1").assertEqualTo("")
		captureForever.invoke("10").assertEqualTo("")
		captureForever.invoke("101").assertEqualTo("")
	}

	@Test
	fun negateForever() {
		val resultVar = newVar()
		val inputVar = newVar()
		val negateForever = treo(
			capture(resultVar),
			treo(
				capture(inputVar),
				treo(
					call(
						fn(negTreo),
						param(treo(inputVar, treo(leaf)))),
					treo(back.back.back))))
		negateForever.string.assertEqualTo("|__.?(0)<<<")
		negateForever.invoke("01").assertEqualTo("0")
		resultVar.bit.assertEqualTo(bit0)
		negateForever.invoke("010").assertEqualTo("1")
		resultVar.bit.assertEqualTo(bit1)
		negateForever.invoke("0101").assertEqualTo("0")
		resultVar.bit.assertEqualTo(bit0)
	}
}
