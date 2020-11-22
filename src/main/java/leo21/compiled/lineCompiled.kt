package leo21.compiled

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.nativeTerm
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.ArrowLine
import leo21.type.ChoiceLine
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.NumberLine
import leo21.type.RecurseLine
import leo21.type.RecursiveLine
import leo21.type.StringLine
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberLine
import leo21.type.resolve
import leo21.type.stringLine

data class LineCompiled(val term: Term<Prim>, val line: Line)

infix fun Term<Prim>.of(line: Line) = LineCompiled(this, line)

fun <R> LineCompiled.switch(
	stringFn: (StringCompiled) -> R,
	doubleFn: (DoubleCompiled) -> R,
	fieldFn: (FieldCompiled) -> R,
	choiceFn: (ChoiceCompiled) -> R,
	arrowFn: (ArrowCompiled) -> R
): R =
	when (line) {
		StringLine -> stringFn(StringCompiled(term))
		NumberLine -> doubleFn(DoubleCompiled(term))
		is ChoiceLine -> choiceFn(ChoiceCompiled(term, line.choice))
		is FieldLine -> fieldFn(FieldCompiled(term, line.field))
		is ArrowLine -> arrowFn(ArrowCompiled(term, line.arrow))
		is RecursiveLine -> term.of(line.recursive.resolve).switch(stringFn, doubleFn, fieldFn, choiceFn, arrowFn)
		is RecurseLine -> null!!
	}

fun <R> LineCompiled.switch(
	stringFn: (StringCompiled) -> R,
	doubleFn: (DoubleCompiled) -> R,
	fieldFn: (FieldCompiled) -> R,
	choiceFn: (ChoiceCompiled) -> R,
	arrowFn: (ArrowCompiled) -> R,
	recursiveFn: (RecursiveCompiled) -> R,
	recurseFn: (RecurseCompiled) -> R
): R =
	when (line) {
		StringLine -> stringFn(StringCompiled(term))
		NumberLine -> doubleFn(DoubleCompiled(term))
		is ChoiceLine -> choiceFn(ChoiceCompiled(term, line.choice))
		is FieldLine -> fieldFn(FieldCompiled(term, line.field))
		is ArrowLine -> arrowFn(ArrowCompiled(term, line.arrow))
		is RecursiveLine -> recursiveFn(RecursiveCompiled(term, line.recursive))
		is RecurseLine -> recurseFn(RecurseCompiled(term, line.recurse))
	}

fun line(string: String) = LineCompiled(nativeTerm(prim(string)), stringLine)
fun line(double: Double) = LineCompiled(nativeTerm(prim(double)), numberLine)
infix fun String.lineTo(rhs: Compiled) = LineCompiled(rhs.term, this lineTo rhs.type)
fun line(arrowCompiled: ArrowCompiled) = LineCompiled(arrowCompiled.term, line(arrowCompiled.arrow))
fun line(choiceCompiled: ChoiceCompiled) = LineCompiled(choiceCompiled.termOrNull!!, line(choiceCompiled.choice))
fun line(recursiveCompiled: RecursiveCompiled) = LineCompiled(recursiveCompiled.term, line(recursiveCompiled.recursive))
infix fun Type.does(compiled: Compiled) = line(fn(compiled.term).of(this arrowTo compiled.type))

fun lineCompiled(literal: Literal): LineCompiled =
	when (literal) {
		is StringLiteral -> line(literal.string)
		is NumberLiteral -> line(literal.number.bigDecimal.toDouble())
	}

val LineCompiled.stringCompiledOrNull: StringCompiled?
	get() =
		switch({ it }, { null }, { null }, { null }, { null })

val LineCompiled.doubleCompiledOrNull: DoubleCompiled?
	get() =
		switch({ null }, { it }, { null }, { null }, { null })

val LineCompiled.fieldCompiledOrNull: FieldCompiled?
	get() =
		switch({ null }, { null }, { it }, { null }, { null })

val LineCompiled.choiceCompiledOrNull: ChoiceCompiled?
	get() =
		switch({ null }, { null }, { null }, { it }, { null })

val LineCompiled.arrowCompiledOrNull: ArrowCompiled?
	get() =
		switch({ null }, { null }, { null }, { null }, { it })

val LineCompiled.rhsOrNull: Compiled? get() = fieldCompiledOrNull?.rhsCompiled

val FieldCompiled.lineCompiled: LineCompiled
	get() =
		term of line(field)