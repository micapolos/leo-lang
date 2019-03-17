package leo32

import leo.base.ifThenElse
import leo.binary.bit

val emptyDict = nullPtr

fun Vm.dictAt(dict: Ptr, index: Ptr, mask: Ptr): Ptr =
	if (mask == 0) dict
	else dictAt(branchOrNullAt(dict, index.and(mask).bit), index, mask ushr 1)

fun Vm.dictWith(dict: Ptr, index: Ptr, mask: Ptr, value: Ptr): Ptr =
	if (mask == 0) value
	else index.and(mask).bit.let { bit ->
		branchOrNullWith(
			dict,
			bit,
			dictWith(branchOrNullAt(dict, bit), index, mask ushr 1, value))
	}

fun Vm.dictAt(dict: Ptr, index: Ptr) =
	dictAt(dict, index, 1 shl 31)

fun Vm.dictWith(dict: Ptr, index: Ptr, value: Ptr) =
	dictWith(dict, index, 1 shl 31, value)

// === print ===

fun Print.dict(vm: Vm, dict: Ptr, value: Print.(Ptr) -> Print): Print =
	simple("dict") {
		dictEntries(vm, dict, 0, 1 shl 31, value)
	}

fun Print.dictEntries(vm: Vm, dict: Ptr, index: Int, mask: Int, value: Print.(Ptr) -> Print) =
	ifThenElse(
		mask != 0,
		{ dictInner(vm, dict, index, mask, value) },
		{ dictLeaf(dict, index, value) })

fun Print.dictInner(vm: Vm, dict: Ptr, index: Ptr, mask: Ptr, value: Print.(Ptr) -> Print): Print =
	if (dict == nullPtr) this
	else this
		.dictEntries(vm, dict, index, mask ushr 1, value)
		.dictEntries(vm, dict, index.or(mask), mask ushr 1, value)

fun Print.dictLeaf(dict: Ptr, index: Ptr, value: Print.(Ptr) -> Print) =
	simple("0x${index.toString(16)}") {
		value(dict)
	}
