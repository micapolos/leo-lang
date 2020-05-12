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
		}.assertEquals { class_ { String::class.java.native_ } }
	}

	@Test
	fun typeClass() {
		evaluate_ { native.reflection.import; byte.class_ }
			.assertEquals { class_ { Byte::class.java.native_ } }
		evaluate_ { native.reflection.import; char.class_ }
			.assertEquals { class_ { Char::class.java.native_ } }
		evaluate_ { native.reflection.import; short.class_ }
			.assertEquals { class_ { Short::class.java.native_ } }
		evaluate_ { native.reflection.import; int.class_ }
			.assertEquals { class_ { Int::class.java.native_ } }
		evaluate_ { native.reflection.import; long.class_ }
			.assertEquals { class_ { Long::class.java.native_ } }
		evaluate_ { native.reflection.import; float.class_ }
			.assertEquals { class_ { Float::class.java.native_ } }
		evaluate_ { native.reflection.import; double.class_ }
			.assertEquals { class_ { Double::class.java.native_ } }
		evaluate_ { native.reflection.import; boolean.class_ }
			.assertEquals { class_ { Boolean::class.java.native_ } }
	}

	@Test
	fun textNameClass() {
		evaluate_ { native.reflection.import; "java.lang.Integer".text.name.class_ }
			.assertEquals { class_ { Integer::class.java.native_ } }
	}

	@Test
	fun nativeClassField() {
		evaluate_ {
			native.reflection.import
			"java.lang.Integer".text.name.class_
			field { name { "MAX_VALUE".text } }
		}.assertEquals {
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
		}.assertEquals {
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
		}.assertEquals { 0.native_ }
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
		}.assertEquals {
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
		}.assertEquals {
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
		}.assertEquals {
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
		}.assertEquals { "world".native_ }
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
		}.assertEquals { "123".native_ }
	}

	@Test
	fun numberToPrimitive() {
		evaluate_ { reflection.import; 10.toByte().byte.native }.assertEquals { 10.toByte().native_ }
		evaluate_ { reflection.import; 10.toShort().short.native }.assertEquals { 10.toShort().native_ }
		evaluate_ { reflection.import; 10.int.native }.assertEquals { 10.native_ }
		evaluate_ { reflection.import; 10L.long.native }.assertEquals { 10L.native_ }
		evaluate_ { reflection.import; 10f.float.native }.assertEquals { 10f.native_ }
		evaluate_ { reflection.import; 10.0.double.native }.assertEquals { 10.0.native_ }
	}

	@Test
	fun primitiveToNumber() {
		evaluate_ { reflection.import; 10.toByte().byte.number }.assertEquals { 10.number }
		evaluate_ { reflection.import; 10.toShort().short.number }.assertEquals { 10.number }
		evaluate_ { reflection.import; 10.int.number }.assertEquals { 10.number }
		evaluate_ { reflection.import; 10L.long.number }.assertEquals { 10.number }
		evaluate_ { reflection.import; 10f.float.number }.assertEquals { 10f.number }
		evaluate_ { reflection.import; 10.0.double.number }.assertEquals { 10.0.number }
	}

	@Test
	fun booleanToNative() {
		evaluate_ { reflection.import; true_.boolean.native }.assertEquals { true.native_ }
		evaluate_ { reflection.import; false_.boolean.native }.assertEquals { false.native_ }
	}

	@Test
	fun nullNative() {
		evaluate_ { native.reflection.import; null_.native }.assertEquals { null.native_ }
	}
}