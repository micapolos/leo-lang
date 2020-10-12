package leo19.typed

import leo13.get
import leo13.toList
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo19.term.ArrayTerm
import leo19.term.IntTerm
import leo19.type.ArrowType
import leo19.type.ChoiceType
import leo19.type.StructType
import leo19.type.isSimple

val Typed.script: Script
	get() =
		when (type) {
			is StructType ->
				type.struct.fieldStack.toList()
					.zip((term as ArrayTerm).stack.toList())
					.map { (field, term) ->
						(field.name fieldTo term.of(field.type)).scriptLine
					}
					.let { script(*it.toTypedArray()) }
			is ChoiceType ->
				(if (type.choice.isSimple) (term as IntTerm).int
				else ((term as ArrayTerm).stack.get(1) as IntTerm).int).let { index ->
					type.choice.caseStack.get(index)!!.let { case ->
						script(case.name lineTo term.of(case.type).script)
					}

				}
			is ArrowType -> null
		}!!

val TypedField.scriptLine: ScriptLine
	get() =
		name lineTo typed.script