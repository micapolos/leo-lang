package leo.asm

import leo.base.EnumBit

sealed class Op

object NoOp : Op()

data class ZeroOp(
	val dst: EnumBit) : Op()

data class OneOp(
	val dst: EnumBit) : Op()

data class LoadOp(
	val dst: EnumBit,
	val src: EnumBit) : Op()

data class BranchOp(
	val switch: EnumBit,
	val caseZero: Op,
	val caseOne: Op) : Op()

data class SequenceOp(
	val firstOp: Op,
	val secondOp: Op) : Op()

data class WhileOp(
	val condition: EnumBit,
	val op: Op) : Op()

// === constructors

val noOp = NoOp

val EnumBit.zeroOp: Op
	get() =
		ZeroOp(this)

val EnumBit.oneOp: Op
	get() =
		OneOp(this)

fun EnumBit.loadOp(bit: EnumBit): Op =
	LoadOp(this, bit)

fun EnumBit.branchOp(zeroOp: Op, oneOp: Op): Op =
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

fun EnumBit.whileOne(op: Op): Op =
	WhileOp(this, op)
