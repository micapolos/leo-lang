package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.string
import leo32.base.BranchTree
import leo32.base.LeafTree

data class Value(
	val scope: Scope,
	val localScope: Scope,
	val term: Term) {
	override fun toString() = term.string
}

val Empty.value get() =
	Value(scope, scope, term)

val Scope.value get() =
	Value(this, this, empty.term)

fun Value.plus(field: TermField): Value =
	plus(field.name to Value(scope, scope, term()).plus(field.value))

fun Value.plus(term: Term): Value =
	fold(term.fieldSeq) { plus(it) }

fun Value.plus(string: String): Value =
	plus(string to term())

fun value(vararg fields: TermField): Value =
	empty.value.fold(fields) { plus(it) }

fun value(key: String) =
	value(key to term())

fun Value.plus(field: ValueField): Value =
	(field.key.string to field.value.term)
		.let { termField ->
		localScope
			.plus(termField)
			.let { newLocalScope ->
				when (newLocalScope.functionTree) {
					is LeafTree ->
						newLocalScope.functionTree.leaf.value.let { functionOrNull ->
							if (functionOrNull == null)
								copy(localScope = newLocalScope, term = term.plus(termField))
							else
								functionOrNull.invoke(parameter(term.plus(termField))).let { invokedTerm ->
									copy(
										localScope = scope.plus(invokedTerm),
										term = invokedTerm)
								}
						}
					is BranchTree ->
						copy(localScope = newLocalScope, term = term.plus(termField))
				}
			}
	}
