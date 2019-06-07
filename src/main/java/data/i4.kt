package data

val New.i4 get() = data(i2, i2)

var Data.i4Int: Int
	get() = at0.i2Int.shl(2).or(at1.i2Int)
	set(int) {
		at0.i2Int = int.ushr(2); at1.i2Int = int
	}