package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

val lhsBitVar = newVar()
val rhsBitVar = newVar()

val negFn =
	fn(treo(
		at0(treo(at1(treo(leaf)))),
		at1(treo(at0(treo(leaf))))))

val zeroTreo = treo(at0(treo(leaf)))
val oneTreo = treo(at1(treo(leaf)))

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

val notTreo = treo(at0(treo(at0(treo(leaf)))))
val andTreo = treo(at0(treo(at1(treo(leaf)))))
val orTreo = treo(at1(treo(at0(treo(leaf)))))
val xorTreo = treo(at1(treo(at1(treo(leaf)))))

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

val mathTreo =
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

class BitNegateTest {
	@Test
	fun testMathTreo() {
		mathTreo.invoke("000").assertEqualTo("1") // !0 => 1
		mathTreo.invoke("100").assertEqualTo("0") // !1 => 0

		mathTreo.invoke("0010").assertEqualTo("0") // 0&0 => 0
		mathTreo.invoke("0011").assertEqualTo("0") // 0&1 => 0
		mathTreo.invoke("1010").assertEqualTo("0") // 1&0 => 0
		mathTreo.invoke("1011").assertEqualTo("1") // 1&1 => 1

		mathTreo.invoke("0100").assertEqualTo("0") // 0^0 => 0
		mathTreo.invoke("0101").assertEqualTo("1") // 0^1 => 1
		mathTreo.invoke("1100").assertEqualTo("1") // 1^0 => 1
		mathTreo.invoke("1101").assertEqualTo("0") // 1^1 => 0

		mathTreo.invoke("0110").assertEqualTo("0") // 0|0 => 0
		mathTreo.invoke("0111").assertEqualTo("1") // 0|1 => 1
		mathTreo.invoke("1110").assertEqualTo("1") // 1|0 => 1
		mathTreo.invoke("1111").assertEqualTo("1") // 1|1 => 1

		mathTreo.invoke("00000").assertEqualTo("0") // !!0 => 0
		mathTreo.invoke("0000000").assertEqualTo("1") // !!!0 => 1
		mathTreo.invoke("000010").assertEqualTo("0") // !0&0 => 0
	}
}