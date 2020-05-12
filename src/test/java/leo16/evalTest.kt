package leo16

import leo.base.assertEqualTo
import leo14.Script
import leo15.dsl.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

fun Script.assertDoes(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertDoes { negate { zero } }
	}

	@Test
	fun comment() {
		evaluate_ { comment { nothing_ } }.assertDoes { nothing_ }
		evaluate_ { comment { zero } }.assertDoes { nothing_ }
		evaluate_ {
			comment { two { dimensional { point } } }
			point {
				comment { x.coordinate }
				x { 10.number }
				comment { y.coordinate }
				y { 20.number }
			}
		}.assertDoes {
			point {
				x { 10.number }
				y { 20.number }
			}
		}
	}

	@Test
	fun nothing() {
		evaluate_ { nothing }.assertDoes { nothing_ }
		evaluate_ { x { nothing } }.assertDoes { x { nothing_ } }
		evaluate_ { x.nothing }.assertDoes { nothing { x } }
		evaluate_ { x.nothing { y } }.assertDoes { x.nothing { y } }
	}

	@Test
	fun sentences() {
		evaluate_ { nothing_ }.assertDoes { nothing_ }
		evaluate_ { zero }.assertDoes { zero }
		evaluate_ { zero.plus { one } }.assertDoes { zero.plus { one } }
	}

	@Test
	fun literal() {
		evaluate_ { "foo".text }.assertDoes { "foo".text }
		evaluate_ { 123.number }.assertDoes { 123.number }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertDoes { nothing_ }
		evaluate_ { quote { zero.negate } }.assertDoes { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertDoes { zero.is_ { one } }
		evaluate_ { zero.quote { one.two } }.assertDoes { zero.one.two }
		evaluate_ { zero.one.quote { two } }.assertDoes { one { zero }.two }
	}

	@Test
	fun script() {
		evaluate_ {
			function { zero.does { one } }
			script
			take { zero }
		}.assertDoes {
			function { zero.does { one } }
			take { zero }
		}
	}

	@Test
	fun leonardo() {
		evaluate_ { leonardo.author.text }.assertDoes { "Michał Pociecha-Łoś".text }
	}

	@Test
	fun this_() {
		evaluate_ { this_ { nothing_ } }.assertDoes { nothing_ }
		evaluate_ { x { zero }.this_ { nothing_ } }.assertDoes { x { zero } }
		evaluate_ { x { zero }.this_ { y { one } } }.assertDoes { x { zero }; y { one } }
		evaluate_ { x { zero }.this_ { y { one }; z { two } } }.assertDoes { x { zero }; y { one }; z { two } }
		evaluate_ { this_ { zero }; this_ { one } }.assertDoes { zero; one }
	}

	@Test
	fun evaluate() {
		evaluate_ { quote { nothing_ }.evaluate }.assertDoes { nothing_ }
		evaluate_ { quote { zero.negate }.evaluate }.assertDoes { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.evaluate }.assertDoes { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.evaluate }.assertDoes { one }
		evaluate_ { quote { zero.is_ { one } }.evaluate.zero }.assertDoes { zero }
	}

	@Test
	fun compile() {
		evaluate_ { quote { nothing_ }.compile }.assertDoes { nothing_ }
		evaluate_ { quote { zero.negate }.compile }.assertDoes { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.compile }.assertDoes { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.compile }.assertDoes { one }
		evaluate_ { quote { zero.is_ { one } }.compile.zero }.assertDoes { one }
	}

	@Test
	fun getField() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertDoes { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertDoes { y { one } }
	}

	@Test
	fun getSpecial() {
		evaluate_ { the { "foo".text }.text }.assertDoes { "foo".text }
		evaluate_ { the { 123.number }.number }.assertDoes { 123.number }
		evaluate_ { the { dictionary { nothing_ } }.dictionary }.assertDoes { dictionary { definition { list { empty } } } }
		evaluate_ { the { function { zero.does { one } } }.function }.assertDoes { function { zero.does { one } } }
	}

	@Test
	fun content() {
		evaluate_ { content }.assertDoes { content }
		evaluate_ { point { x { zero }; y { one } }.content }.assertDoes { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; content }.assertDoes { content { x { zero }; y { one } } }
	}

	@Test
	fun functionFunction() {
		evaluate_ {
			function { function { zero }.does { ok } }
			take { function { zero.does { one } } }
		}.assertDoes { ok }
	}

	@Test
	fun functionAsField() {
		evaluate_ {
			the {
				0.number
				"hello".text
				function { zero.does { one } }
			}
			function.take { zero }
		}.assertDoes { one }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertDoes { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertDoes { one }
		evaluate_ { any.is_ { one }.zero }.assertDoes { one }

		evaluate_ { any.text.is_ { ok }; "foo".text }.assertDoes { ok }
		evaluate_ { any.number.is_ { ok }; 123.number }.assertDoes { ok }
	}

	@Test
	fun does() {
		evaluate_ { zero.does { one } }.assertDoes { nothing_ }
		evaluate_ { zero.does { one }.zero }.assertDoes { one }
		evaluate_ { zero.does { one }.one }.assertDoes { one }
		evaluate_ { any.x.does { x }.zero.x }.assertDoes { x { zero } }
		evaluate_ { any.x.does { x }.zero.y }.assertDoes { y { zero } }

		evaluate_ { any.text.does { text }; "foo".text }.assertDoes { "foo".text }
		evaluate_ { any.number.does { number }; 123.number }.assertDoes { 123.number }
	}

	@Test
	fun anyDoes() {
		evaluate_ {
			any.plus { any }
			does { the.content.add { plus.content } }
			two.plus { three }
		}.assertDoes {
			two.add { three }
		}
	}

	@Test
	fun matchSentence() {
		evaluate_ { zero.bit.match { zero { one } } }
			.assertDoes { one }

		evaluate_ { zero.bit.match { nothing_ } }
			.assertDoes { match { bit { zero } } }
		evaluate_ { zero.bit.match { one } }
			.assertDoes { bit { zero }.match { one } }
	}

	@Test
	fun matchEmpty() {
		evaluate_ { empty.list.match { empty { ok } } }
			.assertDoes { ok }
	}

	@Test
	fun matchLinked() {
		evaluate_ {
			list {
				item { zero }
				item { one }
				item { two }
			}
			match { linked { linked } }
		}.assertDoes {
			linked {
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
	fun function() {
		evaluate_ {
			function { zero.does { one } }
		}.assertDoes {
			function { zero.does { one } }
		}

		evaluate_ {
			function { zero.does { one } }
			zero
		}.assertDoes {
			zero { function { zero.does { one } } }
		}

		evaluate_ {
			function { zero.plus { one } }
			zero
		}.assertDoes { zero }
	}

	@Test
	fun take() {
		evaluate_ {
			function { zero.does { one } }
			take { zero }
		}.assertDoes { one }

		evaluate_ {
			function { any.bit.does { ok } }
			take { zero.bit }
		}.assertDoes { ok }

		evaluate_ {
			function { any.x.does { x } }
			take { y { zero } }
		}.assertDoes {
			function { x { any }.does { x } }
			take { y { zero } }
		}

		evaluate_ {
			function { does { zero } }.take
		}.assertDoes { zero }
	}

	@Test
	fun do_() {
		evaluate_ {
			x { zero }
			y { one }
			do_ { x.and { y } }
		}.assertDoes { x { zero }.and { y { one } } }
	}

	@Test
	fun doRepeat() {
		evaluate_ {
			one.bit
			do_ {
				bit.match {
					zero { bit }
					one { zero.bit.repeat }
				}
			}
		}.assertDoes { bit { zero } }
	}

	@Test
	fun expands() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
		}.assertDoes { nothing_ }
	}

	@Test
	fun expandsApply() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
		}.assertDoes { nothing_ }
	}

	@Test
	fun expandsApplyUse() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
			zero
		}.assertDoes { one }
	}

	@Test
	fun expandsLocals() {
		evaluate_ {
			expand.expands {
				zero.is_ { one }
				zero
			}
			expand
		}.assertDoes { one }
	}

	@Test
	fun dictionary() {
		evaluate_ {
			dictionary { nothing_ }
		}.assertDoes {
			dictionary {
				definition { list { empty } }
			}
		}

		evaluate_ {
			dictionary {
				zero.is_ { one }
			}
		}.assertDoes {
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
		}.assertDoes {
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
		evaluate_ { ping.testing.load }.assertDoes { pong }
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
		}.assertDoes { one }
	}

	@Test
	fun import() {
		evaluate_ { dictionary { nothing_ }.import }.assertDoes { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import }.assertDoes { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import.zero }.assertDoes { one }

		evaluate_ { zero.import { dictionary { zero.is_ { one } } } }.assertDoes { zero }
		evaluate_ { zero.import { dictionary { zero.is_ { one } } }.evaluate }.assertDoes { one }

		evaluate_ {
			zero.is_ { one }
			import {
				dictionary {
					two.is_ { zero }
				}
			}
			two
		}.assertDoes { one }
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
		}.assertDoes { one }

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
		}.assertDoes { zero }

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
		}.assertDoes { zero }
	}

	@Test
	fun testTest() {
		evaluate_ {
			test { zero.equals_ { zero } }
		}.assertDoes { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { one.equals_ { result } }
		}.assertDoes { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { result.equals_ { one } }
		}.assertDoes { nothing_ }

		assertFailsWith(AssertionError::class) {
			evaluate_ { test { zero.equals_ { one } } }
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { zero.equals_ { result } }
			}
		}

		assertFailsWith(AssertionError::class) {
			evaluate_ {
				result.is_ { one }
				test { result.equals_ { zero } }
			}
		}
	}

	@Test
	fun matchesSentence() {
		evaluate_ { zero.matches { zero } }.assertDoes { true.boolean }
		evaluate_ { zero.matches { one } }.assertDoes { false.boolean }
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
		}.assertDoes { true.boolean }

		evaluate_ {
			one
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertDoes { true.boolean }

		evaluate_ {
			two
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertDoes { false.boolean }

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
		}.assertDoes { true.boolean }

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
		}.assertDoes { false.boolean }
	}

	@Test
	fun matchesDictionary() {
		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary }
		}.assertDoes { false.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { quote { dictionary } }
		}.assertDoes { true.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary { zero.is_ { one } } }
		}.assertDoes { true.boolean }
	}

	@Test
	fun matchesGiving() {
		evaluate_ {
			function { zero.does { one } }
			matches { function { zero } }
		}.assertDoes { true.boolean }

		evaluate_ {
			function { zero.does { one } }
			matches { function { one } }
		}.assertDoes { false.boolean }
	}

//	@Test
//	fun debug() {
//		evaluate_ {
//			zero.is_ { one }.debug
//		}.assertDoes {
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