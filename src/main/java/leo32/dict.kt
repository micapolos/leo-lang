package leo32

import leo.base.ifThenElse
import leo.binary.Bit
import leo.binary.bit
import leo.binary.isZero

const val dictMask = 0
const val dictLeafValue = 1
const val dictInnerAt0 = 1
const val dictInnerAt1 = 2

fun Vm.dictLeaf(value: T) = alloc(2) { dict ->
	set(dict, dictMask, 0)
	set(dict, dictLeafValue, value)
}

fun Vm.dictInner(mask: T, at0: T, at1: T) = alloc(3) { dict ->
	set(dict, dictMask, mask)
	set(dict, dictInnerAt0, at0)
	set(dict, dictInnerAt1, at1)
}

fun Vm.dictMask(dict: T) = get(dict, dictMask)
fun Vm.dictIsLeaf(dict: T) = dictMask(dict) == 0
fun Vm.dictLeafValue(dict: T) = get(dict, dictLeafValue)
fun Vm.dictInnerAt0(dict: T) = get(dict, dictInnerAt0)
fun Vm.dictInnerAt1(dict: T) = get(dict, dictInnerAt1)

fun Vm.dictInnerAt(dict: T, bit: Bit): T =
	if (bit.isZero) dictInnerAt0(dict) else dictInnerAt1(dict)

fun Vm.dictAt(dict: T, index: T): T =
	if (dictIsLeaf(dict)) dictLeafValue(dict)
	else dictAt(dictInnerAt(dict, index.and(dictMask(dict)).bit), index)

// === appendables ===

fun Appendable.appendDict(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable = this
	.appendField("dict") {
		this
			.ifThenElse(
				vm.dictIsLeaf(dict),
				{ appendDictLeaf(vm, dict, appendValue) },
				{ appendDictInner(vm, dict, appendValue) })
	}

fun Appendable.appendDictLeaf(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable =
	appendField("leaf") {
		appendValue(vm.dictLeafValue(dict))
	}

fun Appendable.appendDictInner(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable =
	appendField("inner") {
		this
			.appendField("mask") {
				appendPrimitive {
					append("0x").append(vm.dictMask(dict).toString(16))
				}
			}
			.appendField("at0") {
				appendDict(vm, vm.dictInnerAt0(dict), appendValue)
			}
			.appendField("at1") {
				appendDict(vm, vm.dictInnerAt1(dict), appendValue)
			}
	}
