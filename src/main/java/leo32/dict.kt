package leo32

import leo.base.ifThenElse
import leo.binary.Bit
import leo.binary.bit
import leo.binary.isZero

const val dictMask = 0
const val leafDictValue = 1
const val innerDictAt0 = 1
const val innerDictAt1 = 2

fun Vm.leafDict(value: T) = alloc(2) { dict ->
	set(dict, dictMask, 0)
	set(dict, leafDictValue, value)
}

fun Vm.innerDict(mask: T, at0: T, at1: T) = alloc(3) { dict ->
	set(dict, dictMask, mask)
	set(dict, innerDictAt0, at0)
	set(dict, innerDictAt1, at1)
}

fun Vm.dictMask(dict: T) = get(dict, dictMask)
fun Vm.dictIsLeaf(dict: T) = dictMask(dict) == 0
fun Vm.leafDictValue(dict: T) = get(dict, leafDictValue)
fun Vm.innerDictAt0(dict: T) = get(dict, innerDictAt0)
fun Vm.innerDictAt1(dict: T) = get(dict, innerDictAt1)

fun Vm.innerDictAt(dict: T, bit: Bit): T =
	if (bit.isZero) innerDictAt0(dict) else innerDictAt1(dict)

fun Vm.dictAt(dict: T, index: T): T =
	if (dictIsLeaf(dict)) leafDictValue(dict)
	else dictAt(innerDictAt(dict, index.and(dictMask(dict)).bit), index)

// === appendables ===

fun Appendable.appendDict(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable = this
	.appendField("dict") {
		this
			.ifThenElse(
				vm.dictIsLeaf(dict),
				{ appendLeafDict(vm, dict, appendValue) },
				{ appendInnerDict(vm, dict, appendValue) })
	}

fun Appendable.appendLeafDict(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable =
	appendField("leaf") {
		appendValue(vm.leafDictValue(dict))
	}

fun Appendable.appendInnerDict(vm: Vm, dict: T, appendValue: Appendable.(T) -> Appendable): Appendable =
	appendField("inner") {
		this
			.appendField("mask") {
				appendPrimitive {
					append("0x").append(vm.dictMask(dict).toString(16))
				}
			}
			.appendField("at0") {
				appendDict(vm, vm.innerDictAt0(dict), appendValue)
			}
			.appendField("at1") {
				appendDict(vm, vm.innerDictAt1(dict), appendValue)
			}
	}
