package leo.vm

import leo.base.Bit
import leo.base.Variable
import leo.base.oneBit
import leo.base.zeroBit

data class Int1(
	val bit: Variable<Bit>)

val Int1.notOp: Op
	get() =
		bit.notOp

fun Int1.andOp(int1: Int1): Op =
	bit.andOp(int1.bit)

fun Int1.orOp(int1: Int1): Op =
	bit.orOp(int1.bit)

fun Int1.xorOp(int1: Int1): Op =
	bit.xorOp(int1.bit)

fun Int1.incOp(carryBitVariable: Variable<Bit>): Op =
	bit.branchOp(
		carryBitVariable.branchOp(
			bit.setOp(zeroBit).then(carryBitVariable.setOp(zeroBit)),
			bit.setOp(oneBit).then(carryBitVariable.setOp(zeroBit))),
		carryBitVariable.branchOp(
			bit.setOp(oneBit).then(carryBitVariable.setOp(zeroBit)),
			bit.setOp(zeroBit).then(carryBitVariable.setOp(oneBit))))

fun Int1.addOp(int1: Int1, carryBitVariable: Variable<Bit>): Op =
	bit.branchOp(
		int1.bit.branchOp(
			carryBitVariable.branchOp(
				bit.setOp(zeroBit).then(carryBitVariable.setOp(zeroBit)),
				bit.setOp(oneBit).then(carryBitVariable.setOp(zeroBit))),
			carryBitVariable.branchOp(
				bit.setOp(zeroBit).then(carryBitVariable.setOp(zeroBit)),
				bit.setOp(oneBit).then(carryBitVariable.setOp(zeroBit)))),
		int1.bit.branchOp(
			carryBitVariable.branchOp(
				bit.setOp(oneBit).then(carryBitVariable.setOp(zeroBit)),
				bit.setOp(zeroBit).then(carryBitVariable.setOp(oneBit))),
			carryBitVariable.branchOp(
				bit.setOp(zeroBit).then(carryBitVariable.setOp(oneBit)),
				bit.setOp(oneBit).then(carryBitVariable.setOp(oneBit)))))
