package leo32.leo.scratch

import leo32.leo.*
import leo32.leo.lib.bitLib

fun main() {
	_main {
		_import(bitLib)

		describe { negate { bit { zero } } }
	}
}