package vm3.asm

sealed class Op {
	object Exit : Op()
	object Nop : Op()
	object SysCall : Op()

	data class Jump(val index: Int) : Op()
	data class Switch(val index: Int, val jumpIndices: List<Int>) : Op()

	data class Call(val jumpIndex: Int, val retIndex: Int) : Op()
	data class CallTable(val selector: Int, val calls: List<Call>) : Op()
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

sealed class Prim {
	data class Set32(val dst: Int, val src: Int) : Prim()
	data class Copy32(val dst: Int, val src: Int) : Prim()
	data class CopyIndirect32(val dst: Int, val src: Int) : Prim()
	data class CopyBlock(val dst: Int, val src: Int, val size: Int) : Prim()

	data class I32Inc(val dst: Int, val src: Int) : Prim()
	data class I32Dec(val dst: Int, val src: Int) : Prim()

	data class I32Add(val dst: Int, val lhs: Int, val rhs: Int) : Prim()
	data class I32Sub(val dst: Int, val lhs: Int, val rhs: Int) : Prim()
	data class I32Mul(val dst: Int, val lhs: Int, val rhs: Int) : Prim()

	data class F32Add(val dst: Int, val lhs: Int, val rhs: Int) : Prim()
	data class F32Sub(val dst: Int, val lhs: Int, val rhs: Int) : Prim()
	data class F32Mul(val dst: Int, val lhs: Int, val rhs: Int) : Prim()
}

val Prim.op
	get() =
		when (this) {
			is Prim.Set32 -> Op.Set32(dst, src)
			is Prim.Copy32 -> Op.Copy32(dst, src)
			is Prim.CopyIndirect32 -> Op.CopyIndirect32(dst, src)
			is Prim.CopyBlock -> Op.CopyBlock(dst, src, size)
			is Prim.I32Inc -> Op.I32Inc(dst, src)
			is Prim.I32Dec -> Op.I32Dec(dst, src)
			is Prim.I32Add -> Op.I32Add(dst, lhs, rhs)
			is Prim.I32Sub -> Op.I32Sub(dst, lhs, rhs)
			is Prim.I32Mul -> Op.I32Mul(dst, lhs, rhs)
			is Prim.F32Add -> Op.F32Add(dst, lhs, rhs)
			is Prim.F32Sub -> Op.F32Sub(dst, lhs, rhs)
			is Prim.F32Mul -> Op.F32Mul(dst, lhs, rhs)
		}
