package leo16.library.native

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(objects)
}

val objects = dsl_ {
	use { native.reflection }

	object_.class_
	is_ { "java.lang.Object".text.name.class_ }

	test {
		object_.class_.native.as_ { text }
		equals_ { Object::class.java.nativeText }
	}

	object_.eq.method
	is_ {
		object_.class_
		method {
			name { "equals".text }
			parameter { list { item { object_.class_ } } }
		}
	}

	test {
		object_.eq.method.native.as_ { text }
		equals_ { Object::class.java.getMethod("equals", Object::class.java).nativeText }
	}

	object_.hash.code.method
	is_ {
		object_.class_
		method {
			name { "hashCode".text }
			parameter { empty.list }
		}
	}

	test {
		object_.hash.code.method.native.as_ { text }
		equals_ { Object::class.java.getMethod("hashCode").nativeText }
	}
}