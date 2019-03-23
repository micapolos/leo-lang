package leo32

import leo.binary.Bit
import leo.binary.isZero

data class Branch<T>(
	val at0: T,
	val at1: T)

fun <T> branch(at0: T, at1: T) =
	Branch(at0, at1)

fun <T> Branch<T>.at(bit: Bit) =
	if (bit.isZero) at0 else at1

fun <T> Branch<T>.put(bit: Bit, value: T) =
	if (bit.isZero) branch(value, at1)
	else branch(at0, value)

// === nullable branches

fun <T : Any> branchOrNull(at0: T?, at1: T?): Branch<T?>? =
	if (at0 == null && at1 == null) null
	else Branch(at0, at1)

fun <T : Any> branchOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, null)
	else branchOrNull(null as T?, value)

fun <T : Any> Branch<T?>.putOrNull(bit: Bit, value: T?): Branch<T?>? =
	if (bit.isZero) branchOrNull(value, at1)
	else branchOrNull(at0, value)

// === ptr

fun branchPtr(at0: Ptr, at1: Ptr): Ptr =
	if (at0 == null && at1 == null) null
	else branch(at0, at1)

@Suppress("UNCHECKED_CAST")
val Ptr.branchPtr
	get() =
		this as Branch<Ptr>

@Suppress("UNCHECKED_CAST")
fun Ptr.ptrBranchAt(bit: Bit): Ptr =
	if (this == null) null
	else branchPtr.at(bit)

@Suppress("UNCHECKED_CAST")
fun Ptr.ptrBranchPut(bit: Bit, value: Ptr): Ptr =
	if (this == null) branchOrNull(bit, value)
	else branchPtr.putOrNull(
		bit, value)
