package leo32.ptr

import leo.binary.Bit
import leo32.base.Branch
import leo32.base.at
import leo32.base.branchOrNull
import leo32.base.putOrNull

fun branchPtr(at0: Ptr, at1: Ptr): Ptr =
	branchOrNull(at0, at1)

@Suppress("UNCHECKED_CAST")
val Ptr.ptrBranch
	get() =
		this as Branch<Ptr>

fun Ptr.ptrBranchAt(bit: Bit): Ptr =
	if (this == null) null
	else ptrBranch.at(bit)

fun Ptr.ptrBranchPut(bit: Bit, value: Ptr): Ptr =
	if (this == null) branchOrNull(bit, value)
	else ptrBranch.putOrNull(bit, value)
