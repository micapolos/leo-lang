package leo14.matching

import leo.base.mapFirstOrNull
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.onlyLineOrNull
import leo14.script

val Script.rhs: Script
	get() =
		onlyLineOrNull!!.fieldOrNull!!.rhs

fun <R : Any> Script.switch(vararg cases: Case<R>): R =
	rhs.onlyLineOrNull!!.let { line ->
		stack(*cases).seq.mapFirstOrNull { matchOrNull(line) }!!
	}

fun Script.get(name: String): Script =
	script(rhs.lineSeq.firstOrNull { it.name == name }!!)
