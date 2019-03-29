package leo32.vm

import leo.base.Empty
import leo.base.empty
import leo.base.fail
import leo.base.fold
import leo32.base.*

data class Vm(
	val stack: Stack<Int>,
	val pc: Int)

@Suppress("unused")
val Empty.vm
	get() =
		Vm(0.stack.push(0), 0)

fun vm(vararg ops: Int): Vm =
	empty.vm
		.fold(ops.toTypedArray().reversed()) { push(it) }
		.run { copy(pc = sp) }

tailrec fun Vm.invoke(): Vm =
	if (pc == 0) this
	else copy(pc = pc.dec()).invoke(at(pc)).invoke()

val Vm.sp get() = stack.topIndex

fun Vm.invoke(op: Int): Vm =
	when (op) {
		noOp -> this
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
		loadOp -> fetch(Vm::load)
		storeOp -> fetch(Vm::store)
		popOp -> pop { this }
		jumpOp -> fetch(Vm::jump)
		branchOp -> pop(Vm::branch)
		callOp -> fetch2(Vm::call)
		jumpZOp -> popFetch(Vm::jumpIfZero)
		shlOp -> op2(Int::shl)
		shrOp -> op2(Int::shr)
		ushrOp -> op2(Int::ushr)
		retOp -> fetch(Vm::ret)
		else -> fail()
	}

inline fun Vm.fetch(fn: Vm.(Int) -> Vm): Vm =
	copy(pc = pc.dec()).fn(at(pc))

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

fun Vm.put(index: Int, int: Int): Vm =
	copy(stack = stack.put(index, int))

fun Vm.putTop(int: Int): Vm =
	put(sp, int)

inline fun Vm.op1(fn: Int.() -> Int): Vm =
	copy(stack = stack.put(stack.topIndex, stack.top.fn()))

inline fun Vm.op2(fn: Int.(Int) -> Int): Vm =
	pop { rhs -> op1 { fn(rhs) } }

fun Vm.load(offset: Int) =
	push(at(sp - offset))

fun Vm.store(offset: Int) =
	pop { put(sp - offset, it) }

fun Vm.jump(pcOffset: Int): Vm =
	copy(pc = pc - pcOffset)

fun Vm.branch(index: Int): Vm =
	jump(at(pc - index))

fun Vm.call(jumpOffset: Int, retOffset: Int): Vm =
	jump(jumpOffset).put(pc - retOffset, pc)

fun Vm.jumpIfZero(value: Int, pcOffset: Int): Vm =
	if (value == 0) jump(pcOffset) else this

fun Vm.pc(pc: Int): Vm =
	copy(pc = pc)

fun Vm.ret(retPc: Int): Vm =
	put(pc + 1, 0).pc(retPc)
