package leo32

import leo.binary.Bit
import leo.binary.isZero

const val branchAt0 = 0
const val branchAt1 = 1

fun Vm.branch(at0: Ptr, at1: Ptr) = alloc(2) { branch ->
	set(branch, branchAt0, at0)
	set(branch, branchAt1, at1)
}

fun Vm.branchAt0(branch: Ptr) = get(branch, branchAt0)
fun Vm.branchAt1(branch: Ptr) = get(branch, branchAt1)

fun Vm.branchWith0(branch: Ptr, value: Ptr) = branch(value, branchAt1(branch))
fun Vm.branchWith1(branch: Ptr, value: Ptr) = branch(branchAt0(branch), value)

fun Vm.branchAt(branch: Ptr, bit: Bit): Ptr =
	if (bit.isZero) branchAt0(branch)
	else branchAt1(branch)

fun Vm.branchWith(branch: Ptr, bit: Bit, value: Ptr): Ptr =
	if (bit.isZero) branchWith0(branch, value)
	else branchWith1(branch, value)

fun Vm.branch(print: Print, branch: Ptr, value: Print.(Ptr) -> Print) =
	print.complex("branch") {
		this
			.simple("at0") { value(branchAt0(branch)) }
			.simple("at1") { value(branchAt1(branch)) }
	}
