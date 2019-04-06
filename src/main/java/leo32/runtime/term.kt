@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List
import leo32.dsl.Expr

data class Term(
	val globalScope: Scope,
	val localScope: Scope,
	val fieldList: List<TermField>,
	val termListDict: Dict<String, List<Term>>,
	val termListOrNull: TermList?,
	val scriptOrNull: Script?) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.term get() =
	Term(scope, scope, list(), stringDict(), null, null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

fun Term.fieldAt(index: I32): TermField =
	fieldList.at(index)

fun Term.at(name: String): List<Term> =
	termListDict.at(name) ?: empty.list()

fun Term.countAt(name: String): I32 =
	at(name).size

fun Term.at(name: String, index: I32): Term =
	at(name).at(index)

fun Term.onlyAt(name: String): Term =
	at(name).only

fun Term.onlyOrNullAt(name: String): Term? =
	at(name).onlyOrNull

val Term.fieldSeq get() =
	fieldList.seq

fun Term.invoke(term: Term): Term =
	begin.fold(term.fieldSeq) { invoke(it) }

fun Term.invoke(field: TermField): Term =
	begin
		.invoke(field.value)
		.let { childTerm ->
			(field.name to childTerm).let { resolvedField ->
				localScope
					.plus(resolvedField)
					.let { newLocalScope ->
						copy(localScope = newLocalScope)
							.plus(resolvedField)
							.let { resolvedTerm -> newLocalScope.invoke(resolvedTerm) }
					}
			}
		}

fun Term.invoke(scope: Scope) =
	term()
		.copy(globalScope = scope, localScope = scope)
		.invoke(this)

val Term.begin get() =
	Term(
		globalScope = globalScope,
		localScope = empty.scope,
		fieldList = empty.list(),
		termListDict = empty.stringDict(),
		termListOrNull = null,
		scriptOrNull = null)

fun Term.plus(field: TermField) =
	Term(
		globalScope = globalScope,
		localScope = localScope,
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this?:empty.list()).add(field.value)
		},
		termListOrNull = when {
				termListOrNull != null -> termListOrNull.plus(field)
				fieldCount.int == 1 -> termListOrNull(fieldList.at(0.i32), field)
				else -> null
		},
		scriptOrNull = Script(this, field))

fun term(name: String, vararg names: String) =
	term().plus(name, *names)

fun Term.plus(name: String, vararg names: String): Term =
	fold(names.reversed()) { plus(it to term()) }.plus(name to term())

fun term(vararg fields: TermField) =
	empty.term.fold(fields) { plus(it) }

fun invoke(vararg fields: TermField) =
	empty.term.fold(fields) { invoke(it) }

fun Appendable.append(term: Term): Appendable =
	tryAppend { appendSimple(term) } ?: appendComplex(term)

fun Appendable.appendComplex(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
			(if (second) first.append(", ") else first).append(it) to true
		}.first

fun Appendable.appendSimple(term: Term): Appendable? =
	when {
			term.scriptOrNull == null -> this
			term.scriptOrNull.lhs.isEmpty -> append(term.scriptOrNull.field)
			else -> null
	}

val Term.seq32: Seq32 get() =
	fieldList.seq.map { seq32 }.flat

val Term.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.termDict() =
	dict<Term, V> { dictKey }

val List<Term>.theTerm get() =
	empty.term.fold(seq) { plus("the" to it) }

fun Term.map(fn: Term.() -> Term): Term =
	if (scriptOrNull == null) this
	else scriptOrNull.lhs.fn().plus(scriptOrNull.field.map(fn))

val Term.evalGet: Term get()  =
	scriptOrNull?.evalGet?:this

val Term.evalWrap: Term get() =
	scriptOrNull?.evalWrap?:this

fun exprTerm(exprs: List<Expr>): Term =
	term().fold(exprs.seq) { plus(it.field) }

fun invoke(expr: Expr, vararg exprs: Expr): List<Expr> =
	empty.term
		.invoke(expr.field)
		.fold(exprs) { invoke(it.field) }
		.exprList

fun expr(vararg exprs: Expr): List<Expr> =
	list<Expr>().fold(exprs) { add(it) }

val Term.exprList: List<Expr> get() =
	list<Expr>().fold(fieldSeq) { add(it.expr) }
