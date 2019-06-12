package leo5.asm

data class Base(var ptr: PtrVar)

fun base(ptr: Ptr) = Base(new(ptr))
val Base.inc: Unit
	get() {
		ptr.int++
	}

fun Base.set(int: Int) {
	ptr.int = int
}

fun Base.add(offset: Offset) {
	ptr.int += offset.int
}
