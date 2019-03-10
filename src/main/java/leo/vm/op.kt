package leo.vm

import leo.base.EnumBit
import leo.base.Variable
import leo.base.oneBit
import leo.base.zeroBit

sealed class Op(
	var previousOpOrNull: Op? = null) {
	abstract fun apply()
}

object NoOp : Op() {
	override fun apply() {}
}

data class ConstantOp(
	val dstBitVariable: Variable<EnumBit>,
	val srcBit: EnumBit) : Op() {
	override fun apply() {
		dstBitVariable.value = srcBit
	}
}

data class LoadOp(
	val dstBitVariable: Variable<EnumBit>,
	val srcBitVariable: Variable<EnumBit>) : Op() {
	override fun apply() {
		dstBitVariable.value = srcBitVariable.value
	}
}

data class BranchOp(
	val switchBitVariable: Variable<EnumBit>,
	val caseZeroOp: Op,
	val caseOneOp: Op) : Op() {
	override fun apply() {
		when (switchBitVariable.value) {
			EnumBit.ZERO -> caseZeroOp.apply()
			EnumBit.ONE -> caseOneOp.apply()
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

data class WhileOp(
	val conditionBitVariable: Variable<EnumBit>,
	val op: Op) : Op() {
	override fun apply() {
		while (conditionBitVariable.value == oneBit) {
			op.apply()
		}
	}
}

// === constructors

val noOp = NoOp

fun Variable<EnumBit>.setOp(bit: EnumBit): Op =
	ConstantOp(this, bit)

fun Variable<EnumBit>.setOp(bitVariable: Variable<EnumBit>): Op =
	LoadOp(this, bitVariable)

fun Variable<EnumBit>.branchOp(zeroOp: Op, oneOp: Op): Op =
	BranchOp(this, zeroOp, oneOp)

fun Op.then(op: Op): Op =
	SequenceOp(this, op)

fun sequenceOp(op1: Op, op2: Op): Op =
	op1.then(op2)

fun sequenceOp(op1: Op, op2: Op, op3: Op, op4: Op): Op =
	sequenceOp(
		sequenceOp(op1, op2),
		sequenceOp(op3, op4))

fun sequenceOp(op1: Op, op2: Op, op3: Op, op4: Op, op5: Op, op6: Op, op7: Op, op8: Op): Op =
	sequenceOp(
		sequenceOp(op1, op2, op3, op4),
		sequenceOp(op5, op6, op7, op8))

// === custom

val Variable<EnumBit>.notOp: Op
	get() =
		branchOp(setOp(oneBit), setOp(zeroBit))

fun Variable<EnumBit>.andOp(bitVariable: Variable<EnumBit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(zeroBit)),
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)))

fun Variable<EnumBit>.orOp(bitVariable: Variable<EnumBit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)),
		bitVariable.branchOp(setOp(oneBit), setOp(oneBit)))

fun Variable<EnumBit>.xorOp(bitVariable: Variable<EnumBit>): Op =
	branchOp(
		bitVariable.branchOp(setOp(zeroBit), setOp(oneBit)),
		bitVariable.branchOp(setOp(oneBit), setOp(zeroBit)))

fun Variable<EnumBit>.whileOne(op: Op): Op =
	WhileOp(this, op)
