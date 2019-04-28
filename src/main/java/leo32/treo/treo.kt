package leo32.treo

import leo.base.*
import leo.base.Stack
import leo.binary.*

sealed class Treo(
	var exitTrace: Treo? = null) {
	override fun toString() = charSeq.charString
}

data class LeafTreo(
	val leaf: Leaf) : Treo() {
	override fun toString() = super.toString()
}

data class SelectTreo(
	val select: Select) : Treo() {
	override fun toString() = super.toString()
}

data class VarTreo(
	val bitVar: Var,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class BranchTreo(
	val branch: Branch) : Treo() {
	override fun toString() = super.toString()
}

data class CaptureTreo(
	val bitVar: Var,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class ExpandTreo(
	val fn: Treo,
	val arg: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class CallTreo(
	val call: Call,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class BackTreo(
	val back: Back) : Treo() {
	override fun toString() = super.toString()
}

fun treo(leaf: Leaf) = LeafTreo(leaf)
fun treo(branch: Branch) = BranchTreo(branch)
fun treo01(at0: Treo, at1: Treo) = treo(branch(at0, at1))
fun treo(select: Select) = SelectTreo(select)
fun treo(bit: Bit, treo: Treo) = treo(bit select treo)
fun treo0(treo: Treo) = treo(bit0, treo)
fun treo1(treo: Treo) = treo(bit1, treo)
fun treo(bitVar: Var, treo: Treo) = VarTreo(bitVar, treo)
fun capture(bitVar: Var, treo: Treo) = CaptureTreo(bitVar, treo)
fun expand(fn: Treo, arg: Treo) = ExpandTreo(fn, arg)
fun treo(call: Call, treo: Treo) = CallTreo(call, treo)
fun treo(back: Back) = BackTreo(back)

fun Treo.withExitTrace(treo: Treo): Treo {
	if (exitTrace != null) error("already traced: $this")
	exitTrace = treo
	return this
}

fun Treo.enter(bit: Bit): Treo? =
	when (this) {
		is LeafTreo -> null
		is SelectTreo -> write(bit)
		is VarTreo -> write(bit)
		is BranchTreo -> write(bit)
		is CaptureTreo -> write(bit)
		is ExpandTreo -> null
		is CallTreo -> null
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

fun SelectTreo.write(bit: Bit): Treo? =
	select.at(bit)

fun VarTreo.write(bit: Bit): Treo? =
	apply { bitVar.set(bit) }

val VarTreo.bit: Bit
	get() =
		bitVar.bit

fun BranchTreo.write(bit: Bit): Treo =
	branch.at(bit)

fun CaptureTreo.write(bit: Bit): Treo =
	apply { bitVar.set(bit) }.treo

fun Treo.invoke(bit: Bit): Treo =
	(enter(bit) ?: error("$this.enter($bit)")).resolve()

fun Treo.invoke(string: String): String {
	val result = fold(string.charSeq.map { digitBitOrNull!! }, Treo::invoke)
	val resultString = result.charSeq.charString
	result.rewind()
	return resultString
}

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is LeafTreo -> this
		is SelectTreo -> invoke(treo.select.bit).invoke(treo.select.treo)
		is VarTreo -> invoke(treo.bit).invoke(treo.treo)
		is BranchTreo -> null!!
		is CaptureTreo -> null!!
		is ExpandTreo -> null!!
		is CallTreo -> null!!
		is BackTreo -> null!!
	}

fun Treo.resolve(): Treo =
	when (this) {
		is LeafTreo -> this
		is SelectTreo -> this
		is VarTreo -> this
		is BranchTreo -> this
		is CaptureTreo -> this
		is ExpandTreo -> resolve()
		is CallTreo -> resolve()
		is BackTreo -> invoke(back)
	}

fun ExpandTreo.resolve(): Treo {
	val result = fn.invoke(arg)
	rewind()
	fn.rewind()
	return result
}

fun CallTreo.resolve(): Treo {
	val result = call.fn.treo.invoke(call.param.treo).let { result ->
		result.rewind()
		treo.withExitTrace(this).resolve().invoke(result)
	}
	call.fn.treo.rewind()
	call.param.treo.rewind()
	return result
}

val Treo.charSeq: Seq<Char>
	get() =
		flatSeq(enteredCharSeq, seq('|'), trailingCharSeq)

val Treo.trailingCharSeq: Seq<Char>
	get() =
		Seq {
			when (this) {
				is LeafTreo -> null
				is SelectTreo -> select.charSeq.seqNodeOrNull
				is VarTreo -> seqNodeOrNull(bitVar.charSeq, treo.trailingCharSeq)
				is BranchTreo -> seqNode('?')
				is CaptureTreo -> seqNodeOrNull(seq('_'), treo.trailingCharSeq)
				is ExpandTreo -> seqNodeOrNull(seq('.'),
					fn.trailingCharSeq,
					seq('<'),
					arg.trailingCharSeq,
					seq('>'))
				is CallTreo -> seqNodeOrNull(
					seq('.'),
					call.fn.treo.trailingCharSeq,
					seq('('),
					call.param.treo.trailingCharSeq,
					seq(')'),
					treo.trailingCharSeq)
				is BackTreo -> back.charSeq.seqNodeOrNull
			}
		}

val Treo.exitChar: Char
	get() =
		when (this) {
			is LeafTreo -> '.'
			is SelectTreo -> select.bit.digitChar
			is VarTreo -> bitVar.bit.digitChar
			is BranchTreo -> '?'
			is CaptureTreo -> bitVar.bit.digitChar
			is ExpandTreo -> 'x'
			is CallTreo -> 'i'
			is BackTreo -> '<'
		}

val Treo.exitCharSeq: Seq<Char>
	get() =
		Seq {
			exitTrace?.let { treo ->
				treo.exitChar then treo.exitCharSeq
			}
		}

// TODO(micapolos): Implement without stack allocation.
val Treo.enteredCharSeq: Seq<Char>
	get() =
		nullOf<Stack<Char>>().fold(exitCharSeq) { push(it) }.seq
