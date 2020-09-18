package vm3

import leo.base.assertEqualTo
import vm3.dsl.type.i32
import vm3.dsl.value.dec
import vm3.dsl.value.fn
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import kotlin.test.Test

class CompilerTest {
	@Test
	fun test() {
		val compiled = i32.fn { inc + dec }.compiled

		compiled
			.disassemble
			.assertEqualTo("""dataSize: 0x00000010
--- disassembly ---
0x00000000: [0x00000004] <- [0x00000000] i32.inc
0x00000009: [0x00000008] <- [0x00000000] i32.dec
0x00000012: [0x0000000c] <- [0x00000004] i32.plus [0x00000008]
0x0000001f: exit
""")
	}
}