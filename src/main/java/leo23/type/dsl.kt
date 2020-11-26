package leo23.type

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

val booleanType: Type get() = BooleanType
val textType: Type get() = TextType
val numberType: Type get() = NumberType
fun params(vararg params: Type): List<Type> = listOf(*params)
infix fun List<Type>.arrowTo(type: Type): Type = ArrowType(this, type)
fun fields(vararg types: Type): PersistentList<Type> = persistentListOf(*types)
fun cases(vararg types: Type): PersistentList<Type> = persistentListOf(*types)
infix fun String.struct(fields: PersistentList<Type>): Type = StructType(this, fields)
val String.type: Type get() = this struct fields()
infix fun String.choice(cases: PersistentList<Type>): Type = ChoiceType(this, cases)
