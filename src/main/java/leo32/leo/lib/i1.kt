package leo32.leo.lib

import leo32.leo.*

val i1Lib = _leo {
	_import(bitLib)

	i1.has { bit }
}

fun main() {
	_main {
		_import(i1Lib)
		i1.print
	}
}