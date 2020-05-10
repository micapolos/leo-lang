package leo16

import leo.base.assertEqualTo
import leo14.Script
import leo15.dsl.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

fun Script.assertGives(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertGives { negate { zero } }
	}

	@Test
	fun comment() {
		evaluate_ { comment { nothing_ } }.assertGives { nothing_ }
		evaluate_ { comment { zero } }.assertGives { nothing_ }
		evaluate_ {
			comment { two { dimensional { point } } }
			point {
				comment { x.coordinate }
				x { 10.number }
				comment { y.coordinate }
				y { 20.number }
			}
		}.assertGives {
			point {
				x { 10.number }
				y { 20.number }
			}
		}
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
	fun literal() {
		evaluate_ { "foo".text }.assertGives { "foo".text }
		evaluate_ { 123.number }.assertGives { 123.number }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertGives { nothing_ }
		evaluate_ { quote { zero.negate } }.assertGives { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertGives { zero.is_ { one } }
		evaluate_ { zero.quote { one.two } }.assertGives { zero.one.two }
		evaluate_ { zero.one.quote { two } }.assertGives { one { zero }.two }
	}

	@Test
	fun script() {
		evaluate_ { zero.giving { given }.script.take { zero } }
			.assertGives { taking { zero }.take { zero } }
	}

	@Test
	fun leonardo() {
		evaluate_ { leonardo.author.text }.assertGives { "Michał Pociecha-Łoś".text }
	}

	@Test
	fun this_() {
		evaluate_ { this_ { nothing_ } }.assertGives { nothing_ }
		evaluate_ { x { zero }.this_ { nothing_ } }.assertGives { x { zero } }
		evaluate_ { x { zero }.this_ { y { one } } }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }.this_ { y { one }; z { two } } }.assertGives { x { zero }; y { one }; z { two } }
		evaluate_ { this_ { zero }; this_ { one } }.assertGives { zero; one }
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
	fun getSpecial() {
		evaluate_ { the { "foo".text }.text }.assertGives { "foo".text }
		evaluate_ { the { 123.number }.number }.assertGives { 123.number }
		evaluate_ { the { dictionary { nothing_ } }.dictionary }.assertGives { dictionary { definition { list { empty } } } }
		evaluate_ { the { zero.giving { given } }.taking }.assertGives { taking { zero } }
		evaluate_ { the { x { zero }.giving { x } }.take { x { zero } } }.assertGives { x { zero } }
	}

	@Test
	fun content() {
		evaluate_ { content }.assertGives { content }
		evaluate_ { point { x { zero }; y { one } }.content }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; content }.assertGives { content { x { zero }; y { one } } }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertGives { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertGives { one }
		evaluate_ { any.is_ { one }.zero }.assertGives { one }

		evaluate_ { any.text.is_ { ok }; "foo".text }.assertGives { ok }
		evaluate_ { any.number.is_ { ok }; 123.number }.assertGives { ok }
	}

	@Test
	fun gives() {
		evaluate_ { zero.gives { one } }.assertGives { nothing_ }
		evaluate_ { zero.gives { one }.zero }.assertGives { one }
		evaluate_ { zero.gives { one }.one }.assertGives { one }
		evaluate_ { any.x.gives { x }.zero.x }.assertGives { x { zero } }
		evaluate_ { any.x.gives { x }.zero.y }.assertGives { y { zero } }

		evaluate_ { any.text.gives { text }; "foo".text }.assertGives { "foo".text }
		evaluate_ { any.number.gives { number }; 123.number }.assertGives { 123.number }
	}

	@Test
	fun anyGives() {
		evaluate_ {
			any.plus { any }
			gives { the.content.add { plus.content } }
			two.plus { three }
		}.assertGives {
			two.add { three }
		}
	}

	@Test
	fun matchSentence() {
		evaluate_ { zero.bit.match { zero { one } } }
			.assertGives { one }

		evaluate_ { zero.bit.match { nothing_ } }
			.assertGives { match { bit { zero } } }
		evaluate_ { zero.bit.match { one } }
			.assertGives { bit { zero }.match { one } }
	}

	@Test
	fun matchEmpty() {
		evaluate_ { empty.list.match { empty { ok } } }
			.assertGives { ok }
	}

	@Test
	fun matchLinked() {
		evaluate_ {
			list {
				item { zero }
				item { one }
				item { two }
			}
			match { link { link } }
		}.assertGives {
			link {
				previous {
					list {
						item { zero }
						item { one }
					}
				}
				last { two }
			}
		}
	}

	@Test
	fun giving() {
		evaluate_ { zero.giving { given } }.assertGives { taking { zero } }
	}

	@Test
	fun take() {
		evaluate_ { the { any.x.giving { x } }.take { x { zero } } }.assertGives { x { zero } }
		evaluate_ { the { any.x.giving { x } }.take { y { zero } } }.assertGives { the { taking { x { any } } }.take { y { zero } } }
	}

	@Test
	fun give() {
		evaluate_ {
			x { zero }
			y { one }
			give { x.and { y } }
		}.assertGives { x { zero }.and { y { one } } }
	}

	@Test
	fun giveRepeating_noRepeat() {
		evaluate_ {
			10.number
			give {
				repeating {
					number
				}
			}
		}.assertGives { 10.number }
	}

	@Test
	fun giveRepeating_repeating() {
		evaluate_ {
			import { number }
			10.number
			give {
				repeating {
					number
					equals_ { 0.number }
					match {
						true_ { number }
						false_ { number.minus { 1.number }.repeat }
					}
				}
			}
		}.assertGives { 0.number }
	}

	@Test
	fun giveRecursing_noRecurse() {
		evaluate_ {
			10.number
			give {
				recursing {
					number
				}
			}
		}.assertGives { 10.number }
	}

	@Test
	fun giveRecursing_recurse() {
		evaluate_ {
			import { number }
			10.number
			give {
				recursing {
					number
					equals_ { 0.number }
					match {
						true_ { number }
						false_ { number.minus { 1.number }.recurse }
					}
				}
			}
		}.assertGives { 0.number }
	}

	@Test
	fun expands() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
		}.assertGives { nothing_ }
	}

	@Test
	fun expandsApply() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
		}.assertGives { nothing_ }
	}

	@Test
	fun expandsApplyUse() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
			zero
		}.assertGives { one }
	}

	@Test
	fun expandsLocals() {
		evaluate_ {
			expand.expands {
				zero.is_ { one }
				zero
			}
			expand
		}.assertGives { one }
	}

	@Test
	fun dictionary() {
		evaluate_ {
			dictionary { nothing_ }
		}.assertGives {
			dictionary {
				definition { list { empty } }
			}
		}

		evaluate_ {
			dictionary {
				zero.is_ { one }
			}
		}.assertGives {
			dictionary {
				definition {
					list {
						item {
							definition {
								zero.is_ { one }
							}
						}
					}
				}
			}
		}
		evaluate_ {
			dictionary {
				zero.is_ { one }
				one.is_ { zero }
			}
		}.assertGives {
			dictionary {
				definition {
					list {
						item { definition { zero.is_ { one } } }
						item { definition { one.is_ { one } } }
					}
				}
			}
		}
	}

	@Test
	fun load() {
		evaluate_ { ping.testing.load }.assertGives { pong }
	}

	@Test
	fun dictionaryInsideDictionary() {
		evaluate_ {
			dictionary {
				dictionary {
					zero.is_ { one }
				}.import

				two.is_ { zero }
			}.import.two
		}.assertGives { one }
	}

	@Test
	fun import() {
		evaluate_ { dictionary { nothing_ }.import }.assertGives { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import }.assertGives { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import.zero }.assertGives { one }

		evaluate_ { zero.import { dictionary { zero.is_ { one } } } }.assertGives { zero }
		evaluate_ { zero.import { dictionary { zero.is_ { one } } }.evaluate }.assertGives { one }

		evaluate_ {
			zero.is_ { one }
			import {
				dictionary {
					two.is_ { zero }
				}
			}
			two
		}.assertGives { one }
	}

	@Test
	fun export() {
		evaluate_ {
			import {
				dictionary {
					export {
						dictionary {
							zero.is_ { one }
						}
					}
				}
			}
			zero
		}.assertGives { one }

		evaluate_ {
			import {
				dictionary {
					export {
						dictionary {
							zero.is_ { one }
						}
					}
					two.is_ { zero }
				}
			}
			two
		}.assertGives { zero }

		evaluate_ {
			import {
				dictionary {
					import {
						dictionary {
							zero.is_ { one }
						}
					}
				}
			}
			zero
		}.assertGives { zero }
	}

	@Test
	fun testTest() {
		evaluate_ {
			test { zero.gives { zero } }
		}.assertGives { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { one.gives { result } }
		}.assertGives { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { result.gives { one } }
		}.assertGives { nothing_ }

		assertFailsWith(AssertionError::class) {
			evaluate_ { test { zero.gives { one } } }
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { zero.gives { result } }
			}
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { result.gives { zero } }
			}
		}
	}

	@Test
	fun matchesSentence() {
		evaluate_ { zero.matches { zero } }.assertGives { true.boolean }
		evaluate_ { zero.matches { one } }.assertGives { false.boolean }
	}

	@Test
	fun matchesChoice() {
		evaluate_ {
			zero
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertGives { true.boolean }

		evaluate_ {
			one
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertGives { true.boolean }

		evaluate_ {
			two
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertGives { false.boolean }

		evaluate_ {
			choice {
				case { zero }
				case { one }
			}
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertGives { true.boolean }

		evaluate_ {
			choice {
				case { zero }
				case { one }
			}
			matches {
				quote {
					choice {
						case { zero }
						case { one }
					}
				}
			}
		}.assertGives { false.boolean }
	}

	@Test
	fun matchesDictionary() {
		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary }
		}.assertGives { false.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { quote { dictionary } }
		}.assertGives { true.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary { zero.is_ { one } } }
		}.assertGives { true.boolean }
	}

	@Test
	fun matchesGiving() {
		evaluate_ {
			zero.giving { given }
			matches { taking { zero } }
		}.assertGives { true.boolean }

		evaluate_ {
			zero.giving { given }
			matches { taking { one } }
		}.assertGives { false.boolean }
	}

//	@Test
//	fun debug() {
//		evaluate_ {
//			zero.is_ { one }.debug
//		}.assertGives {
//			evaluator {
//				parent { nothing }
//				evaluated {
//					scope {
//						dictionary {
//							definition {
//								list {
//									definition {
//										pattern { zero }
//										body { one }
//									}
//								}
//							}
//						}
//						export {
//							dictionary {
//								definition {
//									list {
//										definition {
//											pattern { zero }
//											body { one }
//										}
//									}
//								}
//							}
//						}
//					}
//					value { nothing_ }
//				}
//				mode { evaluate }
//			}
//		}
//	}
}