@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo.binary.byteBitSeq
import leo32.Seq32
import leo32.base.*
import leo32.base.List

data class Term(
	val scope: Scope,
	val localScope: Scope,
	val fieldList: List<TermField>,
	val termListDict: Dict<Symbol, List<Term>>,
	val termListOrNull: TermList?,
	val isList: Boolean,
	val nodeOrNull: Node?,
	val intOrNull: Int?,
	val typeTermOrNull: Term?,
	val selfOrNull: Term?,
	val switchOrNull: Switch?,
	val alternativesTermOrNull: Term?,
	val quoteDepth: I32) {
	override fun toString() = appendableString { it.append(this) }
}

val Scope.emptyTerm get() =
	Term(this, this, list(), empty.symbolDict(), null, true, null, null, null, null, null, null, 0.i32)

val Empty.term get() =
	Term(scope, scope, list(), symbolDict(), null, true, null, null, null, null, null, null, 0.i32)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

val Term.simpleNameOrNull get() =
	nodeOrNull?.simpleNameOrNull

fun Term.fieldAt(index: I32): TermField =
	fieldList.at(index)

fun Term.at(name: Symbol): List<Term> =
	termListDict.at(name) ?: empty.list()

fun Term.countAt(name: Symbol): I32 =
	at(name).size

fun Term.at(name: Symbol, index: I32): Term =
	at(name).at(index)

fun Term.onlyAt(name: Symbol): Term =
	at(name).only

fun Term.onlyOrNullAt(name: Symbol): Term? =
	at(name).onlyOrNull

fun Term.simpleAtOrNull(symbol: Symbol): Term? =
	nodeOrNull?.simpleAtOrNull(symbol)

fun Term.listTermSeqOrNull(key: Symbol): Seq<Term>? =
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
	simpleAtOrNull(errorSymbol)?.let { this }

fun Term.evalPlusQuote(line: Line): Term? =
	notNullIf(line.name == quoteSymbol) { plus(line.value.term) }

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
	if (quoteDepth.isZero)
		if (!isEmpty && field.value.isEmpty) clear.plusMacro(field.name to this).invoke
		else plusMacro(field).invoke
	else plus(field)

val Term.begin get() =
	Term(
		scope = scope,
		localScope = scope,
		fieldList = empty.list(),
		termListDict = empty.symbolDict(),
		termListOrNull = null,
		isList = true,
		nodeOrNull = null,
		intOrNull = null,
		typeTermOrNull = null,
		selfOrNull = selfOrNull,
		switchOrNull = null,
		alternativesTermOrNull = null,
		quoteDepth = quoteDepth)

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
		selfOrNull = selfOrNull,
		switchOrNull = ifOrNull(nodeOrNull == null) { field.switchOrNull },
		alternativesTermOrNull = alternativesTermForPlusOrNull?.let { alternativesTerm ->
			ifOrNull(field.name == eitherSymbol) {
				field.value.nodeOrNull?.let { node ->
					ifOrNull(node.lhs.isEmpty) {
						alternativesTerm.plus(node.field)
					}
				}
			}
		},
		quoteDepth = quoteDepth)
}

val Term.isSimple
	get() =
		nodeOrNull != null && nodeOrNull.isSimple

fun Term.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

val Term.typeTerm get() =
	typeTermOrNull ?: term().plus(this)

val Term.alternativesTermForPlusOrNull
	get() =
		if (isEmpty) term()
		else alternativesTermOrNull

val Term.invoke
	get() =
		typeTerm.let { typeTerm ->
			scope.typeToBodyDictionary.at(typeTerm)?.let { body ->
				typeTerm.clear
					.copy(selfOrNull = clear.plus(selfSymbol to this))
					.invoke(body)
					.copy(selfOrNull = selfOrNull)
			}.orIfNull { this }
		}

val Term.self
	get() =
		selfOrNull ?: term(selfSymbol)

fun term(name: Symbol) =
	term().plus(name to term())

fun term(name: String) =
	term(symbol(name))

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

val Term.byteSeq
	get() =
		fieldList.seq.map { byteSeq }.flat

val Term.bitSeq
	get() =
		byteSeq.byteBitSeq

fun <V: Any> Empty.termDict() =
	dict<Term, V> { bitSeq }

val List<Term>.theTerm get() =
	empty.term.fold(seq) { plus(theSymbol to it) }

fun Term.map(fn: Term.() -> Term): Term =
	if (nodeOrNull == null) this
	else nodeOrNull.lhs.fn().plus(nodeOrNull.field.map(fn))

fun Term.plusMacroGet(field: TermField): Term? =
	ifOrNull(isEmpty) {
		field.value.nodeOrNull?.let { fieldNode ->
			ifOrNull(fieldNode.lhs.isEmpty) {
				fieldNode.field.value.onlyOrNullAt(field.name)?.let { value ->
					clear.plus(field.name to value)
				}
			}
		}
	}

fun Term.plusMacroEquals(field: TermField): Term? =
	if (field.name == equalsSymbol) clear.plus(termField(this == field.value))
	else null

fun Term.plusMacroLhs(field: TermField): Term? =
	if (isEmpty
		&& field.name == lhsSymbol
		&& field.value.nodeOrNull != null
		&& !field.value.nodeOrNull.lhs.isEmpty)
		clear.plus(field.value.nodeOrNull.lhs)
	else null

fun Term.plusMacroRhs(field: TermField): Term? =
	if (isEmpty
		&& field.name == rhsSymbol
		&& field.value.nodeOrNull != null
		&& !field.value.nodeOrNull.field.value.isEmpty)
		clear.plus(field.value.nodeOrNull.field.value)
	else null

fun Term.plusMacroClass(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == classSymbol) {
		field.value.typeTerm
	}

fun Term.plusMacroDescribe(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == describeSymbol) {
		scope
			.typeToDescribeDictionary
			.at(field.value)
			?.let { typeOrNull -> clear.plus(typeOrNull) }
			.orIfNull { clear.plus(field.value) }
	}

fun Term.plusMacroBody(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == bodySymbol) {
		scope
			.typeToBodyDictionary
			.at(field.value)
			?.let { typeOrNull -> clear.plus(typeOrNull) }
			.orIfNull { clear.plus(field.value) }
	}

fun Term.plusMacroDefine(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == defineSymbol) {
		invokeDefine(field.value)
	}

fun Term.plusMacroTest(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == testSymbol) {
		invokeTest(field.value)
	}

fun Term.plusMacroUnquote(field: TermField): Term? =
	if (field.name == unquoteSymbol) clear.plus(termField(this == field.value))
	else null

fun Term.plusMacroSelf(field: TermField): Term? =
	notNullIf(isEmpty && field.name == selfSymbol && field.value.isEmpty) {
		clear.plus(self)
	}

fun Term.plusMacroSwitch(field: TermField): Term? =
	field.switchOrNull?.invoke(this)

fun Term.plusMacro(field: TermField): Term =
	null
		?: plusMacroUnquote(field)
		?: plusMacroEquals(field)
		?: plusMacroDefine(field)
		?: plusMacroTest(field)
		?: plusMacroClass(field)
		?: plusMacroDescribe(field)
		?: plusMacroBody(field)
		?: plusMacroSelf(field)
		?: plusMacroGet(field)
		?: plusMacroSwitch(field)
		?: plusMacroLhs(field)
		?: plusMacroRhs(field)
		?: plus(field)

fun Term.invokeDefine(term: Term) =
	term.nodeOrNull?.let { node ->
		ifOrNull(!node.lhs.isEmpty && !node.field.value.isEmpty) {
			when (node.field.name) {
				givesSymbol -> clear.set(scope.define(node.lhs caseTo node.field.value))
				hasSymbol -> clear.set(scope.define(node.lhs has node.field.value))
				else -> null
			}
		}
	}

fun Term.invokeTest(term: Term) =
	term.nodeOrNull?.let { node ->
		ifOrNull(!node.lhs.isEmpty && !node.field.value.isEmpty) {
			when (node.field.name) {
				givesSymbol ->
					clear.invoke(node.lhs).descope.let { actual ->
						clear.invoke(node.field.value).descope.let { expected ->
							if (actual == expected) clear
							else throw AssertionError(
								term(
									errorSymbol to term(
										testSymbol to term,
										expectedSymbol to expected,
										actualSymbol to actual)).script.code.string)
						}
					}
				else -> null
			}
		}
	}

val Script.term get() =
	term().fold(lineSeq) { plus(it.field) }

val Term.script: Script get() =
	Script(list<Line>().fold(fieldSeq) { add(it.line) })

fun invoke(line: Line, vararg lines: Line): Script =
	invokeTerm(line, *lines).script

fun invokeTerm(line: Line, vararg lines: Line): Term =
	term().plus(line).fold(lines) { plus(it) }

val Term.clear get() =
	scope.emptyTerm.copy(selfOrNull = selfOrNull)

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

// === chained syntax

val Term.quote
	get() =
		copy(quoteDepth = quoteDepth.inc)

val Term.unquote
	get() =
		copy(quoteDepth = quoteDepth.dec)

fun Term.plus(string: String, fn: Term.() -> Term): Term =
	if (string == "quote") plus(begin.quote.fn())
	else if (!quoteDepth.isZero) plus(string to begin.fn())
	else if (string == "test") plusResolved("test" to begin.quote.fn())
	else if (string == "error") plusResolved("error" to begin.quote.fn())
	else plusResolved(string to begin.fn())

fun Term.plus(string: String) =
	plus(string) { this }

val Term.descope: Term
	get() =
		empty.term.fold(fieldSeq) {
			plus(it.name to it.value.descope)
		}

//fun Term.typedInvoke(field: TermField): Term =
//	failIfOr(!isEmpty && field.value.isEmpty) {
//		if (isEmpty) TODO()
//		else TODO()
//	}
