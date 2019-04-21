package leo32.leo.lib

import leo32.leo.*

val bitLib: Leo = {
	define {
		bit.has {
			either { zero }
			either { one }
		}
	}

	define { zero.bit.negate.gives { one.bit } }
	define { one.bit.negate.gives { zero.bit } }

	define { zero.bit.and { zero.bit }.gives { zero.bit } }
	define { zero.bit.and { one.bit }.gives { zero.bit } }
	define { one.bit.and { zero.bit }.gives { zero.bit } }
	define { one.bit.and { one.bit }.gives { one.bit } }

	define { zero.bit.or { zero.bit }.gives { zero.bit } }
	define { zero.bit.or { one.bit }.gives { one.bit } }
	define { one.bit.or { zero.bit }.gives { one.bit } }
	define { one.bit.or { one.bit }.gives { one.bit } }

	define { zero.bit.xor { zero.bit }.gives { zero.bit } }
	define { zero.bit.xor { one.bit }.gives { one.bit } }
	define { one.bit.xor { zero.bit }.gives { one.bit } }
	define { one.bit.xor { one.bit }.gives { zero.bit } }

	test { zero.bit.negate.negate.gives { zero.bit } }
}

fun main() {
	_test(bitLib)
}