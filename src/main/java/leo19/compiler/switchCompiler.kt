package leo19.compiler

import leo.base.fold
import leo.base.map
import leo.base.notNullOrError
import leo.base.reverse
import leo13.topOrNull
import leo14.Script
import leo14.ScriptField
import leo14.fieldOrNull
import leo14.lineSeq
import leo19.type.Arrow
import leo19.type.field
import leo19.type.struct
import leo19.type.structOrNull
import leo19.typed.fieldTo
import leo19.typed.nullTyped

data class SwitchCompiler(
	val resolver: Resolver,
	val switchBuilder: SwitchBuilder
)

fun Resolver.switchCompiler(switchBuilder: SwitchBuilder) =
	SwitchCompiler(this, switchBuilder)

fun SwitchCompiler.plus(script: Script) =
	fold(script.lineSeq.reverse.map { fieldOrNull.notNullOrError("not a case") }) { plus(it) }

fun SwitchCompiler.plus(scriptField: ScriptField): SwitchCompiler =
	switchBuilder.remainingCaseStack.topOrNull.notNullOrError("switch exhausted").let { case ->
		copy(switchBuilder = switchBuilder.plus(
			scriptField.string fieldTo resolver
				.plus(binding(case.type.structOrNull!!))
				.compiler(nullTyped)
				.plus(scriptField.rhs)
				.typed))
	}
