package vm3.asm

import leo.base.assertEqualTo
import vm3.bytes
import vm3.x00_exitOpcode
import vm3.x01_nopOpcode
import vm3.x02_syscallOpcode
import vm3.x03_jumpOpcode
import vm3.x06_callOpcode
import vm3.x07_retOpcode
import vm3.x08_setConst32Opcode
import vm3.x09_set32Opcode
import vm3.x0B_setSizeOpcode
import vm3.x16_i32PlusOpcode
import vm3.x17_i32MinusOpcode
import vm3.x18_i32TimesOpcode
import vm3.x33_f32PlusOpcode
import vm3.x34_f32MinusOpcode
import vm3.x35_f32TimesOpcode
import kotlin.test.Test

class CompilerTest {
	@Test
	fun compile() {
		listOf<Op>().compiledBytes.assertEqualTo(bytes())

		listOf(Op.Exit).compiledBytes.assertEqualTo(bytes(x00_exitOpcode))
		listOf(Op.Nop).compiledBytes.assertEqualTo(bytes(x01_nopOpcode))
		listOf(Op.SysCall).compiledBytes.assertEqualTo(bytes(x02_syscallOpcode))

		listOf(Op.Jump(1), Op.Exit)
			.compiledBytes
			.assertEqualTo(bytes(x03_jumpOpcode, 5, 0, 0, 0, x00_exitOpcode))

		listOf(Op.Call(1, 0x20), Op.Ret(0x20))
			.compiledBytes
			.assertEqualTo(bytes(x06_callOpcode, 9, 0, 0, 0, 0x20, 0, 0, 0, x07_retOpcode, 0x20, 0, 0, 0))

		listOf(Op.Set32(0x10, 0x20))
			.compiledBytes
			.assertEqualTo(bytes(x08_setConst32Opcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0))

		listOf(Op.Copy32(0x10, 0x20))
			.compiledBytes
			.assertEqualTo(bytes(x09_set32Opcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0))

		listOf(Op.CopyBlock(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x0B_setSizeOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))

		listOf(Op.I32Add(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x16_i32PlusOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))
		listOf(Op.I32Sub(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x17_i32MinusOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))
		listOf(Op.I32Mul(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x18_i32TimesOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))

		listOf(Op.F32Add(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x33_f32PlusOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))
		listOf(Op.F32Sub(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x34_f32MinusOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))
		listOf(Op.F32Mul(0x10, 0x20, 0x30))
			.compiledBytes
			.assertEqualTo(bytes(x35_f32TimesOpcode, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0))
	}
}