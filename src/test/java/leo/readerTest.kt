package leo

import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class ReaderTest {
	// 1 -> 10
	// 2 -> 20, 30
	// 3 -> error
	// 4 -> swallowed
	val testReader =
		Reader(
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
						template(term(leoReaderField)))),
			leoReaderScript)

	@Test
	fun read_1_becomes_10() {
		testReader
			.read("x", 1.toByte()) { string, nextByte ->
				string.plus(nextByte)
			}
			.assertEqualTo("x10" to testReader)
	}

	@Test
	fun read_2_becomes_20_30() {
		testReader
			.read("x", 2.toByte()) { string, nextByte ->
				string.plus(nextByte)
			}
			.assertEqualTo("x2030" to testReader)
	}

	@Test
	fun read_3_becomes_error() {
		testReader
			.read("x", 3.toByte()) { string, byte ->
				string.plus(byte)
			}
			.assertEqualTo("x" to testReader.copy(script = script(term(errorWord))))
	}

	@Test
	fun read_4_is_swallowed() {
		testReader
			.read("x", 4.toByte()) { string, nextByte ->
				string.plus(nextByte)
			}
			.assertEqualTo("x" to testReader)
	}
}