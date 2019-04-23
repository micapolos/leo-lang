@file:Suppress("unused")

package leo32.runtime

import leo.base.*
import leo.binary.byteBitSeq
import leo32.base.*
import leo32.base.List

data class Term(
	val scope: Scope,
	val fieldList: List<TermField>,
	val termListDict: Dict<Symbol, List<Term>>,
	val nodeOrNull: Node?,
	val alternativesTermOrNull: Term?) {
	override fun toString() = appendableString { it.append(this) }
}

val Scope.emptyTerm get() =
	Term(this, list(), empty.symbolDict(), null, null)

val Empty.term get() =
	Term(scope, list(), symbolDict(), null, null)

val Term.fieldCount get() =
	fieldList.size

val Term.isEmpty get() =
	fieldCount.isZero

val Term.simpleNameOrNull get() =
	nodeOrNull?.simpleNameOrNull

fun Term.fieldAt(index: I32): TermField =
	fieldList.at(index)

fun Term.at(name: Symbol): List<Term> =
	termListDict.uncheckedAt(name) ?: empty.list()

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

val Term.fieldSeq get() =
	fieldList.seq

fun Term.plus(line: Line): Term =
	null
		?: evalError
		?: evalPlusQuote(line)
		?: evalPlusWith(line)
		?: invoke(line)

val Term.evalError: Term? get() =
	simpleAtOrNull(errorSymbol)?.let { this }

fun Term.evalPlusQuote(line: Line): Term? =
	notNullIf(line.name == quoteSymbol) { plus(line.value.term) }

fun Term.evalPlusWith(line: Line): Term? =
	notNullIf(line.name == withSymbol) { shortQuote.plus(line.value.term) }

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
	if (field.name == withSymbol) shortQuote.invoke(field.value)
	else if (field.name == quoteSymbol) quote.invoke(field.value)
	else if (field.name == unquoteSymbol) unquote.invoke(field.value)
	else begin
		.invoke(field.value)
		.let { childTerm ->
			plusResolved(field.name to childTerm)
		}

fun Term.invoke(term: Term): Term =
	fold(term.fieldSeq) { invoke(it) }

fun Term.plusResolved(field: TermField) =
	if (!scope.isQuoted)
		if (!isEmpty && field.value.isEmpty) clear.plusMacro(field.name to this).invoke
		else plusMacro(field).invoke
	else plus(field)

val Term.begin get() =
	scope
		.copy(isShortQuoted = false)
		.let { scope ->
			Term(
				scope = scope,
				fieldList = empty.list(),
				termListDict = empty.symbolDict(),
				nodeOrNull = null,
				alternativesTermOrNull = null)
		}

fun Term.plus(field: TermField): Term =
	Term(
		scope = scope,
		fieldList = fieldList.add(field),
		termListDict = termListDict.update(field.name) {
			(this ?: empty.list()).add(field.value)
		},
		nodeOrNull = Node(this, field),
		alternativesTermOrNull = alternativesTermForPlusOrNull?.let { alternativesTerm ->
			ifOrNull(field.name == eitherSymbol) {
				field.value.nodeOrNull?.let { node ->
					ifOrNull(node.lhs.isEmpty) {
						alternativesTerm.plus(node.field)
					}
				}
			}
		})

val Term.isSimple
	get() =
		nodeOrNull != null && nodeOrNull.isSimple

fun Term.plus(term: Term) =
	fold(term.fieldSeq) { plus(it) }

val Term.alternativesTermForPlusOrNull
	get() =
		if (isEmpty) term()
		else alternativesTermOrNull

val Term.invoke
	get() =
		scope
			.dispatcher
			.at(this)
			?.let { body ->
				body.clear
					.set(body.scope.bindSelf(body.scope.bring(term(selfSymbol to this))))
					.invoke(body)
					.set(body.scope.bindSelf(body.scope.selfOrNull))
					.let { result -> scope.bring(result) }
			}
			.orIfNull { this }

val Term.self
	get() =
		scope.selfOrNull ?: scope.bring(term(selfSymbol))

fun term(name: Symbol) =
	term().plus(name to term())

fun term(name: String) =
	term(symbol(name))

fun Term.leafPlus(term: Term): Term =
	nodeOrNull?.leafPlus(term)?.term ?: plus(term)

fun term(vararg fields: TermField): Term =
	empty.term.fold(fields) { plus(it) }

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

fun Term.plusMacroHas(field: TermField): Term? =
	ifOrNull(!isEmpty && field.name == hasSymbol) {
		clear.set(scope.define(this has field.value))
	}

fun Term.plusMacroGives(field: TermField): Term? =
	ifOrNull(!isEmpty && field.name == givesSymbol) {
		clear.set(scope.define(this caseTo field.value))
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
		scope.bring(self)
	}

fun Term.plusMacroDo(field: TermField): Term? =
	ifOrNull(field.name == doSymbol) {
		invoke(field.value)
	}

fun Term.plusMacroComment(field: TermField): Term? =
	ifOrNull(field.name == commentSymbol) { this }

fun Term.plusMacroScript(field: TermField): Term? =
	ifOrNull(isEmpty && field.name == scriptSymbol) {
		clear.plus(field.value.scriptField)
	}

fun Term.plusMacroStringPlusString(field: TermField): Term? =
	simpleAtOrNull(stringSymbol)?.simpleNameOrNull?.let { lhsSymbol ->
		field.atOrNull(plusSymbol)?.simpleAtOrNull(stringSymbol)?.simpleNameOrNull?.let { rhsSymbol ->
			clear.plus(stringSymbol to term(lhsSymbol.plus(rhsSymbol)!!))
		}
	}

fun Term.plusMacroPrint(field: TermField): Term? =
	notNullIf(isEmpty && field.name == printSymbol) {
		println(field.value.script.code)
		clear
	}

fun Term.plusMacro(field: TermField): Term =
	null
		?: plusMacroUnquote(field)
		?: plusMacroEquals(field)
		?: plusMacroGives(field)
		?: plusMacroHas(field)
		?: plusMacroTest(field)
		?: plusMacroSelf(field)
		?: plusMacroGet(field)
		?: plusMacroLhs(field)
		?: plusMacroRhs(field)
		?: plusMacroDo(field)
		?: plusMacroComment(field)
		?: plusMacroScript(field)
		?: plusMacroStringPlusString(field)
		?: plusMacroPrint(field)
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
	scope.emptyTerm

fun Term.set(scope: Scope) =
	copy(scope = scope)

fun term(boolean: Boolean) =
	term(termField(boolean))

fun term(int: Int) =
	term(termField(int))

fun Term.invoke(termHasTerm: TermHasTerm) =
	set(scope.define(termHasTerm))

// === chained syntax

val Term.quote
	get() =
		set(scope.quote)

val Term.unquote
	get() =
		set(scope.unquote)

fun Term.plus(string: String, fn: Term.() -> Term): Term =
	if (string == "quote") plus(begin.quote.fn())
	else if (scope.isQuoted) plus(string to begin.fn())
	else if (string == "test") plusResolved("test" to begin.quote.fn())
	else if (string == "error") plusResolved("error" to begin.quote.fn())
	else if (string == "with") plus(begin.shortQuote.fn())
	else plusResolved(string to begin.fn())

fun Term.plus(string: String) =
	plus(string) { this }

val Term.descope: Term
	get() =
		empty.term.fold(fieldSeq) {
			plus(it.name to it.value.descope)
		}

fun Scope.bring(term: Term): Term =
	emptyTerm.fold(term.fieldSeq) { field ->
		plus(field.name to bring(field.value))
	}

val Term.scriptField: TermField
	get() =
		scriptSymbol to term().fold(fieldSeq) { plus(it.lineField) }

val Term.shortQuote
	get() =
		set(scope.shortQuote)