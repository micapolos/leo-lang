package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

class BitNegateTest {
	private val zero = treo(at0(treo(leaf)))
	private val one = treo(at1(treo(leaf)))

	private val notOp = treo(at0(treo(at0(treo(leaf)))))
	private val andOp = treo(at0(treo(at1(treo(leaf)))))
	private val orOp = treo(at1(treo(at0(treo(leaf)))))
	private val xorOp = treo(at1(treo(at1(treo(leaf)))))

	private val nandFn =
		fn(treo(
			at0(treo(
				at0(treo(at1(treo(leaf)))),
				at1(treo(at1(treo(leaf)))))),
			at1(treo(
				at0(treo(at1(treo(leaf)))),
				at1(treo(at0(treo(leaf))))))))

	private val notVar = newVar()

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

	val lhsBitVar = newVar()
	val rhsBitVar = newVar()

	val encodeBitCharFn =
		fn(treo(
			at0(treo(
				at0(treo(
					at1(treo(
						at1(treo(
							at0(treo(
								at0(treo(
									at0(treo(
										at0(zero),
										at1(one)))))))))))))))))

	val encodeOpCharFn =
		fn(treo(
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
										at0(andOp))))))))))))))),
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
									at0(orOp)))))))))))))))

	@Test
	fun charMath() {

	}
}