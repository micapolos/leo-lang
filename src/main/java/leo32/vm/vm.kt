package leo32.vm

import leo.base.clampedByte
import leo.base.uint

var pc = 0
var sp = 0

fun exec(startPc: Ptr) {
	pc = startPc
	run()
}

fun run() {
	while (true) {
		if (pc == 0) return
		when (val op = fetch8()) {
			noOp -> Unit

			i32invOp -> i32op1(Int::inv)
			i32andOp -> i32op2(Int::and)
			i32orOp -> i32op2(Int::or)
			i32xorOp -> i32op2(Int::xor)

			i32negOp -> i32op1(Int::unaryMinus)
			i32incOp -> i32op1(Int::inc)
			i32decOp -> i32op1(Int::dec)
			i32plusOp -> i32op2(Int::plus)
			i32minusOp -> i32op2(Int::minus)
			i32timesOp -> i32op2(Int::times)
			i32divOp -> i32op2(Int::div)
			i32remOp -> i32op2(Int::rem)

			i32constOp -> fetch32()
			i32loadOp -> i32push(i32get(fetch32()))
			i32storeOp -> i32set(fetch32(), i32pop())
			i32popOp -> i32pop()

			jumpOp -> jump()
			branchOp -> branch()
			callOp -> call()
			jumpZOp -> jumpZ()
			retOp -> ret()

			i32shlOp -> i32op2(Int::shl)
			i32shrOp -> i32op2(Int::shr)
			i32ushrOp -> i32op2(Int::ushr)

			f32negOp -> f32op1(Float::unaryMinus)
			f32plusOp -> f32op2(Float::plus)
			f32minusOp -> f32op2(Float::minus)
			f32timesOp -> f32op2(Float::times)
			f32divOp -> f32op2(Float::div)
			f32remOp -> f32op2(Float::rem)

			else -> error("unknown op: $op")
		}
	}
}

fun fetch8() = memByteArray[pc--].uint

fun fetch32(): Int {
	val int = memByteBuffer.getInt(pc)
	pc -= 4
	return int
}

inline fun i32op1(fn: Int.() -> Int) {
	val arg = i32top()
	i32setTop(arg.fn())
}

inline fun i32op2(fn: Int.(Int) -> Int) {
	val rhs = i32pop()
	val lhs = i32top()
	i32setTop(rhs.fn(lhs))
}

inline fun f32op1(fn: Float.() -> Float) {
	val arg = f32top()
	f32setTop(arg.fn())
}

inline fun f32op2(fn: Float.(Float) -> Float) {
	val rhs = f32pop()
	val lhs = f32top()
	f32setTop(rhs.fn(lhs))
}

fun i32get(ptr: Ptr): Int {
	return memByteBuffer.getInt(ptr)
}

fun i32set(ptr: Ptr, i32: Int) {
	memByteBuffer.putInt(ptr, i32)
}

fun i32top(): Int {
	return i32get(sp - 4)
}

fun i32pop(): Int {
	sp -= 4
	return i32get(sp)
}

fun i32setTop(i32: Int) {
	i32set(sp - 4, i32)
}

fun i32push(i32: Int) {
	i32set(sp, i32)
	sp += 4
}

fun f32top(): Float {
	return memByteBuffer.getFloat(sp - 4)
}

fun f32pop(): Float {
	sp -= 4
	return f32get(sp)
}

fun f32setTop(f32: Float) {
	f32set(sp - 4, f32)
}

fun f32push(f32: Float) {
	f32set(sp, f32)
	sp += 4
}

fun f32get(ptr: Ptr): Float {
	return memByteBuffer.getFloat(ptr)
}

fun f32set(ptr: Ptr, f32: Float) {
	memByteBuffer.putFloat(ptr, f32)
}

fun byte(ptr: Ptr): Byte {
	return memByteArray[ptr]
}

fun set(ptr: Ptr, i8: Byte) {
	memByteArray[ptr] = i8
}

fun push(byte: Byte) {
	set(sp, byte)
	sp += 1
}

fun pushOp(int: Int) {
	push(int.clampedByte)
}

fun jump() {
	pc = fetch32()
}

fun jumpZ() {
	val newPc = fetch32()
	val int = i32pop()
	if (int == 0) pc = newPc
}

fun branch() {
	val index = i32pop()
	pc = i32get(pc + (index shl 2))
}

fun call() {
	val newPc = fetch32()
	val retPc = fetch32()
	i32set(retPc, pc)
	pc = newPc
}

fun ret() {
	pc = fetch32()
}