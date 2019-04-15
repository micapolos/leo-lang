@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List

data class Term(
	val scope: Scope,
	val localScope: Scope,
	val fieldList: List<TermField>,
	val termListDict: Dict<String, List<Term>>,
	val termListOrNull: TermList?,
	val isList: Boolean,
	val nodeOrNull: Node?,
	val intOrNull: Int?,
	val typeTermOrNull: Term?,
	val argumentOrNull: Term?,
	val switchOrNull: Switch?) {
	override fun toString() = appendableString { it.append(this) }
}

val Scope.emptyTerm get() =
	Term(this, this, list(), empty.stringDict(), null, true, null, null, null, null, null)

val Empty.term get() =
	Term(scope, scope, list(), stringDict(), null, true, null, null, null, null, null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

val Term.simpleNameOrNull get() =
	nodeOrNull?.simpleNameOrNull

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

fun Term.simpleAtOrNull(name: String): Term? =
	nodeOrNull?.simpleAtOrNull(name)

fun Term.listTermSeqOrNull(key: String): Seq<Term>? =
	notNullIf(isList && (nodeOrNull == null || nodeOrNull.field.name == key)) {
		fieldList.seq.map { value }
	}

val Term.fieldSeq get() =
	fieldList.seq

fun Term.plus(line: Line): Term =
	null
		?: evalError
		?: evalPlusQuote(line)
		?: invoke(line)

val Term.evalError: Term? get() =
	simpleAtOrNull("error")?.let { this }

fun Term.evalPlusQuote(line: Line): Term? =
	notNullIf(line.name == "quote") { plus(line.value.term) }

fun Term.plus(script: Script) =
	fold(script.lineSeq, Term::plus)

fun Term.invoke(line: Line): Term =
	begin
		.plus(line.value)
		.let { childTerm ->
			plusResolved(line.name to childTerm)
		}

fun Term.invoke(script: Script): Term =
	fold(script.lineSeq) { invoke(it) }

fun Term.invoke(field: TermField): Term =
	begin
		.invoke(field.value)
		.let { childTerm ->
			plusResolved(field.name to childTerm)
		}

fun Term.invoke(term: Term): Term =
	fold(term.fieldSeq) { invoke(it) }

fun Term.plusResolved(field: TermField) =
	plusMacro(field).invoke

val Term.begin get() =
	Term(
		scope = scope,
		localScope = scope,
		fieldList = empty.list(),
		termListDict = empty.stringDict(),
		termListOrNull = null,
		isList = true,
		nodeOrNull = null,
		intOrNull = null,
		typeTermOrNull = null,
		argumentOrNull = argumentOrNull,
		switchOrNull = null)

fun Term.plus(field: TermField): Term {
	val localScope = localScope.plusValue(field)
	return Term(
		scope = scope,
		localScope = localScope,
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this ?: empty.list()).add(field.value)
		},
		termListOrNull = when {
			termListOrNull != null -> termListOrNull.plus(field)
			fieldCount.int == 1 -> termListOrNull(fieldList.at(0.i32), field)
			else -> null
		},
		isList = nodeOrNull == null || nodeOrNull.field.name == field.name,
		nodeOrNull = Node(this, field),
		intOrNull = if (nodeOrNull == null) field.intOrNull else null,
		typeTermOrNull =
		localScope
			.valueToTypeDictionary
			.valueOrNull
			?: notNullIf(field.value.typeTermOrNull != null || typeTermOrNull != null) {
				typeTerm.plus(field.typeTermField)
			},
		argumentOrNull = argumentOrNull,
		switchOrNull = ifOrNull(nodeOrNull == null) { field.switchOrNull })
}

fun Term.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

val Term.typeTerm get() =
	typeTermOrNull ?: term().plus(this)

val Term.invoke
	get() =
		typeTerm.let { typeTerm ->
			scope.typeToBodyDictionary.at(typeTerm)?.let { body ->
				typeTerm.clear
					.copy(argumentOrNull = this)
					.invoke(body)
					.copy(argumentOrNull = argumentOrNull)
			}.orIfNull { this }
		}

val Term.argument
	get() =
		argumentOrNull.orIfNull { term("argument") }

fun term(name: String, vararg names: String) =
	term().plus(name, *names)

fun Term.plus(name: String, vararg names: String): Term =
	fold(names.reversed()) { plus(it to term()) }.plus(name to term())

fun Term.leafPlus(term: Term): Term =
	nodeOrNull?.leafPlus(term)?.term ?: plus(term)

fun term(vararg fields: TermField): Term =
	empty.term.fold(fields) { plus(it) }

infix fun Term.of(type: Term) =
	copy(typeTermOrNull = type)

fun Appendable.append(term: Term): Appendable =
	tryAppend { appendSimple(term) } ?: appendComplex(term)

fun Appendable.appendComplex(term: Term): Appendable =
	(this to false).fold(term.fieldSeq) {
			(if (second) first.append(", ") else first).append(it) to true
		}.first

fun Appendable.appendSimple(term: Term): Appendable? =
	when {
			term.nodeOrNull == null -> this
			term.nodeOrNull.lhs.isEmpty -> append(term.nodeOrNull.field)
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
	if (nodeOrNull == null) this
	else nodeOrNull.lhs.fn().plus(nodeOrNull.field.map(fn))

fun Term.plusMacroGet(field: TermField): Term? =
	ifOrNull(field.value.isEmpty) {
		onlyOrNullAt(field.name)
	}

fun Term.plusMacroWrap(field: TermField): Term? =
	if (!isEmpty && field.value.isEmpty) term(field.name to this)
	else null

fun Term.plusMacroEquals(field: TermField): Term? =
	if (field.name == "equals") clear.plus(termField(this == field.value))
	else null

fun Term.plusMacroHas(field: TermField): Term? =
	ifOrNull(!isEmpty && field.name == "has" && !field.value.isEmpty) {
		clear.set(scope.define(this has field.value))
	}

fun Term.plusMacroClass(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == "class") {
		field.value.typeTerm
	}

fun Term.plusMacroDescribe(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == "describe") {
		scope
			.typeToValueDictionary
			.at(field.value)
			?.let { typeOrNull -> clear.plus(typeOrNull) }
			.orIfNull { clear.plus(field.value) }
	}

fun Term.plusMacroGives(field: TermField): Term? =
	ifOrNull(field.name == "gives" && !isEmpty && !field.value.isEmpty) {
		clear.set(scope.define(this caseTo field.value))
	}

fun Term.plusMacroUnquote(field: TermField): Term? =
	if (field.name == "unquote") clear.plus(termField(this == field.value))
	else null

fun Term.plusMacroIs(field: TermField): Term? =
	if (field.name == "is") clear.plus(leafPlus(field.value))
	else null

fun Term.plusMacroArgument(field: TermField): Term? =
	notNullIf(isEmpty && field.name == "argument" && field.value.isEmpty) {
		clear.plus(argument)
	}

fun Term.plusMacroSwitch(field: TermField): Term? =
	field.switchOrNull?.invoke(this)

fun Term.plusMacro(field: TermField): Term =
	null
		?:plusMacroUnquote(field)
		?:plusMacroIs(field)
		?:plusMacroEquals(field)
		?:plusMacroHas(field)
		?:plusMacroGives(field)
		?:plusMacroClass(field)
		?:plusMacroDescribe(field)
		?: plusMacroArgument(field)
		?:plusMacroGet(field)
		?:plusMacroWrap(field)
		?: plusMacroSwitch(field)
		?:plus(field)

val Script.term get() =
	term().fold(lineSeq) { plus(it.field) }

val Term.script: Script get() =
	Script(list<Line>().fold(fieldSeq) { add(it.line) })

fun invoke(line: Line, vararg lines: Line): Script =
	invokeTerm(line, *lines).script

fun invokeTerm(line: Line, vararg lines: Line): Term =
	term().plus(line).fold(lines) { plus(it) }

val Term.clear get() =
	scope.emptyTerm.copy(argumentOrNull = argumentOrNull)

fun Term.set(scope: Scope) =
	copy(scope = scope, localScope = scope)

fun term(boolean: Boolean) =
	term(termField(boolean))

fun term(int: Int) =
	term(termField(int))

fun Term.invoke(termHasTerm: TermHasTerm) =
	set(scope.define(termHasTerm))

fun Term.switchAdd(case: Case): Term? =
	if (isEmpty) plus("switch" to case.term)
	else ifNotNull(switchOrNull) {
		scope.emptyTerm.plus("switch" to nodeOrNull!!.field.value.plus(case.term))
	}