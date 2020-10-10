package vm3.asm

import vm3.float

val List<Op>.code
	get() =
		mapIndexed { index, op -> "$index: ${op.code}" }.joinToString("\n")

val Op.code: String
	get() =
		when (this) {
			Op.Exit -> "exit"
			Op.Nop -> "nop"
			Op.SysCall -> "syscall"
			is Op.Jump -> "jump $index"
			is Op.Switch -> "jump $index from [${jumpIndices.joinToString(", ")}]"
			is Op.Call -> "call $jumpIndex ret $retIndex"
			is Op.CallTable -> "call $selector from [...]"
			is Op.Ret -> "ret $index"
			is Op.Set32 -> assignCode(dst, code32(src))
			is Op.Copy32 -> assignCode(dst, memCode(src))
			is Op.CopyIndirect32 -> assignCode(dst, memCode(memCode(src)))
			is Op.CopyBlock -> assignCode(blockCode(dst, size), blockCode(src, size))
			is Op.I32Inc -> opCode(dst, "i32.inc", src)
			is Op.I32Dec -> opCode(dst, "i32.dec", src)
			is Op.I32Add -> opCode(dst, lhs, "i32.plus", rhs)
			is Op.I32Sub -> opCode(dst, lhs, "i32.minus", rhs)
			is Op.I32Mul -> opCode(dst, lhs, "i32.times", rhs)
			is Op.F32Add -> opCode(dst, lhs, "f32.plus", rhs)
			is Op.F32Sub -> opCode(dst, lhs, "f32.minus", rhs)
			is Op.F32Mul -> opCode(dst, lhs, "f32.times", rhs)
		}

fun code32(int: Int) = "$int (float: ${int.float})"
fun memCode(addr: Int) = "[$addr]"
fun memCode(code: String) = "[$code]"
fun blockCode(index: Int, size: Int) = "[$index:$size]"
fun prefixCode(op: String, rhs: String) = "$op $rhs"
fun infixCode(lhs: String, op: String, rhs: String) = "$lhs $op $rhs"
fun assignCode(lhs: String, rhs: String) = infixCode(lhs, "<-", rhs)
fun assignCode(lhs: Int, rhs: String) = assignCode(memCode(lhs), rhs)
fun opCode(dst: Int, op: String, src: Int) = assignCode(dst, prefixCode(op, memCode(src)))
fun opCode(dst: Int, lhs: Int, op: String, rhs: Int) = assignCode(dst, infixCode(memCode(lhs), op, memCode(rhs)))
