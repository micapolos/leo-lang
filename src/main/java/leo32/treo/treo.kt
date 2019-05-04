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

data class CaptureTreo(
	val capture: Capture,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

data class PutTreo(
	val put: Put,
	val treo: Treo) : Treo() {
	override fun toString() = super.toString()
}

fun treo(leaf: Leaf) = LeafTreo(leaf)
fun treo(branch: Branch) = BranchTreo(branch)
fun treo(at0: At0, at1: At1) = treo(branch(at0, at1))
fun treo(select: Select) = SelectTreo(select)
fun treo(bit: Bit, treo: Treo) = treo(value(constant(bit)), treo)
fun treo(value: Value, treo: Treo) = treo(value select treo)
fun treo(at0: At0) = treo(bit0, at0.treo)
fun treo(at1: At1) = treo(bit1, at1.treo)
fun treo(expand: Expand) = ExpandTreo(expand)
fun treo(call: Call, treo: Treo) = CallTreo(call, treo)
fun treo(back: Back) = BackTreo(back)
fun treo(capture: Capture, treo: Treo) = CaptureTreo(capture, treo)
fun treo(put: Put, treo: Treo) = PutTreo(put, treo)

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
		is ExpandTreo -> null
		is CallTreo -> null
		is BackTreo -> null
		is CaptureTreo -> write(bit)
		is PutTreo -> null
	}?.withExitTrace(this)

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
	select.enter(bit)

fun BranchTreo.write(bit: Bit): Treo =
	branch.select(bit)

fun CaptureTreo.write(bit: Bit): Treo {
	capture.enter(bit)
	return treo
}

fun Treo.invoke(bit: Bit, scope: Scope = voidScope): Treo =
	(enter(bit) ?: error("$this.enter($bit)")).resolve(scope)

fun Treo.invoke(string: String, scope: Scope = voidScope): String {
	val result = fold(string.charSeq.map { digitBitOrNull!! }) { bit -> invoke(bit, scope) }
	val resultString = result.bitString
	result.rewind()
	return resultString
}

tailrec fun Treo.invoke(treo: Treo, scope: Scope): Treo =
	when (treo) {
		is LeafTreo -> this
		is SelectTreo -> invoke(treo.select.value.bit, scope).invoke(treo.select.treo, scope)
		is BranchTreo -> invoke(treo.branch.enteredVariable.bit, scope).invoke(treo.branch.entered, scope)
		is ExpandTreo -> null!!
		is CallTreo -> null!!
		is BackTreo -> null!!
		is CaptureTreo -> null!!
		is PutTreo -> null!!
	}

tailrec fun Treo.resolve(scope: Scope = voidScope): Treo {
	val resolvedOnce = resolveOnce(scope)
	return if (resolvedOnce == null) this
	else resolvedOnce.resolve(scope)
}

fun Treo.resolveOnce(scope: Scope = voidScope): Treo? =
	when (this) {
		is LeafTreo -> null
		is SelectTreo -> null
		is BranchTreo -> null
		is ExpandTreo -> resolveOnce(scope)
		is CallTreo -> resolveOnce(scope)
		is BackTreo -> invoke(back)
		is CaptureTreo -> null
		is PutTreo -> resolveOnce(scope)
	}

fun ExpandTreo.resolveOnce(scope: Scope = voidScope): Treo {
	val result = expand.macro.treo.invoke(expand.param.treo, scope)
	rewind()
	expand.macro.treo.rewind()
	return result
}

fun CallTreo.resolveOnce(scope: Scope = voidScope): Treo {
	val result = call.invoke.let { result ->
		result.rewind()
		treo.withExitTrace(this).resolve(scope).invoke(result, scope)
	}
	call.fn.treo.rewind()
	call.param.treo.rewind()
	return result
}

fun PutTreo.resolveOnce(scope: Scope = voidScope): Treo {
	put.invoke(scope)
	return treo.withExitTrace(this)
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
			is CaptureTreo -> flatSeq(capture.charSeq, treo.trailingCharSeq)
			is PutTreo -> put.charSeq
		}

val Treo.exitCharSeq: Seq<Char>
	get() =
		when (this) {
			is LeafTreo -> seq()
			is SelectTreo -> seq(select.value.bit.digitChar)
			is BranchTreo -> seq(branch.enteredVariable.bit.digitChar)
			is ExpandTreo -> seq()
			is CallTreo -> seq()
			is BackTreo -> seq()
			is CaptureTreo -> capture.charSeq
			is PutTreo -> seq()
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
		is SelectTreo -> select.value.bit
		is BranchTreo -> branch.enteredVariable.bit
		is ExpandTreo -> fail()
		is CallTreo -> fail()
		is BackTreo -> fail()
		is CaptureTreo -> capture.variable.bit
		is PutTreo -> fail()
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
