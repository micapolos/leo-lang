package data

val New.i32 get() = data(i16, i16)

var Data.i32Int: Int
	get() = at0.i16Int.shl(16).or(at1.i16Int)
	set(int) {
		at0.i16Int = int.ushr(16); at1.i16Int = int
	}

fun Data.i32Add(i32: Data) {
	i32Int += i32.i32Int
}