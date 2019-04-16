package leo32.leo

import leo.base.assertEqualTo
import leo.base.string
import leo32.runtime.isEmpty
import leo32.runtime.simpleAtOrNull
import kotlin.test.Test
import kotlin.test.fail
import kotlin.to

abstract class LeoTest {
	@Test
	fun gives() {
		_term {
			define { zero.gives { one } }
			zero
		}.assertEqualTo(_term { one })
	}

	@Test
	fun quote() {
		_term {
			define { zero.gives { one } }
			quote { zero }
		}.assertEqualTo(_term { zero })
	}

	@Test
	fun test_failure() {
		_term {
			test { zero.gives { zero } }
		}.assertEqualTo(_term { })
	}

	@Test
	fun test_pass() {
		_term {
			test { zero.gives { one } }
		}.assertEqualTo(
			_term {
				error {
					test { zero.gives { one } }
					expected { one }
					actual { zero }
				}
			})
	}

	@Test
	fun get() {
		_term {
			circle {
				radius { int(10) }
				center {
					x { int(12) }
					y { int(15) }
				}
			}
			circle.center.x
		}.assertEqualTo(_term { int(12) })
	}

	@Test
	fun all() {
		_test {
			define {
				bit
				has {
					either { zero }
					either { one }
				}
			}
			define {
				not { bit }
				gives {
					argument
					switch {
						case {
							not { bit { zero } }
							to { bit { one } }
						}
						case {
							not { bit { one } }
							to { bit { zero } }
						}
					}
				}
			}
			test {
				not { bit { zero } }
				gives { bit { one } }
			}
			test {
				not { bit { one } }
				gives { bit { zero } }
			}
			test {
				not { bit { x } }
				gives { quote { not { bit { x } } } }
			}
		}
	}
}

fun _test(leo: Leo) {
	_term(leo).run {
		if (isEmpty) Unit
		else simpleAtOrNull("error")
			?.run { fail(string) }
			?: fail(string)
	}
}
