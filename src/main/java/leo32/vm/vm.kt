package leo32.vm

import leo.base.fail
import leo.base.fold
import leo32.base.*

data class Vm(
	val stack: Stack<Int>,
	val pc: Int)

val vm = Vm(0.stack, 0)

fun vm(vararg ops: Int): Vm =
	vm.fold(ops.toTypedArray()) { push(it) }

tailrec fun Vm.invoke(): Vm {
	val op = at(pc)
	val unit = copy(pc = pc.inc())
	return if (op == exitOp) unit
	else unit.invoke(op).invoke()
}

val Vm.sp get() = stack.topIndex

fun Vm.invoke(op: Int): Vm =
	when (op) {
		nop -> this
		invOp -> op1(Int::inv)
		andOp -> op2(Int::and)
		orOp -> op2(Int::or)
		negOp -> op1(Int::unaryMinus)
		incOp -> op1(Int::inc)
		decOp -> op1(Int::dec)
		plusOp -> op2(Int::plus)
		minusOp -> op2(Int::minus)
		timesOp -> op2(Int::times)
		divOp -> op2(Int::div)
		remOp -> op2(Int::rem)
		constOp -> fetch(Vm::push)
		loadOp -> pop(Vm::load)
		storeOp -> pop2(Vm::store)
		popOp -> pop { this }
		jumpOp -> fetch(Vm::jump)
		branchOp -> pop(Vm::branch)
		callOp -> fetch2(Vm::call)
		jumpIfZeroOp -> popFetch(Vm::jumpIfZero)
		else -> fail()
	}

inline fun Vm.fetch(fn: Vm.(Int) -> Vm): Vm =
	copy(pc = pc.inc()).fn(stack.at(pc))

inline fun Vm.fetch2(fn: Vm.(Int, Int) -> Vm): Vm =
	fetch { int1 -> fetch { int2 -> fn(int1, int2) } }

inline fun Vm.popFetch(fn: Vm.(Int, Int) -> Vm): Vm =
	fetch { rhs -> pop { lhs -> fn(lhs, rhs) } }

inline fun Vm.pop(fn: Vm.(Int) -> Vm): Vm =
	stack.pop().let { stackAndValue ->
		copy(stack = stackAndValue.first).fn(stackAndValue.second)
	}

inline fun Vm.pop2(fn: Vm.(Int, Int) -> Vm): Vm =
	pop { rhs -> pop { lhs -> fn(lhs, rhs) } }

fun Vm.push(int: Int): Vm =
	copy(stack = stack.push(int))

fun Vm.at(index: Int): Int =
	stack.at(index)

fun Vm.put(int: Int): Vm =
	copy(stack = stack.put(stack.topIndex, int))

inline fun Vm.op1(fn: Int.() -> Int): Vm =
	copy(stack = stack.put(stack.topIndex, stack.top.fn()))

inline fun Vm.op2(fn: Int.(Int) -> Int): Vm =
	pop { rhs -> op1 { fn(rhs) } }

fun Vm.load(index: Int) =
	push(at(index))

fun Vm.store(index: Int, int: Int) =
	copy(stack = stack.put(index, int))

fun Vm.jump(jumpPc: Int): Vm =
	copy(pc = jumpPc)

fun Vm.branch(index: Int): Vm =
	jump(at(pc + index))

fun Vm.call(callPc: Int, retPc: Int): Vm =
	jump(callPc).store(retPc, pc)

fun Vm.jumpIfZero(value: Int, jumpPc: Int): Vm =
	if (value == 0) jump(jumpPc) else this