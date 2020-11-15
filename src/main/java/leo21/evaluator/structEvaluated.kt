package leo21.evaluator

import leo13.Link
import leo13.linkTo
import leo14.lambda.value.Value
import leo14.lambda.value.pair
import leo14.lambda.value.plus
import leo21.prim.Prim
import leo21.type.Struct
import leo21.type.isStatic
import leo21.type.linkOrNull
import leo21.type.plus

data class StructEvaluated(val value: Value<Prim>, val struct: Struct)

infix fun Value<Prim>.of(struct: Struct) = StructEvaluated(this, struct)

fun StructEvaluated.plus(rhs: LineEvaluated): StructEvaluated =
	(if (struct.isStatic)
		if (rhs.line.isStatic) nilValue
		else rhs.value
	else
		if (rhs.line.isStatic) value
		else value.plus(rhs.value)) of struct.plus(rhs.line)

val StructEvaluated.linkOrNull: Link<StructEvaluated, LineEvaluated>?
	get() =
		struct.linkOrNull?.let { structLink ->
			(if (structLink.tail.isStatic)
				if (structLink.head.isStatic) nilValue to nilValue
				else nilValue to value
			else
				if (structLink.head.isStatic) value to nilValue
				else value.pair { lhs, rhs -> lhs to rhs }).let { (lhs, rhs) ->
				lhs.of(structLink.tail) linkTo rhs.of(structLink.head)
			}
		}
