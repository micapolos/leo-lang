package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class MemTest {
	@Test
	fun mem16Test() {
		var mem16 = zeroMem16
		for (index in 0 until 65536) mem16.at(index).assertEqualTo(0)
		for (index in 0 until 65536) mem16 = mem16.put(index, index)
		for (index in 0 until 65536) mem16.at(index).assertEqualTo(index)
	}
}