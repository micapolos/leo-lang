package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.base.throwableOrNull
import leo.base.tryRun
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class TreoTest {
	@Test
	fun string() {
		treo(leaf).string.assertEqualTo("|")
		treo(newVar(bit0), treo(leaf)).string.assertEqualTo("|?")
		treo(newVar(bit1), treo(leaf)).string.assertEqualTo("|?")
		treo(newVar(bit0), treo(leaf)).enter(bit0).string.assertEqualTo("0|")
		treo(newVar(bit0), treo(leaf)).enter(bit1).string.assertEqualTo("1|")
		treo(newVar(bit0), treo(leaf)).enter(bit0)!!.exitTrace!!.string.assertEqualTo("|0")
		treo(newVar(bit0), treo(leaf)).enter(bit1)!!.exitTrace!!.string.assertEqualTo("|1")
		treo(at0(treo(at1(treo(leaf))))).string.assertEqualTo("|01")
		treo(back.back.back).string.assertEqualTo("|<<<")
		treo(edit).string.assertEqualTo("|#")
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
		treo(expand(macro(treo(at1(treo(leaf)))), param(treo(leaf)))).resolveOnce().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at0(treo(at0(treo(leaf)))))))).resolveOnce().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at0(treo(at1(treo(leaf)))))))).resolveOnce().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at1(treo(at0(treo(leaf)))))))).resolveOnce().cut.string.assertEqualTo("|1")
		treo(expand(macro(nandTreo), param(treo(at1(treo(at1(treo(leaf)))))))).resolveOnce().cut.string.assertEqualTo("|0")
	}

	@Test
	fun invokeResolve() {
		treo(call(fn(treo(at0(treo(at0(treo(leaf)))))), param(treo(leaf))), nandTreo).resolveOnce().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at0(treo(at1(treo(leaf)))))), param(treo(leaf))), nandTreo).resolveOnce().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at1(treo(at0(treo(leaf)))))), param(treo(leaf))), nandTreo).resolveOnce().cut.string.assertEqualTo("|1")
		treo(call(fn(treo(at1(treo(at1(treo(leaf)))))), param(treo(leaf))), nandTreo).resolveOnce().cut.string.assertEqualTo("|0")
	}

	@Test
	fun capturing() {
		val variable1 = newVar(bit1)
		val variable2 = newVar(bit0)
		val variable3 = newVar(bit0)
		treo(variable1, treo(variable2, treo(variable3, treo(leaf))))
			.invoke("01")
			.assertEqualTo("01")
		variable1.bit.assertEqualTo(bit0)
		variable2.bit.assertEqualTo(bit1)
		variable3.bit.assertEqualTo(bit0)
	}

	@Test
	fun captureExpand() {
		val variable = newVar()

		treo(variable, treo(expand(macro(negTreo), param(treo(variable, treo(leaf))))))
			.invoke("0")
			.assertEqualTo("0")

		treo(variable, treo(expand(macro(negTreo), param(treo(variable, treo(leaf))))))
			.invoke("1")
			.assertEqualTo("1")
	}

	@Test
	fun negUsingNandTreo() {
		val lhsVar = newVar()
		val rhsVar = newVar()
		val neg =
			treo(
				lhsVar,
				treo(
					call(
						fn(selfTreo),
						param(treo(lhsVar, treo(leaf)))),
					treo(
						rhsVar,
						treo(expand(macro(nandTreo), param(treo(lhsVar, treo(rhsVar, treo(leaf)))))))))

		neg.invoke("0").assertEqualTo("00")
	}

	@Test
	fun captureForever() {
		val variable = newVar()
		val captureForever = treo(variable, treo(back))
		captureForever.invoke("1").assertEqualTo("")
		captureForever.invoke("10").assertEqualTo("")
		captureForever.invoke("101").assertEqualTo("")
	}

	@Test
	fun resolve() {
		treo(leaf).resolve().assertEqualTo(treo(leaf))
		treo(
			call(fn(treo(leaf)), param(treo(leaf))),
			treo(leaf)).resolveOnce().assertEqualTo(treo(leaf))
		treo(
			call(fn(treo(leaf)), param(treo(leaf))),
			treo(
				call(fn(treo(leaf)), param(treo(leaf))),
				treo(leaf))).resolveOnce().assertEqualTo(treo(leaf))
		treo(
			call(fn(treo(leaf)), param(treo(leaf))),
			treo(back))
			.tryRun { resolveOnce() }
			.throwableOrNull::class
			.assertEqualTo(StackOverflowError::class)
	}

	@Test
	fun negateForever() {
		val resultVar = newVar()
		val inputVar = newVar()
		val negateForever = treo(
			resultVar,
			treo(
				inputVar,
				treo(
					call(
						fn(negTreo),
						param(treo(inputVar, treo(leaf)))),
					treo(back.back.back))))
		negateForever.string.assertEqualTo("|?")
		negateForever.invoke("01").assertEqualTo("0")
		resultVar.bit.assertEqualTo(bit0)
		negateForever.invoke("010").assertEqualTo("1")
		resultVar.bit.assertEqualTo(bit1)
		negateForever.invoke("0101").assertEqualTo("0")
		resultVar.bit.assertEqualTo(bit0)
	}
}
