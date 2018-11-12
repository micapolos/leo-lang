package leo

import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class ReaderTest {
	// 1 -> 10
	// 2 -> 20, 30
	// 3 -> error
	// 4 -> swallowed
	val readerFunction =
		Function(
			stack(
				pattern(
					term(
						leoReaderField,
						readWord fieldTo term(1.toByte().reflect))) returns
					template(
						term(
							leoReaderField,
							readWord fieldTo term(10.toByte().reflect))),
				pattern(
					term(
						leoReaderField,
						readWord fieldTo term(2.toByte().reflect))) returns
					template(
						term(
							leoReaderField,
							readWord fieldTo term(20.toByte().reflect),
							readWord fieldTo term(30.toByte().reflect))),
				pattern(
					term(
						leoReaderField,
						readWord fieldTo term(3.toByte().reflect))) returns
					template(term(errorWord)),
				pattern(
					term(
						leoReaderField,
						readWord fieldTo term(4.toByte().reflect))) returns
					template(term(leoReaderField))))

	@Test
	fun read_1_becomes_10() {
		"x"
			.read(emptyReader, 1.toByte(), readerFunction::invoke, String::plus)
			.assertEqualTo("x10" to emptyReader)
	}

	@Test
	fun read_2_becomes_20_30() {
		"x"
			.read(emptyReader, 2.toByte(), readerFunction::invoke, String::plus)
			.assertEqualTo("x2030" to emptyReader)
	}

	@Test
	fun read_3_becomes_error() {
		"x"
			.read(emptyReader, 3.toByte(), readerFunction::invoke, String::plus)
			.assertEqualTo("x" to emptyReader.copy(script = script(term(errorWord))))
	}

	@Test
	fun read_4_is_swallowed() {
		"x"
			.read(emptyReader, 4.toByte(), readerFunction::invoke, String::plus)
			.assertEqualTo("x" to emptyReader)
	}
}