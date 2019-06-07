package data

val New.i2 get() = data(i1, i1)

var Data.i2Int: Int
	get() = at0.i1Int.shl(1).or(at1.i1Int)
	set(int) {
		at0.i1Int = int.ushr(1); at1.i1Int = int
	}