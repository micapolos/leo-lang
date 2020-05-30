package leo16.library.native

import leo15.dsl.*

val math = dsl_ {
	use { reflection }

	math.class_
	is_ { "java.lang.Math".text.name.class_ }

	math.double.sin.method
	is_ {
		math.class_
		method {
			name { "sin".text }
			parameter { double.class_.item.list }
		}
	}

	math.double.cos.method
	is_ {
		math.class_
		method {
			name { "cos".text }
			parameter { double.class_.item.list }
		}
	}
}