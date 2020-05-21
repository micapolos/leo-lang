package leo16.library.native.context

import leo15.dsl.*
import leo16.compile_

val math = compile_ {
	use { reflection.library }

	math.context.class_
	is_ { "java.math.MathContext".text.name.class_ }


	math.context.unlimited.field
	is_ {
		math.context.class_
		field { name { "UNLIMITED".text } }
	}

	math.context.decimal.field
	is_ {
		math.context.class_
		field { name { "DECIMAL128".text } }
	}

	math.context.decimal
	is_ { math.context.decimal.field.get }
}