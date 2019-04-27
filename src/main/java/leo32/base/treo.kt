package leo32.base

import leo.base.notNullIf
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.isZero

sealed class Treo(
	var trace: Treo? = null)

data class UnitTreo(
	val unit: Unit) : Treo()

data class BitTreo(
	val bit: Bit,
	val treo: Treo) : Treo()

data class BranchTreo(
	val at0: Treo,
	val at1: Treo,
	var enteredBitOrNull: Bit? = null) : Treo()

data class InvokeTreo(
	val treo: Treo) : Treo()

fun treo(unit: Unit): Treo = UnitTreo(unit)
fun treo(at0: Treo, at1: Treo): Treo = BranchTreo(at0, at1)
fun treo(bit: Bit, treo: Treo): Treo = BitTreo(bit, treo)
fun treo0(treo: Treo): Treo = treo(bit0, treo)
fun treo1(treo: Treo): Treo = treo(bit1, treo)

fun Treo.at(bit: Bit): Treo? =
	when (this) {
		is UnitTreo -> null
		is BitTreo -> treoAt(bit)
		is BranchTreo -> treoAt(bit)
		is InvokeTreo -> null
	}

fun BitTreo.treoAt(bit: Bit): Treo? =
	notNullIf(this.bit == bit) { treo }

fun BranchTreo.treoAt(bit: Bit): Treo =
	if (bit.isZero) at0 else at1

fun Treo.withTrace(treo: Treo): Treo {
	if (trace != null) error("already has trace")
	trace = treo
	return this
}

val Treo.withoutTrace: Treo
	get() {
		if (trace == null) error("already no trace")
		trace = null
		return this
	}

fun Treo.enter(bit: Bit): Treo? =
	when (this) {
		is UnitTreo -> null
		is BitTreo -> notNullIf(this.bit == bit) { treo.withTrace(this) }
		is BranchTreo -> this
			.apply { if (enteredBitOrNull != null) error("already entered") }
			.apply { enteredBitOrNull = bit }
			.run { if (bit.isZero) at0 else at1 }
			.withTrace(this)
		is InvokeTreo -> null
	}

fun BranchTreo.branchEnter(bit: Bit): Treo =
	this
		.apply { if (enteredBitOrNull != null) error("already entered") }
		.apply { enteredBitOrNull = bit }
		.run { if (bit.isZero) at0 else at1 }
		.withTrace(this)

val Treo.exit: Treo?
	get() {
		val exited = trace
		trace = null
		return exited?.returned
	}

tailrec fun Treo.clear() {
	exit?.returned?.clear()
}

val Treo.back: Treo?
	get() {
		val exited = trace
		trace = null
		return exited
	}

tailrec fun Treo.rewind(): Treo {
	val backed = back
	return if (backed == null) this
	else backed.rewind()
}

val Treo.returned: Treo?
	get() =
		when (this) {
			is UnitTreo -> null
			is BitTreo -> this
			is BranchTreo -> this
				.apply { if (enteredBitOrNull == null) error("not entered") }
				.apply { enteredBitOrNull = null }
			is InvokeTreo -> null
		}

val Treo.reenter: Treo?
	get() =
		when (this) {
			is UnitTreo -> null
			is BitTreo -> treo
			is BranchTreo -> enteredBitOrNull?.let { enteredBit -> this.at(enteredBit) }
			is InvokeTreo -> null
		}

fun Treo.consume(): Bit =
	when (this) {
		is UnitTreo -> error("not bit to consume")
		is BitTreo -> bit
		is BranchTreo -> treoConsume()
		is InvokeTreo -> error("no bit to consume")
	}

fun BranchTreo.treoConsume(): Bit {
	val bit = enteredBitOrNull
	enteredBitOrNull = null
	return bit ?: error("no bit to consume")
}

tailrec fun Treo.invoke(argument: Treo): Treo? =
	when (this) {
		is UnitTreo ->
			when (argument) {
				is UnitTreo -> this
				is BitTreo -> null
				is BranchTreo -> null
				is InvokeTreo -> null
			}
		is BitTreo ->
			when (argument) {
				is UnitTreo -> this
				is BitTreo ->
					if (bit == argument.bit) treo.invoke(argument.treo.withoutTrace)
					else null
				is BranchTreo -> {
					val argumentBit = argument.treoConsume()
					if (bit == argumentBit) treo.invoke(argument.treoAt(argumentBit).withoutTrace)
					else null
				}
				is InvokeTreo -> TODO()
			}
		is BranchTreo ->
			when (argument) {
				is UnitTreo -> this
				is BitTreo -> branchEnter(argument.bit).invoke(argument.treo.withoutTrace)
				is BranchTreo -> {
					val argumentBit = argument.treoConsume()
					treoAt(argumentBit).invoke(argument.treoAt(argumentBit).withoutTrace)
				}
				is InvokeTreo -> null
			}
		is InvokeTreo ->
			when (argument) {
				is UnitTreo -> TODO()
				is BitTreo -> TODO()
				is BranchTreo -> TODO()
				is InvokeTreo -> TODO()
			}
	}
