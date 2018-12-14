package leo.vm

import leo.base.Bit
import leo.base.Variable
import leo.base.oneBit
import leo.base.zeroBit

sealed class Op {
	abstract fun apply()
}

object NoOp : Op() {
	override fun apply() {}
}

data class ConstantOp(
	val bitVariable: Variable<Bit>,
	val bit: Bit) : Op() {
	override fun apply() {
		bitVariable.value = bit
	}
}

data class LoadOp(
	val lhs: Variable<Bit>,
	val rhs: Variable<Bit>) : Op() {
	override fun apply() {
		lhs.value = rhs.value
	}
}

data class BranchOp(
	val selector: Variable<Bit>,
	val zeroOp: Op,
	val oneOp: Op) : Op() {
	override fun apply() {
		when (selector.value) {
			Bit.ZERO -> zeroOp.apply()
			Bit.ONE -> oneOp.apply()
		}
	}
}

data class SequenceOp(
	val firstOp: Op,
	val secondOp: Op) : Op() {
	override fun apply() {
		firstOp.apply()
		secondOp.apply()
	}
}


val noOp = NoOp

fun Variable<Bit>.setOp(bit: Bit): Op =
	ConstantOp(this, bit)

fun Variable<Bit>.setOp(bitVariable: Variable<Bit>): Op =
	LoadOp(this, bitVariable)

fun Variable<Bit>.branchOp(zeroOp: Op, oneOp: Op): Op =
	BranchOp(this, zeroOp, oneOp)

fun Op.then(op: Op): Op =
	SequenceOp(this, op)

// === custom ops

val Variable<Bit>.notOp: Op
	get() =
		branchOp(setOp(oneBit), setOp(zeroBit))

fun Variable<Bit>.andOp(bitVariable: Variable<Bit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(zeroBit)),
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)))

fun Variable<Bit>.orOp(bitVariable: Variable<Bit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)),
		bitVariable.branchOp(setOp(oneBit), setOp(oneBit)))

fun Variable<Bit>.xorOp(bitVariable: Variable<Bit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)),
		bitVariable.branchOp(setOp(oneBit), setOp(zeroBit)))
