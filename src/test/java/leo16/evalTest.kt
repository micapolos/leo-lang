package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import kotlin.test.Test

fun Script.assertGives(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertGives { negate { zero } }
	}

	@Test
	fun nothing() {
		evaluate_ { nothing }.assertGives { nothing_ }
		evaluate_ { x { nothing } }.assertGives { x { nothing_ } }
		evaluate_ { x.nothing }.assertGives { nothing { x } }
		evaluate_ { x.nothing { y } }.assertGives { x.nothing { y } }
	}

	@Test
	fun sentences() {
		evaluate_ { nothing_ }.assertGives { nothing_ }
		evaluate_ { zero }.assertGives { zero }
		evaluate_ { zero.plus { one } }.assertGives { zero.plus { one } }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertGives { nothing_ }
		evaluate_ { quote { zero.negate } }.assertGives { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertGives { zero.is_ { one } }
	}

	@Test
	fun this_() {
		evaluate_ { this_ { nothing_ } }.assertGives { nothing_ }
		evaluate_ { x { zero }.this_ { nothing_ } }.assertGives { x { zero } }
		evaluate_ { x { zero }.this_ { y { one } } }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }.this_ { y { one }; z { two } } }.assertGives { x { zero }; y { one }; z { two } }
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
	fun getField() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertGives { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertGives { y { one } }
	}

	@Test
	fun getGiving() {
		evaluate_ { the { library { nothing_ } }.library }.assertGives { library { nothing_ } }
	}

	@Test
	fun getLibrary() {
		evaluate_ { the { giving { given } }.giving }.assertGives { giving { given } }
	}

	@Test
	fun thing() {
		evaluate_ { thing }.assertGives { thing }
		evaluate_ { point { x { zero }; y { one } }.thing }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; thing }.assertGives { thing { x { zero }; y { one } } }
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
	fun matchSentence() {
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
	fun matchList() {
		evaluate_ { list.match { empty.gives { given } } }
			.assertGives { given { empty } }
		evaluate_ { list { bit { zero } }.match { any.link.gives { given } } }
			.assertGives { given { link { previous { list }; last { bit { zero } } } } }
	}

	@Test
	fun giving() {
		evaluate_ { giving { given } }.assertGives { giving { given } }
	}

	@Test
	fun give() {
		evaluate_ { giving { given }.give { zero } }.assertGives { given { zero } }
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
				export { zero }
			}
		}

		evaluate_ {
			library {
				zero.is_ { one }
				one.is_ { zero }
			}
		}.assertGives {
			library {
				export { zero }
				export { one }
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
	fun load() {
		evaluate_ {
			load { bit }
			zero.bit.negate
		}.assertGives { bit { one } }
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
								list {
									binding {
										pattern { zero }
										body { one }
									}
								}
							}
						}
						export {
							library {
								binding {
									list {
										binding {
											pattern { zero }
											body { one }
										}
									}
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