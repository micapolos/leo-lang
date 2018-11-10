package leo

import leo.*
import leo.base.Bit
import leo.base.assertEqualTo
import kotlin.test.Test

class BitTest {
	@Test
	fun bitZero() {
		Bit.ZERO
				.reflect
				.assertEqualTo(bitWord fieldTo term(zeroWord))
	}

	@Test
	fun bitOne() {
		Bit.ONE
				.reflect
				.assertEqualTo(bitWord fieldTo term(oneWord))
	}

	@Test
	fun byte() {
		13.toByte()
				.reflect
				.assertEqualTo(
						byteWord fieldTo term(
              Bit.ZERO.reflect,
              Bit.ZERO.reflect,
              Bit.ZERO.reflect,
              Bit.ZERO.reflect,
              Bit.ONE.reflect,
              Bit.ONE.reflect,
              Bit.ZERO.reflect,
              Bit.ONE.reflect
            )
        )
	}

	@Test
	fun byteArray_empty() {
		byteArrayOf()
				.reflect
				.assertEqualTo(byteWord fieldTo term(arrayWord))
	}


	@Test
	fun byteArray_nonEmpty() {
		byteArrayOf(0, 127)
				.reflect
				.assertEqualTo(
						byteWord fieldTo term(
              arrayWord fieldTo term(
                0.toByte().reflect,
                127.toByte().reflect
              )
            )
        )
	}
}