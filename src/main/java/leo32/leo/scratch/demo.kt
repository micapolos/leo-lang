package leo32.leo.scratch

import leo32.leo.*
import leo32.leo.lib.bitLib

fun main() {
	_leo {
		_import(bitLib)
		zero.bit.and { one.bit }
	}
}