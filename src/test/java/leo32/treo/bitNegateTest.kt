package leo32.treo

import leo.base.assertEqualTo
import leo.binary.*
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
		mathTreo.invoke("000").assertEqualTo(bit0.inverse.digitString)
		mathTreo.invoke("100").assertEqualTo(bit1.inverse.digitString)

		mathTreo.invoke("0010").assertEqualTo(bit0.and(bit0).digitString)
		mathTreo.invoke("0011").assertEqualTo(bit0.and(bit1).digitString)
		mathTreo.invoke("1010").assertEqualTo(bit1.and(bit0).digitString)
		mathTreo.invoke("1011").assertEqualTo(bit1.and(bit1).digitString)

		mathTreo.invoke("0100").assertEqualTo(bit0.xor(bit0).digitString)
		mathTreo.invoke("0101").assertEqualTo(bit0.xor(bit1).digitString)
		mathTreo.invoke("1100").assertEqualTo(bit1.xor(bit0).digitString)
		mathTreo.invoke("1101").assertEqualTo(bit1.xor(bit1).digitString)

		mathTreo.invoke("0110").assertEqualTo(bit0.or(bit0).digitString)
		mathTreo.invoke("0111").assertEqualTo(bit0.or(bit1).digitString)
		mathTreo.invoke("1110").assertEqualTo(bit1.or(bit0).digitString)
		mathTreo.invoke("1111").assertEqualTo(bit1.or(bit1).digitString)

		mathTreo.invoke("00000").assertEqualTo(bit0.inverse.inverse.digitString)
		mathTreo.invoke("0000000").assertEqualTo(bit0.inverse.inverse.inverse.digitString)
	}
}