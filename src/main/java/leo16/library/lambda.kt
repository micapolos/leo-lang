package leo16.library

import leo.base.print
import leo15.dsl.*
import leo15.lambda.at
import leo15.lambda.invoke
import leo15.lambda.valueTerm
import leo16.compile_

fun main() {
	lambda.value.print
}

val lambda = compile_ {
	use { reflection }
	use { lambda.native }

	native.number.at
	does {
		index.term.constructor
		invoke { parameter { list { item { at.number.int.native } } } }
	}

	test { 3.number.at.as_ { text }.equals_ { at(3).nativeText } }

	native.lambda
	does {
		abstraction.term.constructor
		invoke { parameter { list { item { lambda.native } } } }
	}

	test { 3.number.at.lambda.as_ { text }.equals_ { leo15.lambda.fn(at(3)).nativeText } }

	native
	apply { native }
	does {
		application.term.constructor
		invoke {
			parameter {
				list {
					item { native }
					item { apply.native }
				}
			}
		}
	}

	test {
		3.number.at.apply { 4.number.at }.as_ { text }
			.equals_ { at(3).invoke(at(4)).nativeText }
	}

	native.value
	does {
		meta { value }.term.constructor
		invoke { parameter { list { item { value.native } } } }
	}

	test { "foo".text.native.value.as_ { text }.equals_ { "foo".valueTerm.nativeText } }

	native.eval
	does {
		term.eval.method
		invoke { parameter { list { item { eval.native } } } }
	}

//	test {
//		0.number.at.lambda
//		apply { "foo".text.native.value }
//		eval
//		as_ { text }
//		equals_ { "foo".text.native.value.nativeText }
//	}
}