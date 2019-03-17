package leo32

import leo.base.ifThenElse
import leo.binary.Bit
import leo.binary.bit
import leo.binary.isZero

const val dictMask = 0
const val dictLeafValue = 1
const val dictInnerAt0 = 1
const val dictInnerAt1 = 2

fun Vm.dictLeaf(value: Ptr) = alloc(2) { dict ->
	set(dict, dictMask, 0)
	set(dict, dictLeafValue, value)
}

fun Vm.dictInner(mask: Ptr, at0: Ptr, at1: Ptr) = alloc(3) { dict ->
	set(dict, dictMask, mask)
	set(dict, dictInnerAt0, at0)
	set(dict, dictInnerAt1, at1)
}

fun Vm.dictMask(dict: Ptr) = get(dict, dictMask)
fun Vm.dictIsLeaf(dict: Ptr) = dictMask(dict) == 0
fun Vm.dictLeafValue(dict: Ptr) = get(dict, dictLeafValue)
fun Vm.dictInnerAt0(dict: Ptr) = get(dict, dictInnerAt0)
fun Vm.dictInnerAt1(dict: Ptr) = get(dict, dictInnerAt1)

fun Vm.dictInnerAt(dict: Ptr, bit: Bit): Ptr =
	if (bit.isZero) dictInnerAt0(dict)
	else dictInnerAt1(dict)

fun Vm.dictAt(dict: Ptr, index: Ptr): Ptr =
	if (dictIsLeaf(dict)) dictLeafValue(dict)
	else dictAt(dictInnerAt(dict, index.and(dictMask(dict)).bit), index)

// === print ===

fun Vm.dict(print: Print, dict: Ptr, value: Print.(Ptr) -> Print): Print =
	print.simple("dict") {
		ifThenElse(
			dictIsLeaf(dict),
			{ dictLeaf(print, dict, value) },
			{ dictInner(print, dict, value) })
	}

fun Vm.dictLeaf(print: Print, dictLeaf: Ptr, value: Print.(Ptr) -> Print) =
	print.simple("leaf") {
		value(dictLeafValue(dictLeaf))
	}

fun Vm.dictInner(print: Print, dictInner: Ptr, value: Print.(Ptr) -> Print) =
	print.complex("inner") {
		this
			.simple("mask") { print.ptr(dictMask(dictInner)) }
			.simple("at0") { dict(print, dictInnerAt0(dictInner), value) }
			.simple("at1") { dict(print, dictInnerAt1(dictInner), value) }
	}
