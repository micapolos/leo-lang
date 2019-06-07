package data

val New.i8 get() = data(i4, i4)

var Data.i8Int: Int
	get() = at0.i4Int.shl(4).or(at1.i4Int)
	set(int) {
		at0.i4Int = int.ushr(4); at1.i4Int = int
	}