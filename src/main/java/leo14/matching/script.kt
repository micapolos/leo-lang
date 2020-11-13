package leo14.matching

import leo.base.mapFirstOrNull
import leo.base.notNullOrError
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.lineTo
import leo14.onlyLineOrNull
import leo14.script
import leo22.dsl.*

val ScriptLine.rhs: Script
	get() =
		fieldOrNull
			?.rhs
			.notNullOrError(script(this, "rhs" lineTo script()).toString())

fun <R : Any> ScriptLine.switch(vararg cases: Case<R>): R =
	rhs.onlyLineOrNull?.let { line ->
		stack(*cases).seq.mapFirstOrNull { matchOrNull(line) }
	}.notNullOrError(error(this, switch(*cases.map { it.name lineTo script("...") }.toTypedArray())).toString())

fun ScriptLine.get(name: String): ScriptLine =
	rhs.lineSeq.firstOrNull { it.name == name }
		.notNullOrError(error(this, get(text(name))).toString())
