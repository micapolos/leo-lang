package vm3

import leo.base.appendSquareParenthesized
import leo.base.appendableString
import java.io.InputStream

typealias Appender = Appendable.() -> Appendable

fun appender(int: Int): Appender = { append(int) }
fun memAppender(index: Int): Appender = { appendMem(index) }

val Int.hexString get() = appendableString { it.append(this) }

fun Appendable.append(int: Int) =
	append("0x").append(int.toString(16).padStart(8, '0'))

fun Appendable.appendPlain(int: Int) =
	append("$int")

fun Appendable.append(byte: Byte) =
	append("0x").append(byte.toString(16).padStart(2, '0'))

fun Appendable.append(op: Op) =
	when (op) {
		Op.Exit -> append("exit")
		Op.Nop -> append("nop")
		Op.SysCall -> append("syscall")

		is Op.Jump -> append("jump ").append(op.addr)
		is Op.JumpIf -> append("if ").appendMem(op.cond).append(" jump ").append(op.addr)
		is Op.Call -> append("call ").append(op.addr).append(" ret ").append(op.retAddr)

		is Op.SetConst -> appendAssign({ appendMem(op.dst) }, { append(op.value) })
		is Op.Set -> appendAssign({ appendMem(op.dst) }, { appendMem(op.lhs) })
		is Op.SetIndirect -> appendAssign({ appendMem(op.dst) }, { appendMemMem(op.lhs) })

		is Op.I32Inc -> appendAssignOp(op.dst, op.lhs, "i32.inc")
		is Op.I32Dec -> appendAssignOp(op.dst, op.lhs, "i32.dec")

		is Op.I32Plus -> appendAssignOp(op.dst, op.lhs, "i32.plus", op.rhs)
		is Op.I32Minus -> appendAssignOp(op.dst, op.lhs, "i32.minus", op.rhs)
		is Op.I32Times -> appendAssignOp(op.dst, op.lhs, "i32.times", op.rhs)

		is Op.F32Plus -> appendAssignOp(op.dst, op.lhs, "f32.plus", op.rhs)
		is Op.F32Minus -> appendAssignOp(op.dst, op.lhs, "f32.minus", op.rhs)
		is Op.F32Times -> appendAssignOp(op.dst, op.lhs, "f32.times", op.rhs)
		is Op.F32Div -> appendAssignOp(op.dst, op.lhs, "f32.div", op.rhs)

		is Op.Unknown -> append("unknown ").append(op.byte)
	}

fun Appendable.appendOps(ops: List<Op>): Appendable =
	ops.fold(this) { appendable, op -> appendable.append(op) }

fun Appendable.appendMem(index: Int): Appendable =
	appendSquareParenthesized { append(index) }

fun Appendable.appendMemMem(index: Int): Appendable =
	appendSquareParenthesized { appendMem(index) }

fun Appendable.appendMemOffset(index: Int, offset: Int): Appendable =
	appendSquareParenthesized { append(index).append(" + ").appendPlain(offset) }

fun Appendable.appendMemIndex(index: Int, offset: Int, size: Int): Appendable =
	appendMem(index).append(" + ").appendMem(offset).append(" * ").appendPlain(size)

fun Appendable.appendInfix(appendLhs: Appender, op: String, appendRhs: Appender): Appendable =
	appendLhs().append(" ").append(op).append(" ").appendRhs()

fun Appendable.appendPostfix(appendLhs: Appender, op: String): Appendable =
	appendLhs().append(" ").append(op)

fun Appendable.appendAssign(appendLhs: Appender, appendRhs: Appender): Appendable =
	appendInfix(appendLhs, "<-", appendRhs)

fun Appendable.appendAssignOp(dst: Int, lhs: Int, op: String): Appendable =
	appendAssign(
		{ appendMem(dst) },
		{
			appendPostfix(
				{ appendMem(lhs) },
				op)
		})

fun Appendable.appendAssignOp(dst: Int, lhs: Int, op: String, rhs: Int): Appendable =
	appendAssign(memAppender(dst),
		{
			appendInfix(
				{ appendMem(lhs) },
				op,
				{ appendMem(rhs) })
		})

fun Appendable.disassemble(address: Int, inputStream: InputStream): Appendable {
	var address = address
	while (true) {
		val op = inputStream.readOp()
		if (op == null) return this
		else {
			append(address).append(": ").append(op).append("\n")
			address += op.size
		}
	}
}

val ByteArray.disassemble: String
	get() =
		appendableString { appendable ->
			appendable.disassemble(0, inputStream())
		}
