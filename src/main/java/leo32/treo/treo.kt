package leo32.treo

import leo.base.*
import leo.binary.*

sealed class Treo(
	var exitTrace: Treo? = null) {
	override fun toString() = charSeq.charString
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
	val variable: Variable,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class BranchTreo(
	val at0: Treo,
	val at1: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class CaptureTreo(
	val variable: Variable,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class ExpandTreo(
	val fn: Treo,
	val arg: Treo) : Treo() {
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
fun treo(variable: Variable, treo: Treo) = VariableTreo(variable, treo)
fun capture(variable: Variable, treo: Treo) = CaptureTreo(variable, treo)
fun expand(fn: Treo, arg: Treo) = ExpandTreo(fn, arg)
fun invoke(fn: Treo, arg: Treo, cont: Treo) = InvokeTreo(fn, arg, cont)

fun Treo.withExitTrace(treo: Treo): Treo =
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
		is ExpandTreo -> null
		is InvokeTreo -> null
	}?.withExitTrace(this)

val Treo.exit: Treo?
	get() =
		run {
			val treo = exitTrace
			exitTrace = null
			treo
		}

fun BitTreo.write(bit: Bit): Treo? =
	notNullIf(this.bit == bit) { treo }

fun VariableTreo.write(bit: Bit): Treo? =
	apply { variable.set(bit) }

val VariableTreo.bit: Bit
	get() =
		variable.bit

fun BranchTreo.write(bit: Bit): Treo =
	if (bit.isZero) at0 else at1

fun CaptureTreo.write(bit: Bit): Treo =
	apply { variable.set(bit) }.treo

fun Treo.invoke(bit: Bit): Treo =
	(enter(bit) ?: error("$this.enter($bit)")).resolve()

fun Treo.invoke(string: String): String =
	fold(string.charSeq.map { digitBitOrNull!! }, Treo::invoke).string

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is UnitTreo -> this
		is BitTreo -> invoke(treo.bit).invoke(treo.treo)
		is VariableTreo -> invoke(treo.bit).invoke(treo.treo)
		is BranchTreo -> null!!
		is CaptureTreo -> null!!
		is ExpandTreo -> null!!
		is InvokeTreo -> null!!
	}

fun Treo.resolve(): Treo =
	when (this) {
		is UnitTreo -> this
		is BitTreo -> this
		is VariableTreo -> this
		is BranchTreo -> this
		is CaptureTreo -> this
		is ExpandTreo -> resolve()
		is InvokeTreo -> resolve()
	}

fun ExpandTreo.resolve(): Treo =
	fn.invoke(arg)

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
				is VariableTreo -> seqNodeOrNull(variable.charSeq, treo.charSeq)
				is BranchTreo -> seqNode('?')
				is CaptureTreo -> '_' then treo.charSeq
				is ExpandTreo -> seqNodeOrNull(seq('.'),
					fn.charSeq,
					seq('<'),
					arg.charSeq,
					seq('>'))
				is InvokeTreo -> seqNodeOrNull(
					seq('.'),
					fn.charSeq,
					seq('('),
					arg.charSeq,
					seq(')'),
					cont.charSeq)
			}
		}
