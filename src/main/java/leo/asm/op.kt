package leo.asm

import leo.binary.Bit

sealed class Op

object NoOp : Op()

data class ZeroOp(
	val dst: Bit) : Op()

data class OneOp(
	val dst: Bit) : Op()

data class LoadOp(
	val dst: Bit,
	val src: Bit) : Op()

data class BranchOp(
	val switch: Bit,
	val caseZero: Op,
	val caseOne: Op) : Op()

data class SequenceOp(
	val firstOp: Op,
	val secondOp: Op) : Op()

data class WhileOp(
	val condition: Bit,
	val op: Op) : Op()

// === constructors

val noOp = NoOp

val Bit.zeroOp: Op
	get() =
		ZeroOp(this)

val Bit.oneOp: Op
	get() =
		OneOp(this)

fun Bit.loadOp(bit: Bit): Op =
	LoadOp(this, bit)

fun Bit.branchOp(zeroOp: Op, oneOp: Op): Op =
	BranchOp(this, zeroOp, oneOp)

fun sequenceOp(op1: Op, op2: Op): Op =
	SequenceOp(op1, op2)

fun sequenceOp(op1: Op, op2: Op, op3: Op, op4: Op): Op =
	sequenceOp(
		sequenceOp(op1, op2),
		sequenceOp(op3, op4))

fun sequenceOp(op1: Op, op2: Op, op3: Op, op4: Op, op5: Op, op6: Op, op7: Op, op8: Op): Op =
	sequenceOp(
		sequenceOp(op1, op2, op3, op4),
		sequenceOp(op5, op6, op7, op8))

fun Bit.whileOne(op: Op): Op =
	WhileOp(this, op)
