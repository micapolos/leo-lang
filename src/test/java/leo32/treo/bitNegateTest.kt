package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

val nil = treo(leaf)

fun zero(treo: Treo = nil) = treo(at0(treo))
fun one(treo: Treo = nil) = treo(at1(treo))

fun not(treo: Treo = nil) = treo(at0(treo(at0(treo))))
fun and(treo: Treo = nil) = treo(at0(treo(at1(treo))))
fun xor(treo: Treo = nil) = treo(at1(treo(at0(treo))))
fun or(treo: Treo = nil) = treo(at1(treo(at1(treo))))

class BitNegateTest {
	private val zero = treo(at0(treo(leaf)))
	private val one = treo(at1(treo(leaf)))

	private val selfTreo = treo(
		at0(treo(at0(treo(leaf)))),
		at1(treo(at1(treo(leaf)))))

	private val selfFn = fn(selfTreo)

	@Test
	fun self() {
		selfFn.invoke(zero).assertEqualTo(zero)
		selfFn.invoke(one).assertEqualTo(one)
	}

	private val zeroZero = treo(at0(zero))
	private val zeroOne = treo(at0(one))
	private val oneZero = treo(at1(zero))
	private val oneOne = treo(at1(one))

	private val dupTreo = treo(
		at0(zeroZero),
		at1(oneOne))

	private val dupFn = fn(dupTreo)

	@Test
	fun dup() {
		dupFn.invoke(zero).assertEqualTo(zeroZero)
		dupFn.invoke(one).assertEqualTo(oneOne)
	}

	private val nandTreo = treo(
		at0(treo(
			at0(treo(at1(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))),
		at1(treo(
			at0(treo(at1(treo(leaf)))),
			at1(treo(at0(treo(leaf)))))))

	private val nandFn = fn(nandTreo)

	@Test
	fun nand() {
		nandFn.invoke(zeroZero).assertEqualTo(one)
		nandFn.invoke(zeroOne).assertEqualTo(one)
		nandFn.invoke(oneZero).assertEqualTo(one)
		nandFn.invoke(oneOne).assertEqualTo(zero)
	}

	private val negVar = variable()
	private val negTreo = treo(capture(negVar), treo(
		call(dupFn, param(treo(value(negVar), treo(leaf)))),
		nandTreo))
	private val negFn = fn(negTreo)

	@Test
	fun neg() {
		negFn.invoke(zero).assertEqualTo(one)
		negFn.invoke(one).assertEqualTo(zero)
	}

	private val andLhsVar = variable()
	private val andRhsVar = variable()
	private val andUsingNandTreo = treo(capture(andLhsVar), treo(capture(andRhsVar), treo(
		call(nandFn, param(treo(value(andLhsVar), treo(value(andRhsVar), treo(leaf))))),
		treo(capture(andLhsVar), treo(
			call(negFn, param(treo(value(andLhsVar), treo(leaf)))),
			selfTreo)))))
	private val andUsingNandFn = fn(andUsingNandTreo)

	@Test
	fun andUsingNand() {
		andUsingNandFn.invoke(zeroZero).assertEqualTo(zero)
		andUsingNandFn.invoke(zeroOne).assertEqualTo(zero)
		andUsingNandFn.invoke(oneZero).assertEqualTo(zero)
		andUsingNandFn.invoke(oneOne).assertEqualTo(one)
	}

	private val andTreo = treo(
		at0(treo(
			at0(treo(at0(treo(leaf)))),
			at1(treo(at0(treo(leaf)))))),
		at1(treo(
			at0(treo(at0(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))))
	private val andFn = fn(andTreo)

	@Test
	fun and() {
		andFn.invoke(zeroZero).assertEqualTo(zero)
		andFn.invoke(zeroOne).assertEqualTo(zero)
		andFn.invoke(oneZero).assertEqualTo(zero)
		andFn.invoke(oneOne).assertEqualTo(one)
	}

	private val orTreo = treo(
		at0(treo(
			at0(treo(at0(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))),
		at1(treo(
			at0(treo(at1(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))))
	private val orFn = fn(orTreo)

	@Test
	fun or() {
		orFn.invoke(zeroZero).assertEqualTo(zero)
		orFn.invoke(zeroOne).assertEqualTo(one)
		orFn.invoke(oneZero).assertEqualTo(one)
		orFn.invoke(oneOne).assertEqualTo(one)
	}

	private val xorTreo = treo(
		at0(treo(
			at0(treo(at0(treo(leaf)))),
			at1(treo(at1(treo(leaf)))))),
		at1(treo(
			at0(treo(at1(treo(leaf)))),
			at1(treo(at0(treo(leaf)))))))
	private val xorFn = fn(xorTreo)

	@Test
	fun xor() {
		xorFn.invoke(zeroZero).assertEqualTo(zero)
		xorFn.invoke(zeroOne).assertEqualTo(one)
		xorFn.invoke(oneZero).assertEqualTo(one)
		xorFn.invoke(oneOne).assertEqualTo(zero)
	}

	private val zeroChar = treo(
		at0(treo(
			at0(treo(
				at1(treo(
					at1(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at0(treo(leaf)))))))))))))))))

	private val oneChar = treo(
		at0(treo(
			at0(treo(
				at1(treo(
					at1(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at1(treo(leaf)))))))))))))))))

	private val encodeBitTreo = treo(
		at0(treo(
			at0(treo(
				at1(treo(
					at1(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at0(zero),
									at1(one))))))))))))))))
	private val encodeBitFn = fn(encodeBitTreo)

	@Test
	fun encodeBit() {
		encodeBitFn.invoke(zeroChar).assertEqualTo(zero)
		encodeBitFn.invoke(oneChar).assertEqualTo(one)
	}

	private val decodeBitTreo = treo(at0(zeroChar), at1(oneChar))
	private val decodeBitFn = fn(decodeBitTreo)

	@Test
	fun decodeBit() {
		decodeBitFn.invoke(zero).assertEqualTo(zeroChar)
		decodeBitFn.invoke(one).assertEqualTo(oneChar)
	}

	val notChar = treo(
		at0(treo(
			at0(treo(
				at1(treo(
					at0(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at1(treo(leaf)))))))))))))))))

	val andChar = treo(
		at0(treo(
			at0(treo(
				at1(treo(
					at0(treo(
						at0(treo(
							at1(treo(
								at1(treo(
									at0(treo(leaf)))))))))))))))))

	val orChar = treo(
		at0(treo(
			at1(treo(
				at1(treo(
					at1(treo(
						at1(treo(
							at1(treo(
								at0(treo(
									at0(treo(leaf)))))))))))))))))

	val xorChar = treo(
		at0(treo(
			at1(treo(
				at0(treo(
					at1(treo(
						at1(treo(
							at1(treo(
								at1(treo(
									at0(treo(leaf)))))))))))))))))

	private val notOp = treo(at0(treo(at0(treo(leaf)))))
	private val andOp = treo(at0(treo(at1(treo(leaf)))))
	private val orOp = treo(at1(treo(at0(treo(leaf)))))
	private val xorOp = treo(at1(treo(at1(treo(leaf)))))

	private val encodeOpTreo =
		treo(
			at0(treo(
				at0(treo(
					at1(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at0(treo(
										at1(notOp))))),
								at1(treo(
									at1(treo(
										at0(andOp))))))))))))),
				at1(treo(
					at0(treo(
						at1(treo(
							at1(treo(
								at1(treo(
									at1(treo(
										at0(xorOp))))))))))),
					at1(treo(
						at1(treo(
							at1(treo(
								at1(treo(
									at0(treo(
										at0(orOp))))))))))))))))
	private val encodeOpFn = fn(encodeOpTreo)

	@Test
	fun encodeOp() {
		encodeOpFn.invoke(notChar).assertEqualTo(notOp)
		encodeOpFn.invoke(andChar).assertEqualTo(andOp)
		encodeOpFn.invoke(orChar).assertEqualTo(orOp)
		encodeOpFn.invoke(xorChar).assertEqualTo(xorOp)
	}

	private val decodeOpTreo = treo(
		at0(treo(
			at0(notChar),
			at1(andChar))),
		at1(treo(
			at0(orChar),
			at1(xorChar))))
	private val decodeOpFn = fn(decodeOpTreo)

	@Test
	fun decodeOp() {
		decodeOpFn.invoke(notOp).assertEqualTo(notChar)
		decodeOpFn.invoke(andOp).assertEqualTo(andChar)
		decodeOpFn.invoke(orOp).assertEqualTo(orChar)
		decodeOpFn.invoke(xorOp).assertEqualTo(xorChar)
	}

	private val bitMathTreo =
		treo(
			at0(treo(
				at0(treo(
					at0(one),
					at1(treo(
						at0(zero),
						at1(zero))))),
				at1(treo(
					at0(treo(
						at0(zero),
						at1(one))),
					at1(treo(
						at0(zero),
						at1(one))))))),
			at1(treo(
				at0(treo(
					at0(zero),
					at1(treo(
						at0(zero),
						at1(one))))),
				at1(treo(
					at0(treo(
						at0(one),
						at1(zero))),
					at1(treo(
						at0(one),
						at1(one))))))))
	private val bitMathFn = fn(bitMathTreo)

	@Test
	fun bitMath() {
		bitMathFn.invoke(zero(not())).assertEqualTo(one)
		bitMathFn.invoke(one(not())).assertEqualTo(zero)

		bitMathFn.invoke(zero(and(zero()))).assertEqualTo(zero())
		bitMathFn.invoke(zero(and(one()))).assertEqualTo(zero())
		bitMathFn.invoke(one(and(zero()))).assertEqualTo(zero())
		bitMathFn.invoke(one(and(one()))).assertEqualTo(one())

		bitMathFn.invoke(zero(or(zero()))).assertEqualTo(zero())
		bitMathFn.invoke(zero(or(one()))).assertEqualTo(one())
		bitMathFn.invoke(one(or(zero()))).assertEqualTo(one())
		bitMathFn.invoke(one(or(one()))).assertEqualTo(one())

		bitMathFn.invoke(zero(xor(zero()))).assertEqualTo(zero())
		bitMathFn.invoke(zero(xor(one()))).assertEqualTo(one())
		bitMathFn.invoke(one(xor(zero()))).assertEqualTo(one())
		bitMathFn.invoke(one(xor(one()))).assertEqualTo(zero())
	}

	private val charVar0 = variable()
	private val charVar1 = variable()
	private val charVar2 = variable()
	private val charVar3 = variable()
	private val charVar4 = variable()
	private val charVar5 = variable()
	private val charVar6 = variable()
	private val charVar7 = variable()
	private val charVar =
		treo(capture(charVar0),
			treo(capture(charVar1),
				treo(capture(charVar2),
					treo(capture(charVar3),
						treo(capture(charVar4),
							treo(capture(charVar5),
								treo(capture(charVar6),
									treo(capture(charVar7),
										treo(leaf)))))))))

	fun charTreo(treo: Treo) =
		treo(value(charVar0),
			treo(value(charVar1),
				treo(value(charVar2),
					treo(value(charVar3),
						treo(value(charVar4),
							treo(value(charVar5),
								treo(value(charVar6),
									treo(value(charVar7),
										treo))))))))

	private val lhsVar = variable()
	private val rhsVar = variable()

	private val charMath =
		charTreo(treo(
			call(encodeBitFn, param(charVar)),
			treo(capture(lhsVar),
				charTreo(treo(
					call(encodeOpFn, param(charVar)),
					treo(
						at0(treo(
							at0(treo(
								call(negFn, param(treo(value(lhsVar), treo(leaf)))),
								treo(capture(lhsVar), treo(
									call(decodeBitFn, param(treo(value(lhsVar), treo(leaf)))),
									treo(back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back.back))))),
							at1(charTreo(treo(
								call(encodeBitFn, param(charVar)),
								treo(capture(rhsVar), treo(
									call(andFn, param(treo(value(lhsVar), treo(value(rhsVar), treo(leaf))))),
									treo(capture(lhsVar), treo(
										call(decodeBitFn, param(treo(value(lhsVar), treo(leaf)))),
										treo(back.back.back.back.back.back.back.back)))))))))),
						at1(treo(
							at0(charTreo(treo(
								call(encodeBitFn, param(charVar)),
								treo(capture(rhsVar), treo(
									call(andFn, param(treo(value(lhsVar), treo(value(rhsVar), treo(leaf))))),
									treo(capture(lhsVar), treo(
										call(decodeBitFn, param(treo(value(lhsVar), treo(leaf)))),
										treo(back.back.back.back)))))))),
							at1(charTreo(treo(
								call(encodeBitFn, param(charVar)),
								treo(capture(rhsVar), treo(
									call(andFn, param(treo(value(lhsVar), treo(value(rhsVar), treo(leaf))))),
									treo(capture(lhsVar), treo(
										call(decodeBitFn, param(treo(value(lhsVar), treo(leaf)))),
										treo(back.back.back.back))))))))))))))))
}