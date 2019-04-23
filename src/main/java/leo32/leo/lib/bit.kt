package leo32.leo.lib

import leo32.leo.*

val bitLib: Leo = {
	bit.has {
		either { zero }
		either { one }
	}

	zero.bit.negate.gives { one.bit }
	one.bit.negate.gives { zero.bit }

	zero.bit.and { zero.bit }.gives { zero.bit }
	zero.bit.and { one.bit }.gives { zero.bit }
	one.bit.and { zero.bit }.gives { zero.bit }
	one.bit.and { one.bit }.gives { one.bit }

	zero.bit.or { zero.bit }.gives { zero.bit }
	zero.bit.or { one.bit }.gives { one.bit }
	one.bit.or { zero.bit }.gives { one.bit }
	one.bit.or { one.bit }.gives { one.bit }

	zero.bit.xor { zero.bit }.gives { zero.bit }
	zero.bit.xor { one.bit }.gives { one.bit }
	one.bit.xor { zero.bit }.gives { one.bit }
	one.bit.xor { one.bit }.gives { zero.bit }

	test { zero.bit.negate.negate.gives { zero.bit } }
}

fun main() {
	_test(bitLib)
}