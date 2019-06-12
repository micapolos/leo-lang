package leo5.asm

data class PtrVar(var int: Int)

fun new(ptr: Ptr) = PtrVar(ptr.int)
var PtrVar.ptr
	get() = ptr(int)
	set(ptr) {
		int = ptr.int
	}

fun PtrVar.inc() {
	int++
}

operator fun PtrVar.plusAssign(int: Int) {
	this.int += int
}
