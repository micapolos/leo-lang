package leo14.matching

import leo.base.mapFirstOrNull
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.onlyLineOrNull

val ScriptLine.rhs: Script
	get() =
		fieldOrNull!!.rhs

fun <R : Any> ScriptLine.switch(vararg cases: Case<R>): R =
	rhs.onlyLineOrNull!!.let { line ->
		stack(*cases).seq.mapFirstOrNull { matchOrNull(line) }!!
	}

fun ScriptLine.get(name: String): ScriptLine =
	rhs.lineSeq.firstOrNull { it.name == name }!!
