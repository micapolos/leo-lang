package leo32.treo

import leo.base.*
import leo.binary.*

sealed class Treo(
	var exitTrace: Treo? = null) {
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
	val fn: Treo,
	val arg: Treo,
	val cont: Treo) : Treo() {
	override fun toString() = super.toString()
}

fun treo(unit: Unit) = UnitTreo(unit)
fun branch(at0: Treo, at1: Treo) = BranchTreo(at0, at1)
fun treo(bit: Bit, treo: Treo) = BitTreo(bit, treo)
fun treo0(treo: Treo) = treo(bit0, treo)
fun treo1(treo: Treo) = treo(bit1, treo)
fun variable(treo: Treo) = VariableTreo(null, treo)
fun capture(variableTreo: VariableTreo, treo: Treo) = CaptureTreo(variableTreo, treo)
fun invoke(fn: Treo, arg: Treo, cont: Treo) = InvokeTreo(fn, arg, cont)

fun Treo.withExitTrace(treo: Treo) =
	failIfOr(exitTrace != null) {
		apply {
			exitTrace = treo
		}
	}

fun Treo.enter(bit: Bit): Treo? =
	when (this) {
		is UnitTreo -> null
		is BitTreo -> write(bit)
		is VariableTreo -> write(bit)
		is BranchTreo -> write(bit)
		is CaptureTreo -> write(bit)
		is InvokeTreo -> null
	}?.withExitTrace(this)

val Treo.exit: Treo?
	get() =
		erase.run {
			val treo = exitTrace
			exitTrace = null
			treo
		}

val Treo.erase: Treo
	get() =
		when (this) {
			is UnitTreo -> this
			is BitTreo -> this
			is VariableTreo -> this
			is BranchTreo -> this
			is CaptureTreo -> apply { variableTreo.erase() }
			is InvokeTreo -> this
		}

fun BitTreo.write(bit: Bit): Treo? =
	notNullIf(this.bit == bit) { treo }

fun VariableTreo.write(bit: Bit): Treo? =
	notNullIf(this.bitOrNull!! == bit) { treo }

val VariableTreo.bit: Bit
	get() =
		bitOrNull!!

fun VariableTreo.set(bit: Bit): VariableTreo =
	failIfOr(bitOrNull != null) { apply { bitOrNull = bit } }

fun VariableTreo.erase(): VariableTreo =
	failIfOr(bitOrNull == null) { apply { bitOrNull = null } }

fun BranchTreo.write(bit: Bit): Treo =
	if (bit.isZero) at0 else at1

fun CaptureTreo.write(bit: Bit): Treo =
	variableTreo.set(bit).treo

fun Treo.invoke(bit: Bit): Treo =
	enter(bit)!!.resolve()

fun Treo.invoke(string: String): String =
	fold(string.charSeq.map { digitBitOrNull!! }, Treo::invoke).string

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is UnitTreo -> null!!
		is BitTreo -> invoke(treo.bit).invoke(treo)
		is VariableTreo -> invoke(treo.bit).invoke(treo)
		is BranchTreo -> null!!
		is CaptureTreo -> null!!
		is InvokeTreo -> null!!
	}

fun Treo.resolve(): Treo =
	(this as? InvokeTreo)?.resolve() ?: this

fun InvokeTreo.resolve(): Treo =
	fn.invoke(arg).let { result ->
		cont.invoke(result)
	}

val Treo.charSeq: Seq<Char>
	get() =
		Seq {
			when (this) {
				is UnitTreo -> null
				is BitTreo -> bit.digitChar then treo.charSeq
				is VariableTreo -> seqNode('!')
				is BranchTreo -> seqNode('?')
				is CaptureTreo -> '_' then treo.charSeq
				is InvokeTreo -> seqNodeOrNull(
					seq('.'),
					fn.charSeq,
					seq('('),
					arg.charSeq,
					seq(')'),
					cont.charSeq)
			}
		}

fun Appendable.append(treo: Treo): Appendable =
	fold(treo.charSeq, Appendable::append)
