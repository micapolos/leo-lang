package vm3

import leo.base.assertEqualTo
import kotlin.test.Test

class DisassemblerTest {
	@Test
	fun test() {
		byteArrayOf(
			0x00,
			0x01,
			0x02,
			0x03, 0x10, 0, 0, 0,
			0x04, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x05, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x08, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x09, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x0A, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x0B, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0, 0x40, 0, 0, 0,
			0x10, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x11, 0x10, 0, 0, 0, 0x20, 0, 0, 0,
			0x16, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x17, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x33, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x34, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x35, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0,
			0x36, 0x10, 0, 0, 0, 0x20, 0, 0, 0, 0x30, 0, 0, 0)
			.disassemble
			.assertEqualTo("""
				0x00000000: exit
				0x00000001: nop
				0x00000002: syscall
				0x00000003: jump 0x00000010
				0x00000008: if [0x00000010] jump 0x00000020
				0x00000011: call 0x00000010 ret 0x00000020
				0x0000001a: [0x00000010] <- 0x00000020
				0x00000023: [0x00000010] <- [0x00000020]
				0x0000002c: [0x00000010] <- [0x00000020 + 48]
				0x00000039: [0x00000010] <- [0x00000020 + [0x00000030] * 64]
				0x0000004a: [0x00000010] <- [0x00000020] i32.inc
				0x00000053: [0x00000010] <- [0x00000020] i32.dec
				0x0000005c: [0x00000010] <- [0x00000020] i32.plus [0x00000030]
				0x00000069: [0x00000010] <- [0x00000020] i32.minus [0x00000030]
				0x00000076: [0x00000010] <- [0x00000020] f32.plus [0x00000030]
				0x00000083: [0x00000010] <- [0x00000020] f32.minus [0x00000030]
				0x00000090: [0x00000010] <- [0x00000020] f32.times [0x00000030]
				0x0000009d: [0x00000010] <- [0x00000020] f32.div [0x00000030]
	
					""".trimIndent())
	}
}