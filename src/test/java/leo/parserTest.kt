package leo

import leo.base.EnumBit
import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class ParserTest {
	@Test
	fun bitZero() {
		bitWord.fieldTo(zeroWord.term)
			.parseBit
			.assertEqualTo(EnumBit.ZERO)
	}

	@Test
	fun bitOne() {
		bitWord.fieldTo(oneWord.term)
			.parseBit
			.assertEqualTo(EnumBit.ONE)
	}

	@Test
	fun byte() {
		byteWord.fieldTo(
			term(
				EnumBit.ZERO.reflect,
				EnumBit.ZERO.reflect,
				EnumBit.ZERO.reflect,
				EnumBit.ZERO.reflect,
				EnumBit.ONE.reflect,
				EnumBit.ONE.reflect,
				EnumBit.ZERO.reflect,
				EnumBit.ONE.reflect))
			.parseByte
			.assertEqualTo(13.toByte())
	}

	@Test
	fun parseStack() {
		term(
			bitWord fieldTo zeroWord.term,
			bitWord fieldTo oneWord.term)
			.parseStack(Field<Nothing>::parseBit)
			.assertEqualTo(stack(EnumBit.ZERO, EnumBit.ONE))
	}
}

fun <V> V.assertReflectAndParseTermWorks(reflectFn: V.() -> Term<Nothing>, parseFn: Term<Nothing>.() -> V?) =
	reflectFn().parseFn().assertEqualTo(this)

fun <V> V.assertReflectAndParseWorks(reflectFn: V.() -> Field<Nothing>, parseFn: Field<Nothing>.() -> V?) =
	reflectFn().parseFn().assertEqualTo(this)