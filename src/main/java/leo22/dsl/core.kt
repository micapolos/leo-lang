package leo22.dsl

import leo.base.get
import leo14.LiteralScriptLine
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.line
import leo14.lineSeq
import leo14.lineTo
import leo14.literal
import leo14.matching.Case
import leo14.matching.get
import leo14.matching.switch
import leo14.numberOrNull
import leo14.plus
import leo14.script
import leo14.stringOrNull

typealias Line = ScriptLine
typealias X = ScriptLine

fun _line(name: String, vararg lines: Line) = name lineTo script(*lines)
fun Line._get(name: String) = get(name)
fun <R : Any> Line.switch_(vararg cases: Case<R>) = switch(*cases)
fun <R> _case(name: String, fn: (ScriptLine) -> R) = Case(name, fn)

fun number(int: Int) = line(literal(int))
fun text(string: String) = line(literal(string))

val Line.int_ get() = (this as LiteralScriptLine).literal.numberOrNull!!.bigDecimal.intValueExact()
val Line.string_ get() = (this as LiteralScriptLine).literal.stringOrNull!!

fun Line.append_(line: Line): Line =
	fieldOrNull!!.let { field ->
		field.string lineTo field.rhs.plus(line)
	}

fun Line.at_(index: Int): Line =
	fieldOrNull!!.rhs.lineSeq.get(index)!!
