package leo32.leo.lib

import leo32.leo.*

val byteLib: Leo = {
	_import(bitLib)

	define {
		byte.has {
			it { bit }
			it { bit }
			it { bit }
			it { bit }
			it { bit }
			it { bit }
			it { bit }
			it { bit }
		}
	}
}

fun main() {
	_test(byteLib)
}