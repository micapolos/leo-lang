package leo32.treo

import leo.base.assertEqualTo
import kotlin.test.Test

class ExecutorTest {
	@Test
	fun negation() {
		val resultVar = newVar()
		val inputVar = newVar()
		val negateMachine = treo(
			capture(resultVar),
			treo(
				capture(inputVar),
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

		val executor = executor(negateMachine)
		executor.plusBit("0") // initialization
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