package leo19.typed

import leo.base.indexed
import leo13.EmptyStack
import leo13.LinkStack
import leo19.term.Term
import leo19.type.Field
import leo19.type.Struct
import leo19.type.Type
import leo19.type.contentOrNull
import leo19.type.layoutSize
import leo19.type.type
import leo19.type.structOrNull

data class TypedStruct(val term: Term, val struct: Struct)

fun Term.of(struct: Struct) = TypedStruct(this, struct)

fun Struct.indexedFieldOrNull(name: String): IndexedValue<Field>? =
	when (fieldStack) {
		is EmptyStack -> null
		is LinkStack -> fieldStack.link.let { link ->
			if (link.value.name == name) Struct(link.stack).layoutSize indexed link.value
			else Struct(link.stack).indexedFieldOrNull(name)
		}
	}

fun Type.indexedOrNull(name: String): IndexedValue<Type>? =
	contentOrNull?.structOrNull?.indexedFieldOrNull(name)?.run { index indexed type(value) }