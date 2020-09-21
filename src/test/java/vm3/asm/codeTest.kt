package vm3.asm

import leo.base.assertEqualTo
import org.junit.Test

class CodeTest {
	@Test
	fun code() {
		listOf(
			Op.Exit,
			Op.Nop,
			Op.SysCall,

			Op.Jump(1),
			Op.Call(1, 2),
			Op.Ret(1),

			Op.Set32(1, 2),
			Op.Copy32(1, 2),
			Op.CopyIndirect32(1, 2),
			Op.CopyBlock(1, 2, 128),

			Op.I32Inc(1, 2),
			Op.I32Dec(1, 2),

			Op.I32Add(1, 2, 3),
			Op.I32Sub(1, 2, 3),
			Op.I32Mul(1, 2, 3),

			Op.F32Add(1, 2, 3),
			Op.F32Sub(1, 2, 3),
			Op.F32Mul(1, 2, 3))
			.code
			.assertEqualTo("""
				0: exit
				1: nop
				2: syscall
				3: jump 1
				4: call 1 ret 2
				5: ret 1
				6: [1] <- 2 (float: 2.8E-45)
				7: [1] <- [2]
				8: [1] <- [[2]]
				9: [1:128] <- [2:128]
				10: [1] <- i32.inc [2]
				11: [1] <- i32.dec [2]
				12: [1] <- [2] i32.plus [3]
				13: [1] <- [2] i32.minus [3]
				14: [1] <- [2] i32.times [3]
				15: [1] <- [2] f32.plus [3]
				16: [1] <- [2] f32.minus [3]
				17: [1] <- [2] f32.times [3]
				""".trimIndent())
	}
}
