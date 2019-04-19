package leo32.leo.lib

import leo32.leo.*

val bitLib: Leo = {
	define {
		bit
		has {
			either { zero }
			either { one }
		}
	}

	test {
		describe { bit }
		gives {
			quote {
				bit
				has {
					either { zero }
					either { one }
				}
			}
		}
	}

	define {
		bit.negate
		gives {
			self.negate.bit
			switch {
				case { zero.bit.gives { one.bit } }
				case { one.bit.gives { zero.bit } }
			}
		}
	}

	test { zero.bit.negate.gives { one.bit } }
	test { one.bit.negate.gives { zero.bit } }
	test { zero.bit.negate.negate.gives { zero.bit } }

	define {
		bit.and { bit }
		gives {
			self.bit
			switch {
				case {
					zero.bit
					gives {
						self.and.bit
						switch {
							case { zero.bit.gives { zero.bit } }
							case { one.bit.gives { zero.bit } }
						}
					}
				}
				case {
					one.bit
					gives {
						self.and.bit
						switch {
							case { zero.bit.gives { zero.bit } }
							case { one.bit.gives { one.bit } }
						}
					}
				}
			}
		}
	}

	test { zero.bit.and { zero.bit }.gives { zero.bit } }
	test { zero.bit.and { one.bit }.gives { zero.bit } }
	test { one.bit.and { zero.bit }.gives { zero.bit } }
	test { one.bit.and { one.bit }.gives { one.bit } }

	define {
		bit.or { bit }
		gives { self.bit.negate.and { self.or.bit.negate }.negate }
	}

	test { zero.bit.or { zero.bit }.gives { zero.bit } }
	test { zero.bit.or { one.bit }.gives { one.bit } }
	test { one.bit.or { zero.bit }.gives { one.bit } }
	test { one.bit.or { one.bit }.gives { one.bit } }
}

fun main() {
	_test(bitLib)
}