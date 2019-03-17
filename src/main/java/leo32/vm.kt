package leo32

typealias T = Int

const val nullPtr = 0

class Vm(
	var mem: IntArray,
	var top: T)

val newVmCapacity = 65536
val newVm get() = Vm(IntArray(newVmCapacity), 0)

operator fun Vm.get(index: T) = mem[index]
operator fun Vm.set(index: T, int: T) {
	mem[index] = int
}

operator fun Vm.get(index: T, offset: T) = get(index - offset)
operator fun Vm.set(index: T, offset: T, t: T) {
	set(index - offset, t)
}

fun Vm.alloc(size: T, init: (T) -> Unit): T {
	top += size
	ensureSize()
	init(top)
	return top
}

fun Vm.ensureSize() {
	while (mem.size < top + 1) {
		mem = mem.copyOf(mem.size * 2)
	}
}

val Appendable.appendBegin get() = append(" ")
val Appendable.appendEnd get() = append(".")

fun Appendable.appendBegin(name: String) = this
	.append(name)
	.appendBegin

fun Appendable.appendField(key: String, appendValue: Appendable.() -> Appendable) = this
	.appendBegin(key)
	.appendValue()
	.appendEnd

fun Appendable.appendT(t: T) = appendPrimitive { append(t.toString()) }

fun Appendable.appendPrimitive(append: Appendable.() -> Appendable) = this
	.append()
	.append(" ")
	.appendEnd
