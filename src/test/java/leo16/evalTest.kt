package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

fun Script.assertGives(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun appending() {
		evaluate_ { nothing_ }.assertGives { nothing_ }
		evaluate_ { zero }.assertGives { zero }
		evaluate_ { zero.plus { one } }.assertGives { zero.plus { one } }
	}

	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertGives { negate { zero } }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertGives { nothing_ }
		evaluate_ { quote { zero.negate } }.assertGives { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertGives { zero.is_ { one } }
	}

	@Test
	fun evaluate() {
		evaluate_ { quote { nothing_ }.evaluate }.assertGives { nothing_ }
		evaluate_ { quote { zero.negate }.evaluate }.assertGives { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.evaluate }.assertGives { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.evaluate }.assertGives { one }
		evaluate_ { quote { zero.is_ { one } }.evaluate.zero }.assertGives { zero }
	}

	@Test
	fun compile() {
		evaluate_ { quote { nothing_ }.compile }.assertGives { nothing_ }
		evaluate_ { quote { zero.negate }.compile }.assertGives { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.compile }.assertGives { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.compile }.assertGives { one }
		evaluate_ { quote { zero.is_ { one } }.compile.zero }.assertGives { one }
	}

	@Test
	fun get() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertGives { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertGives { y { one } }
	}

	@Test
	fun thing() {
		evaluate_ { thing }.assertGives { thing }
		evaluate_ { point { x { zero }; y { one } }.thing }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; thing }.assertGives { thing { x { zero }; y { one } } }
	}

	@Test
	fun list() {
		evaluate_ { list { bit { zero }; bit { one } }.last }
			.assertGives { last { bit { one } } }
		evaluate_ { list { bit { zero }; bit { one } }.previous }
			.assertGives { previous { list { bit { zero } } } }
		evaluate_ { list.append { zero.bit } }
			.assertGives { list { bit { zero } } }
		evaluate_ { list.append { zero.bit }.append { one.bit } }
			.assertGives { list { bit { zero }; bit { one } } }

		evaluate_ { list.match { empty.gives { given } } }
			.assertGives { given { empty } }
		evaluate_ { list { bit { zero } }.match { any.link.gives { given } } }
			.assertGives { given { link { previous { list }; last { bit { zero } } } } }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertGives { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertGives { one }
		evaluate_ { any.is_ { one }.zero }.assertGives { one }
	}

	@Test
	fun gives() {
		evaluate_ { zero.gives { one } }.assertGives { nothing_ }
		evaluate_ { zero.gives { one }.zero }.assertGives { one }
		evaluate_ { zero.gives { one }.one }.assertGives { one }
		evaluate_ { zero.gives { given }.zero }.assertGives { given { zero } }
		evaluate_ { zero.gives { given }.one }.assertGives { one }
	}

	@Test
	fun match() {
		evaluate_ { zero.bit.match { zero.is_ { one } } }
			.assertGives { one }
		evaluate_ { zero.bit.match { zero.gives { given } } }
			.assertGives { given { zero } }

		evaluate_ { zero.bit.match { nothing_ } }
			.assertGives { match { bit { zero } } }
		evaluate_ { zero.bit.match { one } }
			.assertGives { bit { zero }.match { one } }
		evaluate_ { zero.bit.match { zero.is_ { one }.one } }
			.assertGives { bit { zero }.match { zero.is_ { one }.one } }
	}

	@Test
	fun library() {
		evaluate_ {
			library { nothing_ }
		}.assertGives { library }

		evaluate_ {
			library {
				zero.is_ { one }
			}
		}.assertGives {
			library {
				pattern { zero }
			}
		}

		evaluate_ {
			library {
				zero.is_ { one }
				one.is_ { zero }
			}
		}.assertGives {
			library {
				pattern { zero }
				pattern { one }
			}
		}
	}

	@Test
	fun import() {
		evaluate_ { library { nothing_ }.import }.assertGives { nothing_ }
		evaluate_ { library { zero.is_ { one } }.import }.assertGives { nothing_ }
		evaluate_ { library { zero.is_ { one } }.import.zero }.assertGives { one }

		evaluate_ { zero.import { library { zero.is_ { one } } } }.assertGives { zero }
		evaluate_ { zero.import { library { zero.is_ { one } } }.evaluate }.assertGives { one }
	}

	@Test
	fun compiler() {
		evaluate_ {
			zero.is_ { one }.compiler
		}.assertGives {
			compiler {
				parent { nothing }
				compiled {
					scope {
						library {
							binding {
								pattern { zero }
								body { one }
							}
						}
						export {
							library {
								binding {
									pattern { zero }
									body { one }
								}
							}
						}
					}
					value { nothing_ }
				}
				meta { false_ }
			}
		}
	}
}