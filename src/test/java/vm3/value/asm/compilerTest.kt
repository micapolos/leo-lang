package vm3.value.asm

import leo.base.assertEqualTo
import vm3.dsl.type.f32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.get
import vm3.dsl.value.gives
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class CompilerTest {
	@Test
	fun test() {
		val compiled = f32.gives(argument + argument).compiled

		compiled
			.disassemble
			.assertEqualTo(
				"""
					dataSize: 0x00000008
					outputOffset: 0x00000004
					--- disassembly ---
					0x00000000: [0x00000004] <- [0x00000000] f32.plus [0x00000000]
					0x0000000d: exit
					
					""".trimIndent()
			)
	}

	@Test
	fun struct() {
		struct("x" to f32, "y" to f32)
			.gives(argument)
			.compiled
			.disassemble
			.assertEqualTo("""
				dataSize: 0x00000008
				outputOffset: 0x00000000
				--- disassembly ---
				0x00000000: exit
				
				""".trimIndent())
	}

	@Test
	fun structGet() {
		struct("x" to f32, "y" to f32)
			.gives(argument["y"])
			.compiled
			.disassemble
			.assertEqualTo("""
				dataSize: 0x00000014
				outputOffset: [0x00000010]
				--- disassembly ---
				0x00000000: [0x00000008] <- 0x00000000
				0x00000009: [0x0000000c] <- 0x00000004
				0x00000012: [0x00000010] <- [0x00000008] i32.plus [0x0000000c]
				0x0000001f: exit
				
				""".trimIndent())
	}

	@Test
	fun type() {
		Compiler().run {
			type(10f.value).assertEqualTo(f32)

			type(10f.value.plus(20f.value)).assertEqualTo(f32)

			struct("x" to 10f.value, "y" to 20f.value).let { struct ->
				type(struct).assertEqualTo(struct("x" to f32, "y" to f32))
				type(struct["x"]).assertEqualTo(f32)
				type(struct["y"]).assertEqualTo(f32)
			}
		}
	}

	@Test
	fun layout() {
		Compiler().run {
			layout(f32).size.assertEqualTo(4)
			layout(struct("x" to f32, "y" to f32)).size.assertEqualTo(8)
		}
	}
}