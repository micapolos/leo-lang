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
		}.assertDoes { class_ { String::class.java.native_ } }
	}

	@Test
	fun typeClass() {
		evaluate_ { native.reflection.import; byte.class_ }
			.assertDoes { class_ { Byte::class.java.native_ } }
		evaluate_ { native.reflection.import; char.class_ }
			.assertDoes { class_ { Char::class.java.native_ } }
		evaluate_ { native.reflection.import; short.class_ }
			.assertDoes { class_ { Short::class.java.native_ } }
		evaluate_ { native.reflection.import; int.class_ }
			.assertDoes { class_ { Int::class.java.native_ } }
		evaluate_ { native.reflection.import; long.class_ }
			.assertDoes { class_ { Long::class.java.native_ } }
		evaluate_ { native.reflection.import; float.class_ }
			.assertDoes { class_ { Float::class.java.native_ } }
		evaluate_ { native.reflection.import; double.class_ }
			.assertDoes { class_ { Double::class.java.native_ } }
		evaluate_ { native.reflection.import; boolean.class_ }
			.assertDoes { class_ { Boolean::class.java.native_ } }
	}

	@Test
	fun textNameClass() {
		evaluate_ { native.reflection.import; "java.lang.Integer".text.name.class_ }
			.assertDoes { class_ { Integer::class.java.native_ } }
	}

	@Test
	fun nativeClassField() {
		evaluate_ {
			native.reflection.import
			"java.lang.Integer".text.name.class_
			field { name { "MAX_VALUE".text } }
		}.assertDoes {
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
		}.assertDoes {
			Integer.MAX_VALUE.native_
		}
	}

	@Test
	fun nativeObjectFieldGet() {
		evaluate_ {
			native.reflection.import
			"java.awt.Point".text.name.class_
			constructor { parameter { empty.list } }
			invoke { parameter { empty.list } }
			get {
				"java.awt.Point".text.name.class_
				field { name { "x".text } }
			}
		}.assertDoes { 0.native_ }
	}

	@Test
	fun nativeClassConstructor() {
		evaluate_ {
			native.reflection.import
			"java.awt.Point".text.name.class_
			constructor {
				parameter {
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
		}.assertDoes {
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
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
			invoke {
				parameter {
					list {
						item { 10.int.native }
						item { 20.int.native }
					}
				}
			}
		}.assertDoes {
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
					list {
						item { int.class_ }
						item { int.class_ }
					}
				}
			}
		}.assertDoes {
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
							item { int.class_ }
							item { int.class_ }
						}
					}
				}
				parameter {
					list {
						item { 7.int.native }
						item { 12.int.native }
					}
				}
			}
		}.assertDoes { "world".native_ }
	}

	@Test
	fun nativeObjectMethodInvoke() {
		evaluate_ {
			reflection.import
			"java.lang.String".text.name.class_
			method {
				name { "valueOf".text }
				parameter {
					list {
						item { int.class_ }
					}
				}
			}
			invoke {
				parameter {
					list {
						item { 123.int.native }
					}
				}
			}
		}.assertDoes { "123".native_ }
	}

	@Test
	fun numberToPrimitive() {
		evaluate_ { reflection.import; 10.toByte().byte.native }.assertDoes { 10.toByte().native_ }
		evaluate_ { reflection.import; 10.toShort().short.native }.assertDoes { 10.toShort().native_ }
		evaluate_ { reflection.import; 10.int.native }.assertDoes { 10.native_ }
		evaluate_ { reflection.import; 10L.long.native }.assertDoes { 10L.native_ }
		evaluate_ { reflection.import; 10f.float.native }.assertDoes { 10f.native_ }
		evaluate_ { reflection.import; 10.0.double.native }.assertDoes { 10.0.native_ }
	}

	@Test
	fun primitiveToNumber() {
		evaluate_ { reflection.import; 10.toByte().byte.number }.assertDoes { 10.number }
		evaluate_ { reflection.import; 10.toShort().short.number }.assertDoes { 10.number }
		evaluate_ { reflection.import; 10.int.number }.assertDoes { 10.number }
		evaluate_ { reflection.import; 10L.long.number }.assertDoes { 10.number }
		evaluate_ { reflection.import; 10f.float.number }.assertDoes { 10f.number }
		evaluate_ { reflection.import; 10.0.double.number }.assertDoes { 10.0.number }
	}

	@Test
	fun booleanToNative() {
		evaluate_ { reflection.import; true_.boolean.native }.assertDoes { true.native_ }
		evaluate_ { reflection.import; false_.boolean.native }.assertDoes { false.native_ }
	}

	@Test
	fun nullNative() {
		evaluate_ { native.reflection.import; null_.native }.assertDoes { null.native_ }
	}
}