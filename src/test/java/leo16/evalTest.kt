package leo16

import leo.base.assertEqualTo
import leo14.Script
import leo15.dsl.*
import java.awt.Point
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
		evaluate_ { the { dictionary { nothing_ } }.dictionary }.assertGives { dictionary { pattern { list } } }
		evaluate_ { the { giving { given } }.giving }.assertGives { giving { given } }
	}

	@Test
	fun thing() {
		evaluate_ { thing }.assertGives { thing }
		evaluate_ { point { x { zero }; y { one } }.thing }.assertGives { x { zero }; y { one } }
		evaluate_ { x { zero }; y { one }; thing }.assertGives { thing { x { zero }; y { one } } }
	}

	@Test
	fun texts() {
		evaluate_ { "Hello".text }.assertGives { "Hello".text }
		evaluate_ { "Hello".text.native }.assertGives { "Hello".native_ }
		evaluate_ { "Hello, ".text.plus { "world!".text } }.assertGives { "Hello, world!".text }
		evaluate_ { "Hello, world!".text.length }.assertGives { 13.number }
	}

	@Test
	fun numbers() {
		evaluate_ { 123.number }.assertGives { 123.number }
		evaluate_ { 123.number.native }.assertGives { 123.native_ }
		evaluate_ { 2.number.plus { 3.number } }.assertGives { 5.number }
		evaluate_ { 5.number.minus { 3.number } }.assertGives { 2.number }
		evaluate_ { 2.number.times { 3.number } }.assertGives { 6.number }
	}

	@Test
	fun ints() {
		evaluate_ { 123.int }.assertGives { 123.int }
		evaluate_ { 123.int.native }.assertGives { 123.native_ }
		evaluate_ { 2.int.plus { 3.int } }.assertGives { 5.int }
		evaluate_ { 5.int.minus { 3.int } }.assertGives { 2.int }
		evaluate_ { 2.int.times { 3.int } }.assertGives { 6.int }
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
		evaluate_ { zero.gives { given }.zero }.assertGives { given { zero } }
		evaluate_ { zero.gives { given }.one }.assertGives { one }

		evaluate_ { any.text.gives { given }; "foo".text }.assertGives { given { "foo".text } }
		evaluate_ { any.number.gives { given }; 123.number }.assertGives { given { 123.number } }
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
	fun dictionary() {
		evaluate_ {
			dictionary { nothing_ }
		}.assertGives { dictionary { pattern { list } } }

		evaluate_ {
			dictionary {
				zero.is_ { one }
			}
		}.assertGives {
			dictionary {
				pattern {
					list {
						pattern { zero }
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
				pattern {
					list {
						pattern { zero }
						pattern { one }
					}
				}
			}
		}
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
	fun load() {
		evaluate_ { ping.load }.assertGives { pong }
		evaluate_ { "foo".text.load }.assertGives { load { "foo".text } }
	}

	@Test
	fun natives() {
		evaluate_ { null_.native }.assertGives { null.native_ }
		evaluate_ { true_.native }.assertGives { true.native_ }
		evaluate_ { false_.native }.assertGives { false.native_ }
		evaluate_ { 10.toByte().byte.native }.assertGives { 10.toByte().native_ }
		evaluate_ { 10.int.native }.assertGives { 10.native_ }
		evaluate_ { 10L.long.native }.assertGives { 10L.native_ }
		evaluate_ { 10f.float.native }.assertGives { 10f.native_ }
		evaluate_ { 10.0.double.native }.assertGives { 10.0.native_ }
	}

	@Test
	fun typeNativeClass() {
		evaluate_ {
			int.native.class_
		}.assertGives {
			class_ { Integer.TYPE.native_ }
		}
	}

	@Test
	fun textNameNativeClass() {
		evaluate_ {
			"java.lang.Integer".text.name.native.class_
		}.assertGives {
			class_ { Integer::class.java.native_ }
		}
	}

	@Test
	fun nativeClassField() {
		evaluate_ {
			"java.lang.Integer".text.name.native.class_
			field { name { "MAX_VALUE".text } }
		}.assertGives {
			field { Integer::class.java.getField("MAX_VALUE").native_ }
		}
	}

	@Test
	fun nativeFieldGet() {
		evaluate_ {
			"java.lang.Integer".text.name.native.class_
			field { name { "MAX_VALUE".text } }
			get
		}.assertGives {
			Integer.MAX_VALUE.native_
		}
	}

	@Test
	fun nativeObjectFieldGet() {
		evaluate_ {
			"java.awt.Point".text.name.native.class_
			constructor { parameter { list } }
			invoke { parameter { list } }
			get {
				"java.awt.Point".text.name.native.class_
				field { name { "x".text } }
			}
		}.assertGives { 0.native_ }
	}

	@Test
	fun nativeClassConstructor() {
		evaluate_ {
			"java.awt.Point".text.name.native.class_
			constructor {
				parameter {
					list {
						this_ { int.native.class_ }
						this_ { int.native.class_ }
					}
				}
			}
		}.assertGives {
			constructor {
				Point::class.java.getConstructor(Integer.TYPE, Integer.TYPE).native_
			}
		}
	}

	@Test
	fun nativeConstructorInvoke() {
		evaluate_ {
			"java.awt.Point".text.name.native.class_
			constructor {
				parameter {
					list {
						this_ { int.native.class_ }
						this_ { int.native.class_ }
					}
				}
			}
			invoke {
				parameter {
					list {
						this_ { 10.number.int.native }
						this_ { 20.number.int.native }
					}
				}
			}
		}.assertGives {
			Point(10, 20).native_
		}
	}

	@Test
	fun nativeClassMethod() {
		evaluate_ {
			"java.lang.String".text.name.native.class_
			method {
				name { "substring".text }
				parameter {
					list {
						this_ { int.native.class_ }
						this_ { int.native.class_ }
					}
				}
			}
		}.assertGives {
			method {
				String::class.java.getMethod("substring", Integer.TYPE, Integer.TYPE).native_
			}
		}
	}

	@Test
	fun nativeMethodInvoke() {
		evaluate_ {
			"Hello, world!".text.native
			invoke {
				"java.lang.String".text.name.native.class_
				method {
					name { "substring".text }
					parameter {
						list {
							this_ { int.native.class_ }
							this_ { int.native.class_ }
						}
					}
				}
				parameter {
					list {
						this_ { 7.number.int.native }
						this_ { 12.number.int.native }
					}
				}
			}
		}.assertGives { "world".native_ }
	}

	@Test
	fun nativeObjectMethodInvoke() {
		evaluate_ {
			"java.lang.String".text.name.native.class_
			method {
				name { "valueOf".text }
				parameter {
					list {
						this_ { int.native.class_ }
					}
				}
			}
			invoke {
				parameter {
					list {
						this_ { 123.int.native }
					}
				}
			}
		}.assertGives { "123".native_ }
	}

	@Test
	fun debug() {
		evaluate_ {
			zero.is_ { one }.debug
		}.assertGives {
			evaluator {
				parent { nothing }
				evaluated {
					scope {
						dictionary {
							definition {
								list {
									definition {
										pattern { zero }
										body { one }
									}
								}
							}
						}
						export {
							dictionary {
								definition {
									list {
										definition {
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
				mode { evaluate }
			}
		}
	}
}