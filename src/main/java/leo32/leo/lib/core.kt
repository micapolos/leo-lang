package leo32.leo.lib

import leo32.leo.*

val coreLib: Leo = {
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

	test {
		zero.doIt {
			define { zero.gives { one } }
			zero
		}
		gives { zero.one }
	}

	test {
		define { zero.gives { one } }
		doIt { quote { zero } }
		gives { one }
	}

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

	test {
		define { zero.gives { one } }
		zero.gives { one }
	}

	test {
		define { zero.gives { one } }
		define { one.gives { zero } }
		one.gives { one }
	}

	test {
		define { one.gives { two } }
		define { zero.gives { one } }
		zero.gives { two }
	}

	test {
		define { zero.gives { zero } }
		zero.gives { zero }
	}

	test {
		define { zero.gives { self } }
		zero.gives { self { zero } }
	}

	test {
		define { zero.gives { self.negate } }
		zero.gives { zero.self.negate }
	}

	test {
		define { zero.gives { self.and { self } } }
		zero.gives { zero.self.and { zero.self } }
	}

	test {
		define {
			side.square.area
			gives { self.area.square.side.times { self.area.square.side } }
		}
		side.square.area
		gives { side.times { side } }
	}

	test {
		define { zero.gives { one } }
		quote { zero }.gives { one }
	}

	test {
		define { zero.plus { one }.gives { one } }
		define { one.plus { one }.gives { two } }
		zero.plus { one }.plus { one }.gives { two }
	}

	test {
		define { zero.plus { one }.gives { one } }
		define { one.plus { one }.gives { two } }
		define { zero.plus { two }.gives { self.zero.plus { one }.plus { one } } }
		zero.plus { two }.gives { two }
	}

//	test { script.gives { script } }
//	test { zero.script.gives { quote { script { line { string("zero").script } } } } }
//	test { zero.plus { one }.script.gives { quote {
//		script {
//			line { string("zero").script }
//			line {
//				string("plus")
//				script { line { string("one").script } } } } } } }

//	doIt {
//		define { bit.has { either { zero }.either { one } } }
//		define { bit.negate.gives { self.negate.bit.switch {
//			case { zero.bit.gives { one.bit } }
//			case { one.bit.gives { zero.bit } } } } }
//		define { number.has { either { bit }.either { infinite } } }
//		define { number.negate.gives { self.negate.number.switch {
//			case { number.bit.gives { self.number.bit.negate } }
//			case { number.infinite.gives { zero } } } } }
//		test { zero.bit.negate.gives { one.bit } }
//		test { one.bit.negate.gives { zero.bit } }
//	}
}

fun main() {
	_test(coreLib)
}
