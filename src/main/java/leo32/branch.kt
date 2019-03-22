package leo32

import leo.binary.Bit
import leo.binary.isZero

typealias Ptr = Any?

data class Branch(
	val at0: Ptr,
	val at1: Ptr)

val emptyBranch: Ptr = null

fun branch(at0: Ptr, at1: Ptr) =
	if (at0 == null && at1 == null) emptyBranch
	else Branch(at0, at1)

fun branch(bit: Bit, value: Ptr) =
	if (value == null) emptyBranch
	else if (bit.isZero) Branch(value, null)
	else Branch(null, value)

fun Branch.at(bit: Bit) =
	if (bit.isZero) at0 else at1

fun Branch.put(bit: Bit, value: Ptr) =
	if (bit.isZero) branch(value, at1) else branch(at0, value)

// === unsafe

val Ptr.branch get() = this as Branch

fun Ptr.branchAt(bit: Bit) =
	if (this == null) null
	else branch.at(bit)

fun Ptr.branchPut(bit: Bit, value: Ptr) =
	if (this == null) branch(bit, value)
	else branch.put(bit, value)
