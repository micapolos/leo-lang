package data

val New.i16 get() = data(i8, i8)

var Data.i16Int: Int
	get() = at0.i8Int.shl(8).or(at1.i8Int)
	set(int) {
		at0.i8Int = int.ushr(8); at1.i8Int = int
	}