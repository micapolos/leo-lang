package leo16.library.native.context

import leo15.dsl.*
import leo16.library_

val math = dsl_ {
	use { reflection }

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

	math.context.unlimited
	is_ { math.context.unlimited.field.get }
}