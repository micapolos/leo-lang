package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

class BitNegateTest {
	private val zeroTreo = treo(at0(treo(leaf)))
	private val oneTreo = treo(at1(treo(leaf)))

	private val notTreo = treo(at0(treo(at0(treo(leaf)))))
	private val andTreo = treo(at0(treo(at1(treo(leaf)))))
	private val orTreo = treo(at1(treo(at0(treo(leaf)))))
	private val xorTreo = treo(at1(treo(at1(treo(leaf)))))

	private val bitMathTreo =
		treo(
			at0(treo(
				at0(treo(
					at0(treo(
						call(fn(oneTreo), param(treo(leaf))),
						treo(back.back.back.back))),
					at1(treo(
						at0(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))))),
				at1(treo(
					at0(treo(
						at0(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))),
					at1(treo(
						at0(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))))))),
			at1(treo(
				at0(treo(
					at0(treo(
						call(fn(zeroTreo), param(treo(leaf))),
						treo(back.back.back.back))),
					at1(treo(
						at0(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))))),
				at1(treo(
					at0(treo(
						at0(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(zeroTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))),
					at1(treo(
						at0(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))),
						at1(treo(
							call(fn(oneTreo), param(treo(leaf))),
							treo(back.back.back.back.back))))))))))

	@Test
	fun bitMath() {
		bitMathTreo.invoke("000").assertEqualTo("1") // !0 => 1
		bitMathTreo.invoke("100").assertEqualTo("0") // !1 => 0

		bitMathTreo.invoke("0010").assertEqualTo("0") // 0&0 => 0
		bitMathTreo.invoke("0011").assertEqualTo("0") // 0&1 => 0
		bitMathTreo.invoke("1010").assertEqualTo("0") // 1&0 => 0
		bitMathTreo.invoke("1011").assertEqualTo("1") // 1&1 => 1

		bitMathTreo.invoke("0100").assertEqualTo("0") // 0^0 => 0
		bitMathTreo.invoke("0101").assertEqualTo("1") // 0^1 => 1
		bitMathTreo.invoke("1100").assertEqualTo("1") // 1^0 => 1
		bitMathTreo.invoke("1101").assertEqualTo("0") // 1^1 => 0

		bitMathTreo.invoke("0110").assertEqualTo("0") // 0|0 => 0
		bitMathTreo.invoke("0111").assertEqualTo("1") // 0|1 => 1
		bitMathTreo.invoke("1110").assertEqualTo("1") // 1|0 => 1
		bitMathTreo.invoke("1111").assertEqualTo("1") // 1|1 => 1

		bitMathTreo.invoke("00000").assertEqualTo("0") // !!0 => 0
		bitMathTreo.invoke("0000000").assertEqualTo("1") // !!!0 => 1
		bitMathTreo.invoke("000010").assertEqualTo("0") // !0&0 => 0
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
										at0(zeroTreo),
										at1(oneTreo)))))))))))))))))

	val encodeOpCharFn =
		fn(treo(
			at0(treo(
				at0(treo(
					at1(treo(
						at0(treo(
							at0(treo(
								at0(treo(
									at0(treo(
										at1(notTreo))))),
								at1(treo(
									at1(treo(
										at0(andTreo))))))))))))))),
			at1(treo(
				at0(treo(
					at1(treo(
						at1(treo(
							at1(treo(
								at1(treo(
									at0(xorTreo))))))))))),
				at1(treo(
					at1(treo(
						at1(treo(
							at1(treo(
								at0(treo(
									at0(orTreo)))))))))))))))

	@Test
	fun charMath() {

	}
}