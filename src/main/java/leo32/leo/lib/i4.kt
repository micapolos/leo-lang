package leo32.leo.lib

import leo32.leo.*

val i4Lib = _leo {
	_import(i2Lib)

	i4.has {
		high { i2 }
		low { i2 }
	}
}

fun main() {
	_main {
		_import(i4Lib)
		i4.print
	}
}