package leo32.treo

import leo.base.assertEqualTo
import leo.binary.bit0
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun negation() {
		val resultVar = variable()
		val inputVar = variable()
		val negatorTreo = treo(
			resultVar,
			treo(
				inputVar,
				treo(
					call(
						fn(
							treo(
								at0(treo(
									at1(treo(leaf)))),
								at1(treo(
									at0(treo(leaf)))))),
						param(treo(inputVar, treo(leaf)))),
					treo(back.back.back))))
			.enter(bit0)!! // initialization

		val executor = executor(negatorTreo)
		executor.bitString.assertEqualTo("0")

		executor.plusBit("1")
		executor.bitString.assertEqualTo("0")

		executor.plusBit("0")
		executor.bitString.assertEqualTo("1")

		executor.plusBit("1")
		executor.bitString.assertEqualTo("0")

		executor.plusBit("1")
		executor.bitString.assertEqualTo("0")
	}
}