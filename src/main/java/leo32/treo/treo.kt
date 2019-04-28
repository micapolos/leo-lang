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
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class BackTreo(
	val back: Back) : Treo() {
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
fun invoke(fn: Treo, arg: Treo, treo: Treo) = InvokeTreo(fn, arg, treo)
fun treo(back: Back) = BackTreo(back)

fun Treo.withExitTrace(treo: Treo): Treo {
	if (exitTrace != null) error("already traced: $this")
	exitTrace = treo
	return this
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
		is BackTreo -> null
	}?.withExitTrace(this)

val Treo.exit: Treo?
	get() =
		run {
			val treo = exitTrace
			exitTrace = null
			treo
		}

tailrec fun Treo.rewind() {
	exit?.rewind()
}

tailrec fun Treo.invoke(back: Back): Treo {
	val exited = exit!!
	val nextBackOrNull = back.nextOrNull
	return if (nextBackOrNull == null) exited
	else exited.invoke(nextBackOrNull)
}

val Treo.cut: Treo
	get() =
		apply { rewind() }

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

fun Treo.invoke(string: String): String {
	val result = fold(string.charSeq.map { digitBitOrNull!! }, Treo::invoke)
	result.rewind()
	return result.string
}

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is UnitTreo -> this
		is BitTreo -> invoke(treo.bit).invoke(treo.treo)
		is VariableTreo -> invoke(treo.bit).invoke(treo.treo)
		is BranchTreo -> null!!
		is CaptureTreo -> null!!
		is ExpandTreo -> null!!
		is InvokeTreo -> null!!
		is BackTreo -> null!!
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
		is BackTreo -> invoke(back)
	}

fun ExpandTreo.resolve(): Treo {
	val result = fn.invoke(arg)
	rewind()
	fn.rewind()
	return result
}

fun InvokeTreo.resolve(): Treo {
	val result = fn.invoke(arg).let { result ->
		treo.invoke(result)
	}
	fn.rewind()
	arg.rewind()
	return result
}

val Treo.charSeq: Seq<Char>
	get() =
		Seq {
			when (this) {
				is UnitTreo -> null
				is BitTreo -> bit.digitChar then treo.charSeq
				is VariableTreo -> seqNodeOrNull(variable.charSeq, treo.charSeq)
				is BranchTreo -> seqNode('?')
				is CaptureTreo -> seqNodeOrNull(variable.charSeq, treo.charSeq)
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
					treo.charSeq)
				is BackTreo -> back.charSeq.seqNodeOrNull
			}
		}
