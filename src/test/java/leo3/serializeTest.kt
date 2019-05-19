package leo3

import leo.base.assertEqualTo
import kotlin.test.Test

class BitReaderTest {
	@Test
	fun emptyBitReader() {
		value().assertSerializes
		value("one").assertSerializes
		value(line("one"), line("plus", value("two"))).assertSerializes
	}
}

val Value.assertSerializes
	get() = valueOrNull(bitSeq).assertEqualTo(this)