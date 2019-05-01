package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

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

	private val negVar = newVar()
	private val negTreo = treo(negVar, treo(
		call(dupFn, param(treo(negVar, treo(leaf)))),
		nandTreo))
	private val negFn = fn(negTreo)

	@Test
	fun neg() {
		negFn.invoke(zero).assertEqualTo(one)
		negFn.invoke(one).assertEqualTo(zero)
	}

	private val andLhsVar = newVar()
	private val andRhsVar = newVar()
	private val andUsingNandTreo = treo(andLhsVar, treo(andRhsVar, treo(
		call(nandFn, param(treo(andLhsVar, treo(andRhsVar, treo(leaf))))),
		treo(andLhsVar, treo(
			call(negFn, param(treo(andLhsVar, treo(leaf)))),
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

	private val bitMath =
		treo(
			at0(treo(
				at0(treo(
					at0(treo(
						call(fn(one), param(treo(leaf))),
						treo(back.back.back.back))),
					at1(treo(
						at0(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))))))),
				at1(treo(
					at0(treo(
						at0(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))))),
					at1(treo(
						at0(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))))))))),
			at1(treo(
				at0(treo(
					at0(treo(
						call(fn(zero), param(treo(leaf))),
						treo(back.back.back.back))),
					at1(treo(
						at0(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))))))),
				at1(treo(
					at0(treo(
						at0(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(zero), param(treo(leaf))),
							treo(back.back.back.back.back))))),
					at1(treo(
						at0(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(one), param(treo(leaf))),
							treo(back.back.back.back.back))))))))))

	@Test
	fun bitMath() {
		bitMath.invoke("000").assertEqualTo("1") // !0 => 1
		bitMath.invoke("100").assertEqualTo("0") // !1 => 0

		bitMath.invoke("0010").assertEqualTo("0") // 0&0 => 0
		bitMath.invoke("0011").assertEqualTo("0") // 0&1 => 0
		bitMath.invoke("1010").assertEqualTo("0") // 1&0 => 0
		bitMath.invoke("1011").assertEqualTo("1") // 1&1 => 1

		bitMath.invoke("0100").assertEqualTo("0") // 0^0 => 0
		bitMath.invoke("0101").assertEqualTo("1") // 0^1 => 1
		bitMath.invoke("1100").assertEqualTo("1") // 1^0 => 1
		bitMath.invoke("1101").assertEqualTo("0") // 1^1 => 0

		bitMath.invoke("0110").assertEqualTo("0") // 0|0 => 0
		bitMath.invoke("0111").assertEqualTo("1") // 0|1 => 1
		bitMath.invoke("1110").assertEqualTo("1") // 1|0 => 1
		bitMath.invoke("1111").assertEqualTo("1") // 1|1 => 1

		bitMath.invoke("00000").assertEqualTo("0") // !!0 => 0
		bitMath.invoke("0000000").assertEqualTo("1") // !!!0 => 1
		bitMath.invoke("000010").assertEqualTo("0") // !0&0 => 0
	}
}