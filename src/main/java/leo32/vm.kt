package leo32

class Vm(
	var intArray: IntArray,
	var topPtr: Ptr)

val newVm get() = Vm(IntArray(1), 0)

operator fun Vm.get(index: Ptr) = intArray[index]
operator fun Vm.set(index: Ptr, int: Ptr) {
	intArray[index] = int
}

operator fun Vm.get(index: Ptr, offset: Ptr) = get(index - offset)
operator fun Vm.set(index: Ptr, offset: Ptr, ptr: Ptr) {
	set(index - offset, ptr)
}

fun Vm.alloc(size: Ptr, init: (Ptr) -> Unit): Ptr {
	topPtr += size
	ensureSize()
	init(topPtr)
	return topPtr
}

fun Vm.ensureSize() {
	while (intArray.size < topPtr + 1) {
		intArray = intArray.copyOf(intArray.size * 2)
	}
}

// === appending ===

val Appendable.appendBegin: Appendable
	get() = append(" ")

val Appendable.appendEnd: Appendable
	get() = append(".")

fun Appendable.appendBegin(name: String): Appendable = this
	.append(name)
	.appendBegin

fun Appendable.appendField(key: String, appendValue: Appendable.() -> Appendable): Appendable = this
	.appendBegin(key)
	.appendValue()
	.appendEnd

fun Appendable.appendPtr(ptr: Ptr): Appendable =
	appendPrimitive { append("0x").append(ptr.toString(16)) }

fun Appendable.appendPrimitive(append: Appendable.() -> Appendable): Appendable = this
	.append()
	.append(" ")
	.appendEnd
