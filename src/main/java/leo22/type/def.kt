package leo22.type

import leo22.dsl.*

val literalDef = literal(meta(text()), meta(number()))
val fieldDef = field(name(text()), rhs(type()))
val lineDef = line(choice(literalDef, fieldDef))
val structDef = struct(list(lineDef))
val choiceDef = meta(choice(list(lineDef)))
val recursiveDef = recursive(type())
val recurseDef = recurse(number())
val typeDef = type(choice(structDef, choiceDef, recursiveDef, recurseDef))

val nilType = type(struct())