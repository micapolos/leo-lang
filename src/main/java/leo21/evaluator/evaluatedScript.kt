package leo21.evaluator

import leo14.Script
import leo14.ScriptLine
import leo14.lambda.value.Value
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.switch
import leo14.lambda.value.value
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo21.prim.Prim
import leo21.prim.double
import leo21.prim.nilPrim
import leo21.prim.string
import leo21.type.Arrow
import leo21.type.ArrowLine
import leo21.type.Choice
import leo21.type.ChoiceType
import leo21.type.DoubleLine
import leo21.type.Field
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.StringLine
import leo21.type.Struct
import leo21.type.StructType
import leo21.type.Type
import leo21.type.isStatic
import leo21.type.linkOrNull
import leo21.type.script

fun script(value: Value<Prim>, type: Type): Script =
	when (type) {
		is StructType -> script(value, type.struct)
		is ChoiceType -> script(scriptLine(value, type.choice))
	}

fun script(value: Value<Prim>, struct: Struct): Script =
	struct.linkOrNull
		?.let { (lhsStruct, rhsLine) ->
			if (lhsStruct.isStatic)
				if (rhsLine.isStatic) script(value, lhsStruct).plus(scriptLine(value, rhsLine))
				else script(value(nilPrim), lhsStruct).plus(scriptLine(value, rhsLine))
			else
				if (rhsLine.isStatic) script(value, lhsStruct).plus(scriptLine(value(nilPrim), rhsLine))
				else value.pair.let { (lhsValue, rhsValue) ->
					script(lhsValue, lhsStruct).plus(scriptLine(rhsValue, rhsLine))
				}
		}
		?: script()

fun scriptLine(value: Value<Prim>, choice: Choice): ScriptLine =
	choice.linkOrNull!!.let { link ->
		value.switch(
			{ lhsValue -> scriptLine(lhsValue, link.tail) },
			{ rhsValue -> scriptLine(rhsValue, link.head) })
	}

fun scriptLine(value: Value<Prim>, line: Line): ScriptLine =
	when (line) {
		StringLine -> line(literal(value.native.string))
		DoubleLine -> line(literal(value.native.double))
		is FieldLine -> scriptLine(value, line.field)
		is ArrowLine -> scriptLine(line.arrow)
	}

fun scriptLine(value: Value<Prim>, field: Field): ScriptLine =
	field.name lineTo script(value, field.rhs)

fun scriptLine(arrow: Arrow): ScriptLine =
	"function" lineTo arrow.script