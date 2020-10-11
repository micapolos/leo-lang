package leo19.compiler

import leo.base.notNullIf
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.linkOrNull
import leo14.onlyLineOrNull
import leo14.rhsOrNull
import leo16.names.*
import leo19.type.Arrow
import leo19.type.ArrowType
import leo19.type.Case
import leo19.type.Field
import leo19.type.Type
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct

val Script.type: Type
	get() =
		choiceTypeOrNull ?: arrowTypeOrNull ?: structType

val Script.structType: Type
	get() =
		struct(*fieldList.toTypedArray())

val Script.choiceTypeOrNull: Type?
	get() =
		linkOrNull?.onlyLineOrNull?.fieldOrNull?.choiceTypeOrNull

val Script.arrowTypeOrNull: Type?
	get() =
		linkOrNull?.let { link ->
			link.line.fieldOrNull?.rhsOrNull(_giving)?.let { rhs ->
				ArrowType(Arrow(link.lhs.type, rhs.type))
			}
		}

val ScriptField.choiceTypeOrNull: Type?
	get() =
		notNullIf(string == _choice) {
			choice(*rhs.caseList.toTypedArray())
		}

val Script.fieldList: List<Field>
	get() =
		lineSeq.reverse.map { it.fieldOrNull!!.field }

val Script.caseList: List<Case>
	get() =
		lineSeq.reverse.map { it.fieldOrNull!!.case }

val ScriptField.field
	get() =
		string fieldTo rhs.type

val ScriptField.case
	get() =
		string caseTo rhs.type
