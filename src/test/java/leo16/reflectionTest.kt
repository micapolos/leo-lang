package leo16

import leo15.dsl.*
import java.awt.Point
import kotlin.test.Test

class ReflectionTest {
	@Test
	fun nativeClass() {
		evaluate_ {
			native.reflection.import
			"Hello!".text.native.object_.class_
		}.assertGives { class_ { String::class.java.native_ } }
	}

	@Test
	fun typeClass() {
		evaluate_ { native.reflection.import; byte.class_ }
			.assertGives { class_ { Byte::class.java.native_ } }
		evaluate_ { native.reflection.import; char.class_ }
			.assertGives { class_ { Char::class.java.native_ } }
		evaluate_ { native.reflection.import; short.class_ }
			.assertGives { class_ { Short::class.java.native_ } }
		evaluate_ { native.reflection.import; int.class_ }
			.assertGives { class_ { Int::class.java.native_ } }
		evaluate_ { native.reflection.import; long.class_ }
			.assertGives { class_ { Long::class.java.native_ } }
		evaluate_ { native.reflection.import; float.class_ }
			.assertGives { class_ { Float::class.java.native_ } }
		evaluate_ { native.reflection.import; double.class_ }
			.assertGives { class_ { Double::class.java.native_ } }
		evaluate_ { native.reflection.import; boolean.class_ }
			.assertGives { class_ { Boolean::class.java.native_ } }
	}

	@Test
	fun textNameClass() {
		evaluate_ { native.reflection.import; "java.lang.Integer".text.name.class_ }
			.assertGives { class_ { Integer::class.java.native_ } }
	}

	@Test
	fun nativeClassField() {
		evaluate_ {
			native.reflection.import
			"java.lang.Integer".text.name.class_
			field { name { "MAX_VALUE".text } }
		}.assertGives {
			field { Integer::class.java.getField("MAX_VALUE").native_ }
		}
	}

	@Test
	fun nativeFieldGet() {
		evaluate_ {
			native.reflection.import
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
			native.reflection.import
			"java.awt.Point".text.name.class_
			constructor { parameter { empty.stack } }
			invoke { parameter { empty.stack } }
			get {
				"java.awt.Point".text.name.class_
				field { name { "x".text } }
			}
		}.assertGives { 0.native_ }
	}

	@Test
	fun nativeClassConstructor() {
		evaluate_ {
			native.reflection.import
			"java.awt.Point".text.name.class_
			constructor {
				parameter {
					stack {
						item { int.class_ }
						item { int.class_ }
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
			reflection.import
			"java.awt.Point".text.name.class_
			constructor {
				parameter {
					stack {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
			invoke {
				parameter {
					stack {
						item { 10.int.native }
						item { 20.int.native }
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
			native.reflection.import
			"java.lang.String".text.name.class_
			method {
				name { "substring".text }
				parameter {
					stack {
						item { int.class_ }
						item { int.class_ }
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
						stack {
							item { int.class_ }
							item { int.class_ }
						}
					}
				}
				parameter {
					stack {
						item { 7.int.native }
						item { 12.int.native }
					}
				}
			}
		}.assertGives { "world".native_ }
	}

	@Test
	fun nativeObjectMethodInvoke() {
		evaluate_ {
			reflection.import
			"java.lang.String".text.name.class_
			method {
				name { "valueOf".text }
				parameter {
					stack {
						item { int.class_ }
					}
				}
			}
			invoke {
				parameter {
					stack {
						item { 123.int.native }
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
		evaluate_ { native.reflection.import; null_.native }.assertGives { null.native_ }
	}
}