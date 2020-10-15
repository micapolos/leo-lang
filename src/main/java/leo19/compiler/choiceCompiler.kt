package leo19.compiler

import leo.base.fold
import leo.base.map
import leo.base.notNullOrError
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.onlyLineOrNull
import leo19.typed.TypedChoice
import leo19.typed.emptyTypedChoice
import leo19.typed.fieldTo
import leo19.typed.plusNo
import leo19.typed.plusYes

data class ChoiceCompiler(
	val resolver: Resolver,
	val choice: TypedChoice
)

fun Resolver.choice(script: Script): TypedChoice =
	ChoiceCompiler(this, emptyTypedChoice)
		.fold(script.lineSeq.reverse.map { fieldOrNull!! }) { plus(it) }
		.choice

fun ChoiceCompiler.plus(scriptField: ScriptField): ChoiceCompiler =
	copy(choice = choicePlus(scriptField))

fun ChoiceCompiler.choicePlus(scriptField: ScriptField): TypedChoice =
	when (scriptField.string) {
		"yes" -> choicePlusYes(scriptField.rhs.onlyLineOrNull?.fieldOrNull.notNullOrError("not a field"))
		"no" -> choicePlusNo(scriptField.rhs.onlyLineOrNull?.fieldOrNull.notNullOrError("not a field"))
		else -> error("expected yes or no")
	}

fun ChoiceCompiler.choicePlusYes(field: ScriptField): TypedChoice =
	choice.plusYes(
		field.string fieldTo resolver.typed(field.rhs)
	)

fun ChoiceCompiler.choicePlusNo(field: ScriptField): TypedChoice =
	choice.plusNo(field.case)
