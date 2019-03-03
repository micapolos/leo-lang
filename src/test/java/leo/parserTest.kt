package leo

import leo.base.assertEqualTo
import leo.base.stack
import leo.binary.Bit
import kotlin.test.Test

class ParserTest {
	@Test
	fun bitZero() {
		bitWord.fieldTo(zeroWord.term)
			.parseBit
			.assertEqualTo(Bit.ZERO)
	}

	@Test
	fun bitOne() {
		bitWord.fieldTo(oneWord.term)
			.parseBit
			.assertEqualTo(Bit.ONE)
	}

	@Test
	fun byte() {
		byteWord.fieldTo(
			term(
				Bit.ZERO.reflect,
				Bit.ZERO.reflect,
				Bit.ZERO.reflect,
				Bit.ZERO.reflect,
				Bit.ONE.reflect,
				Bit.ONE.reflect,
				Bit.ZERO.reflect,
				Bit.ONE.reflect))
			.parseByte
			.assertEqualTo(13.toByte())
	}

	@Test
	fun parseStack() {
		term(
			bitWord fieldTo zeroWord.term,
			bitWord fieldTo oneWord.term)
			.parseStack(Field<Nothing>::parseBit)
			.assertEqualTo(stack(Bit.ZERO, Bit.ONE))
	}
}

fun <V> V.assertReflectAndParseTermWorks(reflectFn: V.() -> Term<Nothing>, parseFn: Term<Nothing>.() -> V?) =
	reflectFn().parseFn().assertEqualTo(this)

fun <V> V.assertReflectAndParseWorks(reflectFn: V.() -> Field<Nothing>, parseFn: Field<Nothing>.() -> V?) =
	reflectFn().parseFn().assertEqualTo(this)