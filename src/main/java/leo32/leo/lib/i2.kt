package leo32.leo.lib

import leo32.leo.*

val i2Lib = _leo {
	_import(i1Lib)

	define {
		i2.has {
			high { i1 }
			low { i1 }
		}
	}
}

fun main() {
	_main {
		_import(i2Lib)
		i2.print
	}
}