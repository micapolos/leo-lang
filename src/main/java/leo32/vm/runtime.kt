package leo32.vm

import leo.base.fail
import leo.base.fold

data class Runtime(
	val mem: Mem,
	var sp: Int,
	var pc: Int)

val Mem.runtime
	get() =
		Runtime(this, 0, 0)

fun Runtime.define(vararg ops: Int) {
	Unit.fold(ops.toTypedArray().reversed()) { push(it) }
	pc = sp
}

fun Runtime.run() {
	while (pc != 0) invoke(fetch())
}

fun Runtime.invoke(op: Int) {
	when (op) {
		noOp -> return
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
		constOp -> push(fetch())
		loadOp -> load(fetch())
		storeOp -> store(fetch())
		popOp -> pop
		jumpOp -> jump(fetch())
		branchOp -> branch(pop)
		callOp -> call(fetch(), fetch())
		jumpZOp -> jumpIfZero(pop, fetch())
		shlOp -> op2(Int::shl)
		shrOp -> op2(Int::shr)
		ushrOp -> op2(Int::ushr)
		retOp -> ret(fetch())
		else -> fail()
	}
}

fun Runtime.fetch(): Int {
	val int = mem.int(pc)
	pc -= 4
	return int
}

inline val Runtime.pop: Int
	get() {
		val int = mem.int(sp)
		sp -= 4
		return int
	}

fun Runtime.push(int: Int) {
	mem.set(sp, int)
	sp += 4
}

fun Runtime.at(index: Int) =
	mem.int(index)

fun Runtime.put(index: Int, int: Int) {
	mem.set(index, int)
}

fun Runtime.putTop(int: Int) {
	put(sp, int)
}

val Runtime.top
	get() =
		mem.int(sp)

fun Runtime.setTop(int: Int) {
	mem.set(sp, int)
}

inline fun Runtime.op1(fn: Int.() -> Int) {
	push(pop.fn())
}

inline fun Runtime.op2(fn: Int.(Int) -> Int) {
	push(pop.fn(pop))
}

fun Runtime.load(offset: Int) {
	push(at(sp - offset))
}

fun Runtime.store(offset: Int) {
	put(sp - offset, pop)
}

fun Runtime.jump(pcOffset: Int) {
	pc -= pcOffset
}

fun Runtime.branch(index: Int) {
	jump(at(pc - index))
}

fun Runtime.call(jumpOffset: Int, retOffset: Int) {
	mem.set(pc - retOffset, pc)
	jump(jumpOffset)
}

fun Runtime.jumpIfZero(value: Int, pcOffset: Int) {
	if (value == 0) jump(pcOffset)
}

fun Runtime.ret(retPc: Int) {
	mem.set(pc + 1, 0)
	pc = retPc
}
