package data

import leo.binary.bit0
import leo.binary.int

val New.i1 get() = data0

var Data.i1Int: Int
	get() = bit.int
	set(int) {
		bit = int.bit0
	}
