package leo16

import leo.base.assertEqualTo
import leo14.Script
import leo15.dsl.*
import leo16.names.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

fun Script.assertEquals(f: F) {
	assertEqualTo(read_(f))
}

class EvalTest {
	@Test
	fun normalization() {
		evaluate_ { zero.negate }.assertEquals { negate { zero } }
	}

	@Test
	fun comment() {
		evaluate_ { comment { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { comment { zero } }.assertEquals { nothing_ }
		evaluate_ {
			comment { two { dimensional { point } } }
			point {
				comment { x.coordinate }
				x { 10.number }
				comment { y.coordinate }
				y { 20.number }
			}
		}.assertEquals {
			point {
				x { 10.number }
				y { 20.number }
			}
		}
	}

	@Test
	fun nothing() {
		evaluate_ { nothing }.assertEquals { nothing_ }
		evaluate_ { x { nothing } }.assertEquals { x { nothing_ } }
		evaluate_ { x.nothing }.assertEquals { nothing { x } }
		evaluate_ { x.nothing { y } }.assertEquals { x.nothing { y } }
	}

	@Test
	fun sentences() {
		evaluate_ { nothing_ }.assertEquals { nothing_ }
		evaluate_ { zero }.assertEquals { zero }
		evaluate_ { zero.plus { one } }.assertEquals { zero.plus { one } }
	}

	@Test
	fun literal() {
		evaluate_ { "foo".text }.assertEquals { "foo".text }
		evaluate_ { 123.number }.assertEquals { 123.number }
	}

	@Test
	fun quote() {
		evaluate_ { quote { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate } }.assertEquals { zero.negate }
		evaluate_ { quote { zero.is_ { one } } }.assertEquals { zero.is_ { one } }
		evaluate_ { zero.quote { one.two } }.assertEquals { zero.one.two }
		evaluate_ { zero.one.quote { two } }.assertEquals { one { zero }.two }
	}

	@Test
	fun word() {
		evaluate_ { word { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { word { zero } }.assertEquals { zero }
		evaluate_ { word { zero.one } }.assertEquals { zero.one }
		evaluate_ { word { zero.is_ { one } } }.assertEquals { zero.is_ { one } }
		evaluate_ { zero.word { is_ { one } } }.assertEquals { zero.is_ { one } }
	}

	@Test
	fun script() {
		evaluate_ {
			function { zero.does { one } }
			script
			take { zero }
		}.assertEquals {
			function { zero.does { one } }
			take { zero }
		}
	}

	@Test
	fun leonardo() {
		evaluate_ { leonardo.author.text }.assertEquals { "Michał Pociecha-Łoś".text }
	}

	@Test
	fun this_() {
		evaluate_ { this_ { nothing_ } }.assertEquals { nothing_ }
		evaluate_ { x { zero }.this_ { nothing_ } }.assertEquals { x { zero } }
		evaluate_ { x { zero }.this_ { y { one } } }.assertEquals { x { zero }; y { one } }
		evaluate_ { x { zero }.this_ { y { one }; z { two } } }.assertEquals { x { zero }; y { one }; z { two } }
		evaluate_ { this_ { zero }; this_ { one } }.assertEquals { zero; one }
	}

	@Test
	fun equals_() {
		evaluate_ { zero.equals_ { zero } }.assertEquals { boolean { true_ } }
		evaluate_ { zero.equals_ { one } }.assertEquals { boolean { false_ } }
		evaluate_ { function { does { zero } }.equals_ { function { does { zero } } } }.assertEquals { boolean { true_ } }
		evaluate_ { function { does { zero } }.equals_ { function { does { one } } } }.assertEquals { boolean { false_ } }
	}

	@Test
	fun evaluateBegin() {
		evaluate_ {
			is_ { new { line } }
			zero
		}.assertEquals { zero { new { line } } }

		evaluate_ {
			is_ { new { line } }
			x { zero }
			y { one }
		}.assertEquals {
			x {
				new { line }
				zero { new { line } }
			}
			y {
				new { line }
				one { new { line } }
			}
		}
	}

	@Test
	fun hash() {
		evaluate_ { zero.hash }.assertEquals { hash { value(_zero()).hashBigDecimal.number } }
	}

	@Test
	fun asText() {
		evaluate_ { 123.number.as_ { text } }.assertEquals { "123".text }
	}

	@Test
	fun lazy() {
		evaluate_ {
			zero.is_ { one }
			lazy_ { zero }
		}.assertEquals { lazy_ { zero } }
	}

	@Test
	fun force() {
		evaluate_ {
			zero.is_ { one }
			lazy_ { zero }
			force
		}.assertEquals { force { one } }
	}

	@Test
	fun apply() {
		evaluate_ {
			x.is_ { zero }
			quote { x }
			apply
		}.assertEquals { zero }

		evaluate_ {
			x.is_ { zero }
			quote { the { x } }
			apply
		}.assertEquals { apply { the { x } } }
	}

	@Test
	fun evaluate() {
		evaluate_ { quote { nothing_ }.evaluate }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate }.evaluate }.assertEquals { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.evaluate }.assertEquals { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.evaluate }.assertEquals { one }
		evaluate_ { quote { zero.is_ { one } }.evaluate.zero }.assertEquals { zero }
	}

	@Test
	fun compile() {
		evaluate_ { quote { nothing_ }.compile }.assertEquals { nothing_ }
		evaluate_ { quote { zero.negate }.compile }.assertEquals { negate { zero } }
		evaluate_ { quote { zero.is_ { one } }.compile }.assertEquals { nothing_ }
		evaluate_ { quote { zero.is_ { one }.zero }.compile }.assertEquals { one }
		evaluate_ { quote { zero.is_ { one } }.compile.zero }.assertEquals { one }
	}

	@Test
	fun getField() {
		evaluate_ { point { x { zero }; y { one } }.x }.assertEquals { x { zero } }
		evaluate_ { point { x { zero }; y { one } }.y }.assertEquals { y { one } }
	}

	@Test
	fun getSpecial() {
		evaluate_ { the { "foo".text }.text }.assertEquals { "foo".text }
		evaluate_ { the { 123.number }.number }.assertEquals { 123.number }
		evaluate_ { the { dictionary { nothing_ } }.dictionary }.assertEquals { dictionary { definition { list { empty } } } }
		evaluate_ { the { function { zero.does { one } } }.function }.assertEquals { function { zero.does { one } } }
	}

	@Test
	fun content() {
		evaluate_ { content }.assertEquals { content }
		evaluate_ { point { x { zero }; y { one } }.content }.assertEquals { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; content }.assertEquals { content { x { zero }; y { one } } }
	}

	@Test
	fun functionFunction() {
		evaluate_ {
			function { function { zero }.does { ok } }
			take { function { zero.does { one } } }
		}.assertEquals { ok }
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
		}.assertEquals { one }
	}

	@Test
	fun is_() {
		evaluate_ { zero.is_ { one } }.assertEquals { nothing_ }
		evaluate_ { zero.is_ { one }.zero }.assertEquals { one }
		evaluate_ { any.is_ { one }.zero }.assertEquals { one }

		evaluate_ { any.text.is_ { ok }; "foo".text }.assertEquals { ok }
		evaluate_ { any.number.is_ { ok }; 123.number }.assertEquals { ok }
	}

	@Test
	fun has() {
		evaluate_ { zero.has { one } }.assertEquals { nothing_ }
		evaluate_ { zero.has { one }.zero }.assertEquals { zero { one } }
		evaluate_ { any.has { one }.zero }.assertEquals { any { one } }

		evaluate_ { any.text.has { ok }; "foo".text }.assertEquals { text { ok } }
		evaluate_ { any.number.has { ok }; 123.number }.assertEquals { number { ok } }
	}

	@Test
	fun does() {
		evaluate_ { zero.does { one } }.assertEquals { nothing_ }
		evaluate_ { zero.does { one }.zero }.assertEquals { one }
		evaluate_ { zero.does { one }.one }.assertEquals { one }
		evaluate_ { any.x.does { x }.zero.x }.assertEquals { x { zero } }
		evaluate_ { any.x.does { x }.zero.y }.assertEquals { y { zero } }

		evaluate_ { any.text.does { text }; "foo".text }.assertEquals { "foo".text }
		evaluate_ { any.number.does { number }; 123.number }.assertEquals { 123.number }
	}

	@Test
	fun anyDoes() {
		evaluate_ {
			any.plus { any }
			does { the.content.add { plus.content } }
			two.plus { three }
		}.assertEquals {
			two.add { three }
		}
	}

	@Test
	fun matchSentence() {
		evaluate_ { zero.bit.match { zero { one } } }
			.assertEquals { one }

		evaluate_ { zero.bit.match { nothing_ } }
			.assertEquals { match { bit { zero } } }
		evaluate_ { zero.bit.match { one } }
			.assertEquals { bit { zero }.match { one } }
	}

	@Test
	fun matchEmpty() {
		evaluate_ { empty.list.match { empty { ok } } }
			.assertEquals { ok }
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
		}.assertEquals {
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
		}.assertEquals {
			function { zero.does { one } }
		}

		evaluate_ {
			function { zero.does { one } }
			zero
		}.assertEquals {
			zero { function { zero.does { one } } }
		}

		evaluate_ {
			function { zero.plus { one } }
			zero
		}.assertEquals { zero }
	}

	@Test
	fun take() {
		evaluate_ {
			function { zero.does { one } }
			take { zero }
		}.assertEquals { one }

		evaluate_ {
			function { any.bit.does { ok } }
			take { zero.bit }
		}.assertEquals { ok }

		evaluate_ {
			function { any.x.does { x } }
			take { y { zero } }
		}.assertEquals {
			function { x { any }.does { x } }
			take { y { zero } }
		}

		evaluate_ {
			function { does { zero } }.take
		}.assertEquals { zero }
	}

	@Test
	fun do_() {
		evaluate_ {
			x { zero }
			y { one }
			do_ { x.and { y } }
		}.assertEquals { x { zero }.and { y { one } } }
	}

	@Test
	fun doContent() {
		evaluate_ {
			x { zero }
			y { one }
			do_ { content }
		}.assertEquals {
			x { zero }
			y { one }
		}
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
		}.assertEquals { bit { zero } }
	}

	@Test
	fun expands() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
		}.assertEquals { nothing_ }
	}

	@Test
	fun expandsApply() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
		}.assertEquals { nothing_ }
	}

	@Test
	fun expandsApplyUse() {
		evaluate_ {
			expand.expands {
				quote { zero.is_ { one } }
			}
			expand
			zero
		}.assertEquals { one }
	}

	@Test
	fun expandsLocals() {
		evaluate_ {
			expand.expands {
				zero.is_ { one }
				zero
			}
			expand
		}.assertEquals { one }
	}

	@Test
	fun dictionary() {
		evaluate_ {
			dictionary { nothing_ }
		}.assertEquals {
			dictionary {
				definition { list { empty } }
			}
		}

		evaluate_ {
			dictionary {
				zero.is_ { one }
			}
		}.assertEquals {
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
		}.assertEquals {
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
	fun library() {
		evaluate_ { ping.testing.library }.assertEquals { pong }
	}

	@Test
	fun emptyLibrary() {
		evaluate_ {
			x.is_ { zero }
			empty.library
			x
		}.assertEquals { x }
	}

	@Test
	fun unknownLibrary() {
		evaluate_ {
			// Pick some library which does not exist.
			unknown.library
		}.assertEquals { library { unknown } }
	}

	@Test
	fun useNothing() {
		evaluate_ {
			zero
			use { nothing }
		}.assertEquals { zero }
	}

	@Test
	fun mode() {
		evaluate_ {
			word { word { zero } }.mode
			is_ { word { quote }.mode }
			zero { zero.is_ { one } }
		}.assertEquals { zero { zero.is_ { one } } }
	}

	@Test
	fun nativePatternMatch() {
		evaluate_ {
			"hello".text
			is_ { "world".text }
			"hello".text
		}.assertEquals { "world".text }
	}

	@Test
	fun useSomething() {
		evaluate_ {
			zero
			use { x.is_ { one } }
			plus { x }
		}.assertEquals { zero.plus { one } }
	}

	@Test
	fun useCapturesScope() {
		evaluate_ {
			x.is_ { zero }
			x
			use { y.is_ { x.done } }
			and { y }
		}.assertEquals { zero.and { done { zero } } }
	}

	@Test
	fun useInvalid() {
		evaluate_ {
			use { x }
		}.assertEquals { use { x } }
	}

	@Test
	fun useDoesNotExport() {
		evaluate_ {
			use {
				use {
					x.is_ { zero }
				}
			}
			x
		}.assertEquals { x }
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
		}.assertEquals { one }
	}

	@Test
	fun import() {
		evaluate_ { dictionary { nothing_ }.import }.assertEquals { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import }.assertEquals { nothing_ }
		evaluate_ { dictionary { zero.is_ { one } }.import.zero }.assertEquals { one }

		evaluate_ { zero.import { dictionary { zero.is_ { one } } } }.assertEquals { zero }
		evaluate_ { zero.import { dictionary { zero.is_ { one } } }.evaluate }.assertEquals { one }

		evaluate_ {
			zero.is_ { one }
			import {
				dictionary {
					two.is_ { zero }
				}
			}
			two
		}.assertEquals { one }
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
		}.assertEquals { one }

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
		}.assertEquals { zero }

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
		}.assertEquals { zero }
	}

	@Test
	fun testTest() {
		evaluate_ {
			test { zero.equals_ { zero } }
		}.assertEquals { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { one.equals_ { result } }
		}.assertEquals { nothing_ }

		evaluate_ {
			result.is_ { one }
			test { result.equals_ { one } }
		}.assertEquals { nothing_ }

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
		evaluate_ { zero.matches { zero } }.assertEquals { true.boolean }
		evaluate_ { zero.matches { one } }.assertEquals { false.boolean }
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
		}.assertEquals { true.boolean }

		evaluate_ {
			one
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertEquals { true.boolean }

		evaluate_ {
			two
			matches {
				choice {
					case { zero }
					case { one }
				}
			}
		}.assertEquals { false.boolean }

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
		}.assertEquals { true.boolean }

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
		}.assertEquals { false.boolean }
	}

	@Test
	fun matchesDictionary() {
		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary }
		}.assertEquals { false.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { quote { dictionary } }
		}.assertEquals { true.boolean }

		evaluate_ {
			dictionary { zero.is_ { one } }
			matches { dictionary { zero.is_ { one } } }
		}.assertEquals { true.boolean }
	}

	@Test
	fun matchesGiving() {
		evaluate_ {
			function { zero.does { one } }
			matches { function { zero } }
		}.assertEquals { true.boolean }

		evaluate_ {
			function { zero.does { one } }
			matches { function { one } }
		}.assertEquals { false.boolean }
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