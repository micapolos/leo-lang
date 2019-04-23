package leo32.leo.lib

import leo32.leo.*

val coreLib: Leo = {
	comment("core lib test")

	test {
		zero.comment { zero }.plus { one }
		gives { zero.plus { one } }
	}

	test { zero.gives { zero } }

	test {
		circle { radius }.radius
		gives { radius }
	}

	test {
		circle { radius }.circle
		gives { circle { radius }.circle }
	}

	test {
		vec { x { zero }.y { one } }.x
		gives { x { zero } }
	}

	test {
		vec { x { zero }.y { one } }.y
		gives { y { one } }
	}

	test {
		zero.doIt { zero }
		gives { zero.zero }
	}

	test {
		zero.doIt { plus { zero } }
		gives { zero.plus { zero } }
	}

//	test {
//		zero.doIt {
//			zero.gives { one }
//			zero
//		}
//		gives { zero.one }
//	}
//
//	test {
//		zero.gives { one }
//		doIt { quote { zero } }
//		gives { one }
//	}

	test { lhs.gives { quote { lhs } } }
	test { zero.lhs.gives { quote { zero.lhs } } }
	test { and { one }.lhs.gives { quote { and { one }.lhs } } }
	test { zero.and { one }.lhs.gives { zero } }

	test { rhs.gives { quote { rhs } } }
	test { zero.rhs.gives { quote { zero.rhs } } }
	test { and { one }.rhs.gives { quote { rhs { and { one } } } } }
	test { zero.and { one }.rhs.gives { one } }

	test {
		zero.and { one }.rhs.negate
		gives { one.negate }
	}

	test {
		zero.and { one }.lhs.negate
		gives { zero.negate }
	}

	doIt {
		zero.gives { one }
		test { zero.gives { one } }
	}

	doIt {
		zero.gives { one }
		one.gives { zero }
		test { one.gives { one } }
	}

	doIt {
		one.gives { two }
		zero.gives { one }
		test { zero.gives { two } }
	}

	doIt {
		zero.gives { zero }
		test { zero.gives { zero } }
	}

	doIt {
		zero.gives { self }
		test { zero.gives { quote { quote { self { zero } } } } }
	}

	doIt {
		zero.gives { self.negate }
		test { zero.gives { zero.self.negate } }
	}

	doIt {
		zero.gives { self.and { self } }
		test { zero.gives { quote { quote { zero.self.and { zero.self } } } } }
	}

	doIt {
		side.square.area
		gives { self.area.square.side.times { self.area.square.side } }
		test {
			side.square.area
			gives { side.times { side } }
		}
	}

	doIt {
		zero.gives { one }
		test { quote { zero }.gives { one } }
	}

	doIt {
		zero.plus { one }.gives { one }
		one.plus { one }.gives { two }
		test { zero.plus { one }.plus { one }.gives { two } }
	}

	doIt {
		zero.plus { one }.gives { one }
		one.plus { one }.gives { two }
		zero.plus { two }.gives { self.zero.plus { one }.plus { one } }
		test { zero.plus { two }.gives { two } }
	}

	doIt {
		ping.gives { pong }
		test { circle { ping }.gives { circle { pong } } }
	}

	doIt {
		ping.gives { pong }
		test { circle { with { ping } }.gives { circle { quote { quote { ping } } } } }
	}

	doIt {
		ping.gives { pong }
		test { circle { with { ping { ping } } }.gives { circle { ping { pong } } } }
	}

	test {
		circle { radius { circle } }
		gives { circle }
	}

	test {
		circle { with { radius { circle } } }
		gives { quote { circle { radius { circle } } } }
	}
}

fun main() {
	_test(coreLib)
}
