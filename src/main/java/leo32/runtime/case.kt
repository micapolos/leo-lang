package leo32.runtime

import leo.base.ifOrNull
import leo.base.notNullIf

data class Case(
	val key: Term,
	val value: Term)

infix fun Term.caseTo(term: Term) =
	Case(this, term)

val TermField.caseOrNull: Case?
	get() =
		ifOrNull(name == "case") {
			value.nodeOrNull?.bodyCaseOrNull
		}

val Node.bodyCaseOrNull: Case?
	get() =
		notNullIf(!lhs.isEmpty && field.name == "to" && !field.value.isEmpty) {
			lhs caseTo field.value
		}
