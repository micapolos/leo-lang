package leo16.library.native

import leo15.dsl.*
import leo15.lambda.AbstractionTerm
import leo15.lambda.ApplicationTerm
import leo15.lambda.IndexTerm
import leo15.lambda.Term
import leo15.lambda.ValueTerm
import leo16.library_

fun main() {
	library_(lambda)
}

val lambda = dsl_ {
	use { reflection }
	use { objects.native }

	term.class_.is_ { "leo15.lambda.Term".text.name.class_ }
	index.term.class_.is_ { "leo15.lambda.IndexTerm".text.name.class_ }
	abstraction.term.class_.is_ { "leo15.lambda.AbstractionTerm".text.name.class_ }
	application.term.class_.is_ { "leo15.lambda.ApplicationTerm".text.name.class_ }
	value.term.class_.is_ { "leo15.lambda.ValueTerm".text.name.class_ }
	eval.class_.is_ { "leo15.lambda.EvalKt".text.name.class_ }

	index.term.constructor
	is_ {
		index.term.class_
		constructor { parameter { list { item { int.class_ } } } }
	}

	abstraction.term.constructor
	is_ {
		abstraction.term.class_
		constructor { parameter { list { item { term.class_ } } } }
	}

	application.term.constructor
	is_ {
		application.term.class_
		constructor {
			parameter {
				list {
					item { term.class_ }
					item { term.class_ }
				}
			}
		}
	}

	value.term.constructor
	is_ {
		value.term.class_
		constructor { parameter { list { item { object_.class_ } } } }
	}

	term.eval.method
	is_ {
		eval.class_
		method {
			name { "getEval".text }
			parameter { list { item { term.class_ } } }
		}
	}

	test { term.class_.native.as_ { text }.equals_ { Term::class.java.nativeText } }
	test { index.term.class_.native.as_ { text }.equals_ { IndexTerm::class.java.nativeText } }
	test { abstraction.term.class_.native.as_ { text }.equals_ { AbstractionTerm::class.java.nativeText } }
	test { application.term.class_.native.as_ { text }.equals_ { ApplicationTerm::class.java.nativeText } }
	test { value.term.class_.native.as_ { text }.equals_ { ValueTerm::class.java.nativeText } }
}