package leo19.typed

import leo19.term.Term
import leo19.type.Struct

data class TypedStruct(val term: Term, val struct: Struct)

fun Term.of(struct: Struct) = TypedStruct(this, struct)