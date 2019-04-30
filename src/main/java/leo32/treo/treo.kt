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

data class CaptureTreo(
	val capture: Capture,
	val treo: Treo) : Treo() {
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

fun treo(leaf: Leaf) = LeafTreo(leaf)
fun treo(branch: Branch) = BranchTreo(branch)
fun treo(at0: At0, at1: At1) = treo(branch(at0, at1))
fun treo(select: Select) = SelectTreo(select)
fun treo(bit: Bit, treo: Treo) = treo(bit select treo)
fun treo(at0: At0) = treo(bit0, at0.treo)
fun treo(at1: At1) = treo(bit1, at1.treo)
fun treo(variable: Var, treo: Treo) = treo(branch(variable, at0(treo), at1(treo)))
fun treo(capture: Capture, treo: Treo) = CaptureTreo(capture, treo)
fun treo(expand: Expand) = ExpandTreo(expand)
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

fun BranchTreo.write(bit: Bit): Treo =
	branch.select(bit)

fun CaptureTreo.write(bit: Bit): Treo =
	apply { capture.variable.bit = bit }.treo

fun Treo.invoke(bit: Bit): Treo =
	(enter(bit) ?: error("$this.enter($bit)")).resolve()

fun Treo.invoke(string: String): String {
	val result = fold(string.charSeq.map { digitBitOrNull!! }, Treo::invoke)
	val resultString = result.enteredBitSeq.map { digitChar }.charString
	result.rewind()
	return resultString
}

tailrec fun Treo.invoke(treo: Treo): Treo =
	when (treo) {
		is LeafTreo -> this
		is SelectTreo -> invoke(treo.select.bit).invoke(treo.select.treo)
		is BranchTreo -> invoke(treo.branch.enteredVar.bit).invoke(treo.branch.entered)
		is CaptureTreo -> null!!
		is ExpandTreo -> null!!
		is CallTreo -> null!!
		is BackTreo -> null!!
	}

fun Treo.resolve(): Treo =
	when (this) {
		is LeafTreo -> this
		is SelectTreo -> this
		is BranchTreo -> this
		is CaptureTreo -> this
		is ExpandTreo -> resolve()
		is CallTreo -> resolve()
		is BackTreo -> invoke(back)
	}

// TODO: Resolve recursively until done
fun ExpandTreo.resolve(): Treo {
	val result = expand.macro.treo.invoke(expand.param.treo)
	rewind()
	expand.macro.treo.rewind()
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
		flatSeq(enteredBitSeq.map { digitChar }, seq('|'), trailingCharSeq)

val Treo.trailingCharSeq: Seq<Char>
	get() =
		when (this) {
			is LeafTreo -> leaf.charSeq
			is SelectTreo -> select.charSeq
			is BranchTreo -> branch.charSeqFrom(this)
			is CaptureTreo -> flatSeq(capture.charSeq, treo.trailingCharSeq)
			is ExpandTreo -> expand.charSeq
			is CallTreo -> flatSeq(call.charSeq, treo.trailingCharSeq)
			is BackTreo -> back.charSeq
		}

val Treo.exitBit: Bit
	get() =
	when (this) {
		is LeafTreo -> fail()
		is SelectTreo -> select.bit
		is BranchTreo -> branch.enteredVar.bit
		is CaptureTreo -> capture.variable.bit
		is ExpandTreo -> fail()
		is CallTreo -> fail()
		is BackTreo -> fail()
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
		enteredBitSeq.map { digitChar }.charString