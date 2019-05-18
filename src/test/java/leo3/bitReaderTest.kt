package leo3

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.orNullFold
import leo.binary.utf8BitSeq
import kotlin.test.Test

class BitReaderTest {
	@Test
	fun emptyBitReader() {
		empty.scope.completedBitReader
			.orNullFold("foo\u0000\u0000".utf8BitSeq, BitReader::read)!!
			.assertEqualTo(null)
	}
}