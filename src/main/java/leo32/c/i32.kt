package leo32.c

data class i32(var ptr: ptr)

var i32.int
  get() = mem.int(ptr)
  set(int) = mem.set(ptr, int)
