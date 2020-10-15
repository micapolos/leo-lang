package leo19.compiler

import leo.base.fold
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.lineSeq
import leo14.plus
import leo14.script

data class TestCompiler(
	val resolver: Resolver,
	val script: Script
)

val Resolver.testCompiler get() = TestCompiler(this, script())

fun TestCompiler.plus(script: Script): TestCompiler =
	fold(script.lineSeq.reverse.map { it.fieldOrNull!! }) { plus(it) }

fun TestCompiler.plus(scriptField: ScriptField): TestCompiler =
	when (scriptField.string) {
		equalsKeyword -> plusEquals(scriptField.rhs)
		else -> plusRaw(scriptField)
	}

fun TestCompiler.plusEquals(rhs: Script): TestCompiler =
	resolver.testCompiler.also {
		resolver.testEquals(script, rhs)
	}

fun TestCompiler.plusRaw(scriptField: ScriptField): TestCompiler =
	copy(script = script.plus(scriptField))

val TestCompiler.end: Unit
	get() =
		if (!script.isEmpty) error("non empty type")
		else Unit
