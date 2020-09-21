package vm3.asm

sealed class Op {
	object Exit : Op()
	object Nop : Op()
	object SysCall : Op()

	data class Jump(val index: Int) : Op()

	data class Call(val jumpIndex: Int, val retIndex: Int) : Op()
	data class Ret(val index: Int) : Op()

	data class Set32(val dst: Int, val src: Int) : Op()
	data class Copy32(val dst: Int, val src: Int) : Op()
	data class CopyIndirect32(val dst: Int, val src: Int) : Op()
	data class CopyBlock(val dst: Int, val src: Int, val size: Int) : Op()

	data class I32Inc(val dst: Int, val src: Int) : Op()
	data class I32Dec(val dst: Int, val src: Int) : Op()

	data class I32Add(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class I32Sub(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class I32Mul(val dst: Int, val lhs: Int, val rhs: Int) : Op()

	data class F32Add(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class F32Sub(val dst: Int, val lhs: Int, val rhs: Int) : Op()
	data class F32Mul(val dst: Int, val lhs: Int, val rhs: Int) : Op()
}
