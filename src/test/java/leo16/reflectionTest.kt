package leo16

import leo15.dsl.*
import java.awt.Point
import kotlin.test.Test

class ReflectionTest {
	@Test
	fun typeClass() {
		evaluate_ { reflection.dictionary.import; byte.class_ }
			.assertGives { class_ { Byte::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; char.class_ }
			.assertGives { class_ { Char::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; short.class_ }
			.assertGives { class_ { Short::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; int.class_ }
			.assertGives { class_ { Int::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; long.class_ }
			.assertGives { class_ { Long::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; float.class_ }
			.assertGives { class_ { Float::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; double.class_ }
			.assertGives { class_ { Double::class.java.native_ } }
		evaluate_ { reflection.dictionary.import; boolean.class_ }
			.assertGives { class_ { Boolean::class.java.native_ } }
	}

	@Test
	fun textNameClass() {
		evaluate_ { reflection.dictionary.import; "java.lang.Integer".text.name.class_ }
			.assertGives { class_ { Integer::class.java.native_ } }
	}

	@Test
	fun nativeClassField() {
		evaluate_ {
			reflection.dictionary.import
			"java.lang.Integer".text.name.class_
			field { name { "MAX_VALUE".text } }
		}.assertGives {
			field { Integer::class.java.getField("MAX_VALUE").native_ }
		}
	}

	@Test
	fun nativeFieldGet() {
		evaluate_ {
			reflection.dictionary.import
			"java.lang.Integer".text.name.class_
			field { name { "MAX_VALUE".text } }
			get
		}.assertGives {
			Integer.MAX_VALUE.native_
		}
	}

	@Test
	fun nativeObjectFieldGet() {
		evaluate_ {
			reflection.dictionary.import
			"java.awt.Point".text.name.class_
			constructor { parameter { list } }
			invoke { parameter { list } }
			get {
				"java.awt.Point".text.name.class_
				field { name { "x".text } }
			}
		}.assertGives { 0.native_ }
	}

	@Test
	fun nativeClassConstructor() {
		evaluate_ {
			reflection.dictionary.import
			"java.awt.Point".text.name.class_
			constructor {
				parameter {
					list {
						this_ { int.class_ }
						this_ { int.class_ }
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
			reflection.dictionary.import
			"java.awt.Point".text.name.class_
			constructor {
				parameter {
					list {
						this_ { int.class_ }
						this_ { int.class_ }
					}
				}
			}
			invoke {
				parameter {
					list {
						this_ { 10.int.native }
						this_ { 20.int.native }
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
			reflection.dictionary.import
			"java.lang.String".text.name.class_
			method {
				name { "substring".text }
				parameter {
					list {
						this_ { int.class_ }
						this_ { int.class_ }
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
			reflection.import

			"Hello, world!".text.native
			invoke {
				"java.lang.String".text.name.class_
				method {
					name { "substring".text }
					parameter {
						list {
							this_ { int.class_ }
							this_ { int.class_ }
						}
					}
				}
				parameter {
					list {
						this_ { 7.int.native }
						this_ { 12.int.native }
					}
				}
			}
		}.assertGives { "world".native_ }
	}

	@Test
	fun nativeObjectMethodInvoke() {
		evaluate_ {
			reflection.dictionary.import
			"java.lang.String".text.name.class_
			method {
				name { "valueOf".text }
				parameter {
					list {
						this_ { int.class_ }
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
	fun numberToPrimitive() {
		evaluate_ { reflection.import; 10.toByte().byte.native }.assertGives { 10.toByte().native_ }
		evaluate_ { reflection.import; 10.toShort().short.native }.assertGives { 10.toShort().native_ }
		evaluate_ { reflection.import; 10.int.native }.assertGives { 10.native_ }
		evaluate_ { reflection.import; 10L.long.native }.assertGives { 10L.native_ }
		evaluate_ { reflection.import; 10f.float.native }.assertGives { 10f.native_ }
		evaluate_ { reflection.import; 10.0.double.native }.assertGives { 10.0.native_ }
	}

	@Test
	fun primitiveToNumber() {
		evaluate_ { reflection.import; 10.toByte().byte.number }.assertGives { 10.number }
		evaluate_ { reflection.import; 10.toShort().short.number }.assertGives { 10.number }
		evaluate_ { reflection.import; 10.int.number }.assertGives { 10.number }
		evaluate_ { reflection.import; 10L.long.number }.assertGives { 10.number }
		evaluate_ { reflection.import; 10f.float.number }.assertGives { 10f.number }
		evaluate_ { reflection.import; 10.0.double.number }.assertGives { 10.0.number }
	}

	@Test
	fun booleanToNative() {
		evaluate_ { reflection.import; true_.boolean.native }.assertGives { true.native_ }
		evaluate_ { reflection.import; false_.boolean.native }.assertGives { false.native_ }
	}

	@Test
	fun nullNative() {
		evaluate_ { reflection.import; null_.native }.assertGives { null.native_ }
	}
}