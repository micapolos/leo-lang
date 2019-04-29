package leo32.c

data class i8(var ptr: ptr)

var i8.byte
	get() = mem.byte(ptr)
	set(byte) = mem.set(ptr, byte)
