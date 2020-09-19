package vm3

import leo.base.assertEqualTo
import vm3.dsl.type.bool
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.i32
import vm3.dsl.type.struct
import vm3.dsl.value.argument
import vm3.dsl.value.array
import vm3.dsl.value.dec
import vm3.dsl.value.get
import vm3.dsl.value.gives
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import vm3.dsl.value.struct
import vm3.dsl.value.value
import kotlin.test.Test

class CompilerTest {
	@Test
	fun test() {
		val compiled = i32.gives(argument.inc + argument.dec).compiled

		compiled
			.disassemble
			.assertEqualTo(
				"""
					dataSize: 0x00000010
					outputOffset: 0x00000004
					--- disassembly ---
					0x00000000: [0x00000008] <- [0x00000000] i32.inc
					0x00000009: [0x0000000c] <- [0x00000000] i32.dec
					0x00000012: [0x00000004] <- [0x00000008] i32.plus [0x0000000c]
					0x0000001f: exit
					
					""".trimIndent()
			)
	}

	@Test
	fun array() {
		f32[3]
			.gives(argument)
			.compiled
			.disassemble
			.assertEqualTo("""
				dataSize: 0x0000000c
				outputOffset: 0x00000000
				--- disassembly ---
				0x00000000: exit
				
				""".trimIndent())
	}

	@Test
	fun arrayGet() {
		f32[3]
			.gives(argument[1.value])
			.compiled
			.disassemble
			.assertEqualTo("""
				dataSize: 0x00000020
				outputOffset: [0x0000001c]
				--- disassembly ---
				0x00000000: [0x0000000c] <- 0x00000000
				0x00000009: [0x00000010] <- 0x00000001
				0x00000012: [0x00000014] <- 0x00000004
				0x0000001b: [0x00000018] <- [0x00000010] i32.times [0x00000014]
				0x00000028: [0x0000001c] <- [0x0000000c] i32.plus [0x00000018]
				0x00000035: exit
				
				""".trimIndent())
	}

	@Test
	fun struct() {
		struct("x" to i32, "y" to f32)
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
		struct("x" to i32, "y" to f32)
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
			type(10.value).assertEqualTo(i32)
			type(10f.value).assertEqualTo(f32)

			type(10.value.inc).assertEqualTo(i32)
			type(10.value.dec).assertEqualTo(i32)
			type(10.value.plus(20.value)).assertEqualTo(i32)
			type(10f.value.plus(20f.value)).assertEqualTo(f32)

			array(10f.value, 20f.value).let { array ->
				type(array).assertEqualTo(f32[2])
				type(array[0.value]).assertEqualTo(f32)
				type(array[1.value]).assertEqualTo(f32)
			}

			struct("x" to 10.value, "y" to 20f.value).let { struct ->
				type(struct).assertEqualTo(struct("x" to i32, "y" to f32))
				type(struct["x"]).assertEqualTo(i32)
				type(struct["y"]).assertEqualTo(f32)
			}
		}
	}

	@Test
	fun layout() {
		Compiler().run {
			layout(bool).size.assertEqualTo(4)
			layout(i32).size.assertEqualTo(4)
			layout(f32).size.assertEqualTo(4)

			layout(i32[3]).size.assertEqualTo(12)
			layout(struct("x" to i32, "y" to f32)).size.assertEqualTo(8)
		}
	}
}