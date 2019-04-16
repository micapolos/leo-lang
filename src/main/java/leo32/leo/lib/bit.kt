package leo32.leo.lib

import leo32.leo.*

val bitLib: Leo = {
	define {
		bit.has {
			either { zero }
			either { one }
		}
	}

	test {
		describe { bit }
		gives {
			quote {
				bit.has {
					either { zero }
					either { one }
				}
			}
		}
	}

	define {
		bit.negate.gives {
			self.switch {
				case { zero.bit.negate.gives { one.bit } }
				case { one.bit.negate.gives { zero.bit } }
			}
		}
	}

	test { zero.bit.negate.gives { one.bit } }
	test { one.bit.negate.gives { zero.bit } }

	define {
		bit.and { bit }.gives {
			self.switch {
				case { zero.bit.and { zero.bit }.gives { zero.bit } }
				case { zero.bit.and { one.bit }.gives { zero.bit } }
				case { one.bit.and { zero.bit }.gives { zero.bit } }
				case { one.bit.and { one.bit }.gives { one.bit } }
			}
		}
	}

	test { zero.bit.and { zero.bit }.gives { zero.bit } }
	test { zero.bit.and { one.bit }.gives { zero.bit } }
	test { one.bit.and { zero.bit }.gives { zero.bit } }
	test { one.bit.and { one.bit }.gives { one.bit } }

	define {
		bit.or { bit }.gives {
			self.lhs.negate.and { self.rhs.negate }.negate
		}
	}

	test { zero.bit.or { zero.bit }.gives { zero.bit } }
//	test { zero.bit.or { one.bit }.gives { one.bit } }
	test { one.bit.or { zero.bit }.gives { one.bit } }
	test { one.bit.or { one.bit }.gives { one.bit } }
}