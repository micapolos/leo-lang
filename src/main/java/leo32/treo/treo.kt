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

data class BranchTreo(
	val branch: Branch) : Treo() {
	override fun toString() = super.toString()
}

data class ExpandTreo(
	val expand: Expand) : Treo() {
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

data class WriteTreo(
	val write: Write,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

fun treo(leaf: Leaf) = LeafTreo(leaf)
fun treo(branch: Branch) = BranchTreo(branch)
fun treo(at0: At0, at1: At1) = treo(branch(at0, at1))
fun treo(select: Select) = SelectTreo(select)
fun treo(bit: Bit, treo: Treo) = treo(bit select treo)
fun treo(at0: At0) = treo(bit0, at0.treo)
fun treo(at1: At1) = treo(bit1, at1.treo)
fun treo(variable: Var, treo: Treo) = treo(branch(variable, at0(treo), at1(treo)))
fun treo(expand: Expand) = ExpandTreo(expand)
fun treo(call: Call, treo: Treo) = CallTreo(call, treo)
fun treo(back: Back) = BackTreo(back)
fun treo(write: Write, treo: Treo) = WriteTreo(write, treo)

fun Treo.withExitTrace(treo: Treo): Treo {
	if (exitTrace != null) error("already traced: $this")
	exitTrace = treo
	return this
}

fun Treo.enter(bit: Bit, writer: Writer = nullWriter): Treo? =
	when (this) {
		is LeafTreo -> null
		is SelectTreo -> write(bit)
		is BranchTreo -> write(bit)
		is ExpandTreo -> null
		is CallTreo -> null
		is BackTreo -> null
		is WriteTreo -> write(bit, writer)
	}?.withExitTrace(this)

inline fun Treo.edit(bit: Bit, fn: () -> Treo): Treo? =
	when (this) {
		is LeafTreo -> treo(bit, fn().withExitTrace(this))
		is SelectTreo ->
			if (select.bit == bit) treo(bit, fn().withExitTrace(this))
			else treo(branch(bit, fn().withExitTrace(this), select.treo))
		is BranchTreo -> treo(branch(bit, fn().withExitTrace(this), branch.at(bit.inverse)))
		is ExpandTreo -> null
		is CallTreo -> null
		is BackTreo -> null
		is WriteTreo -> null
	}

fun Treo.replace(treo: Treo): Treo {
	val oldExitTrace = exitTrace
	exitTrace = null
	treo.exitTrace = oldExitTrace
	return treo
}

val Treo.exit: Treo?
	get() =
		run {
			val treo = exitTrace
			exitTrace = null
			treo
		}

val Treo.exitRoot: Treo
	get() {
		val exited = exit
		return if (exited == null) this
		else exited.exitRoot
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

fun BranchTreo.write(bit: Bit): Treo =
	branch.select(bit)

fun WriteTreo.write(bit: Bit, writer: Writer): Treo =
	apply { write.invoke(writer, bit) }.treo

fun Treo.invoke(bit: Bit, writer: Writer = nullWriter): Treo =
	(enter(bit, writer) ?: error("$this.enter($bit)")).resolve(writer)

fun Treo.invoke(string: String, writer: Writer = nullWriter): String {
	val result = fold(string.charSeq.map { digitBitOrNull!! }) { bit -> invoke(bit, writer) }
	val resultString = result.bitString
	result.rewind()
	return resultString
}

tailrec fun Treo.invoke(treo: Treo, writer: Writer): Treo =
	when (treo) {
		is LeafTreo -> this
		is SelectTreo -> invoke(treo.select.bit, writer).invoke(treo.select.treo, writer)
		is BranchTreo -> invoke(treo.branch.enteredVar.bit, writer).invoke(treo.branch.entered, writer)
		is ExpandTreo -> null!!
		is CallTreo -> null!!
		is BackTreo -> null!!
		is WriteTreo -> null!!
	}

tailrec fun Treo.resolve(writer: Writer = nullWriter): Treo {
	val resolvedOnce = resolveOnce(writer)
	return if (resolvedOnce == null) this
	else resolvedOnce.resolve(writer)
}

fun Treo.resolveOnce(writer: Writer = nullWriter): Treo? =
	when (this) {
		is LeafTreo -> null
		is SelectTreo -> null
		is BranchTreo -> null
		is ExpandTreo -> resolveOnce(writer)
		is CallTreo -> resolveOnce(writer)
		is BackTreo -> invoke(back)
		is WriteTreo -> null
	}

fun ExpandTreo.resolveOnce(writer: Writer = nullWriter): Treo {
	val result = expand.macro.treo.invoke(expand.param.treo, writer)
	rewind()
	expand.macro.treo.rewind()
	return result
}

fun CallTreo.resolveOnce(writer: Writer = nullWriter): Treo {
	val result = call.invoke.let { result ->
		result.rewind()
		treo.withExitTrace(this).resolve(writer).invoke(result, writer)
	}
	call.fn.treo.rewind()
	call.param.treo.rewind()
	return result
}

val Treo.charSeq: Seq<Char>
	get() =
		flatSeq(enteredBitSeq.map { digitChar }, seq('|'), trailingCharSeq)

val Treo.trailingCharSeq: Seq<Char>
	get() =
		when (this) {
			is LeafTreo -> leaf.charSeq
			is SelectTreo -> select.charSeq
			is BranchTreo -> branch.charSeqFrom(this)
			is ExpandTreo -> expand.charSeq
			is CallTreo -> flatSeq(call.charSeq, treo.trailingCharSeq)
			is BackTreo -> back.charSeq
			is WriteTreo -> write.charSeq
		}

val Treo.exitCharSeq: Seq<Char>
	get() =
		when (this) {
			is LeafTreo -> seq()
			is SelectTreo -> seq(select.bit.digitChar)
			is BranchTreo -> seq(branch.enteredVar.bit.digitChar)
			is ExpandTreo -> seq()
			is CallTreo -> seq()
			is BackTreo -> seq()
			is WriteTreo -> seq()
		}

val Treo.exitCharSeqSeq: Seq<Seq<Char>>
	get() =
		Seq {
			exitTrace?.let { treo ->
				treo.exitCharSeq then treo.exitCharSeqSeq
			}
		}

val Treo.enteredCharSeq: Seq<Char>
	get() =
		nullOf<Stack<Seq<Char>>>().fold(exitCharSeqSeq) { push(it) }.seq.flat

val Treo.exitBit: Bit
	get() =
	when (this) {
		is LeafTreo -> fail()
		is SelectTreo -> select.bit
		is BranchTreo -> branch.enteredVar.bit
		is ExpandTreo -> fail()
		is CallTreo -> fail()
		is BackTreo -> fail()
		is WriteTreo -> fail()
	}

val Treo.exitBitSeq: Seq<Bit>
	get() =
		Seq {
			exitTrace?.let { treo ->
				treo.exitBit then treo.exitBitSeq
			}
		}

val Treo.enteredBitSeq: Seq<Bit>
	get() =
		nullOf<Stack<Bit>>().fold(exitBitSeq) { push(it) }.seq

val Treo.bitString: String
	get() =
		enteredCharSeq.charString
