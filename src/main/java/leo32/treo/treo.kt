package leo32.treo

import leo.base.appendableString
import leo.base.failIfOr
import leo.base.notNullIf
import leo.binary.*

sealed class Treo {
	override fun toString() = appendableString { it.append(this) }
}

data class UnitTreo(
	val unit: Unit) : Treo() {
	override fun toString() = super.toString()
}

data class BitTreo(
	val bit: Bit,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class VariableTreo(
	var bitOrNull: Bit?,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class BranchTreo(
	val at0: Treo,
	val at1: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class CaptureTreo(
	val variableTreo: VariableTreo,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class InvokeTreo(
	val treo: Treo,
	val argument: Treo) : Treo() {
	override fun toString() = super.toString()
}

fun treo(unit: Unit) = UnitTreo(unit)
fun branch(at0: Treo, at1: Treo) = BranchTreo(at0, at1)
fun treo(bit: Bit, treo: Treo) = BitTreo(bit, treo)
fun treo0(treo: Treo) = treo(bit0, treo)
fun treo1(treo: Treo) = treo(bit1, treo)
fun variable(treo: Treo) = VariableTreo(null, treo)
fun capture(variableTreo: VariableTreo, treo: Treo) = CaptureTreo(variableTreo, treo)
fun invoke(treo: Treo, argument: Treo) = InvokeTreo(treo, argument)

fun Treo.at(bit: Bit): Treo? =
	when (this) {
		is UnitTreo -> null
		is BitTreo -> at(bit)
		is VariableTreo -> at(bit)
		is BranchTreo -> at(bit)
		is CaptureTreo -> at(bit)
		is InvokeTreo -> null
	}

fun Treo.invoke(bit: Bit): Treo =
	at(bit)!!.resolve()

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is UnitTreo -> null!!
		is BitTreo -> invoke(treo.bit).invoke(treo)
		is VariableTreo -> invoke(treo.bit).invoke(treo)
		is BranchTreo -> null!!
		is CaptureTreo -> null!!
		is InvokeTreo -> null!!
	}

fun BitTreo.at(bit: Bit): Treo? =
	notNullIf(this.bit == bit) { treo }

fun VariableTreo.at(bit: Bit): Treo? =
	notNullIf(this.bitOrNull!! == bit) { treo }

val VariableTreo.bit: Bit
	get() =
		bitOrNull!!

fun VariableTreo.set(bit: Bit): VariableTreo =
	failIfOr(bitOrNull != null) { apply { bitOrNull = bit } }

fun VariableTreo.reset(): VariableTreo =
	failIfOr(bitOrNull == null) { apply { bitOrNull = null } }

fun BranchTreo.at(bit: Bit): Treo =
	if (bit.isZero) at0 else at1

fun CaptureTreo.at(bit: Bit): Treo =
	variableTreo.set(bit).treo

fun Treo.resolve(): Treo =
	(this as? InvokeTreo)?.resolve() ?: this

fun InvokeTreo.resolve(): Treo =
	treo.invoke(argument)

fun Appendable.append(treo: Treo): Appendable =
	when (treo) {
		is UnitTreo -> this
		is BitTreo -> append(treo.bit.digitChar).append(treo.treo)
		is VariableTreo -> append(treo.bitOrNull?.digitChar ?: '!')
		is BranchTreo -> append('?')
		is CaptureTreo -> append('_').append(treo.treo)
		is InvokeTreo -> append(".").append(treo.treo).append('(').append(treo.argument).append(')')
	}
