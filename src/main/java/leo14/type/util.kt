package leo14.type

import leo.base.fold
import leo.base.notNullOrError
import leo13.*

val Type.isEmpty get() = structureOrNull?.isEmpty ?: false
val Type.isNative get() = (this is NativeType)
val Type.structureOrNull get() = (this as? StructureType)?.structure
val Type.choiceOrNull get() = (this as? ChoiceType)?.choice
val Type.actionOrNull get() = (this as? ActionType)?.action
val Type.recursiveOrNull get() = (this as? RecursiveType)?.recursive

fun reference(type: Type): Reference = TypeReference(type)
fun reference(index: Index): Reference = IndexReference(index)

val Stack<Type>.scope get() = Scope(this)
fun scope(vararg types: Type) = stack(*types).scope
operator fun Scope.plus(type: Type) = typeStack.push(type).scope
operator fun Scope.get(index: Index): Type = typeStack.get(index).notNullOrError("$this[$index]")

val nativeType: Type = NativeType
fun type(structure: Structure): Type = StructureType(structure)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(action: Action): Type = ActionType(action)
fun type(recursive: Recursive): Type = RecursiveType(recursive)

fun type(vararg fields: Field) = type(structure(*fields))
fun type(string: String, vararg strings: String) = type(structure(string, *strings))
fun Type.plusOrNull(field: Field) = structureOrNull?.plus(field)?.let(::type)

infix fun Reference.actionTo(rhs: Reference) = Action(this, rhs)
infix fun Type.actionTo(rhs: Type) = reference(this) actionTo reference(rhs)

val Stack<Option>.choice get() = Choice(this)
fun choice(vararg options: Option) = stack(*options).choice
fun choice(string: String, vararg strings: String) = choice().fold(string, strings) { plus(it) }
operator fun Choice.plus(option: Option) = optionStack.push(option).choice
operator fun Choice.plus(string: String) = plus(string.option)

val Stack<Field>.structure get() = Structure(this)
fun structure(vararg fields: Field) = stack(*fields).structure
fun structure(string: String, vararg strings: String) = structure().fold(string, strings) { plus(it) }
operator fun Structure.plus(field: Field) = fieldStack.push(field).structure
operator fun Structure.plus(string: String) = plus(string.field)
val Structure.isEmpty get() = fieldStack.isEmpty

infix fun String.fieldTo(reference: Reference) = Field(this, reference)
infix fun String.fieldTo(type: Type) = this fieldTo reference(type)
val String.field get() = this fieldTo type()

infix fun String.optionTo(reference: Reference) = Option(this, reference)
infix fun String.optionTo(type: Type) = this optionTo reference(type)
val String.option get() = this optionTo type()

fun recursive(type: Type) = Recursive(type)

infix fun Reference.with(scope: Scope) = Thunk(this, scope)
fun thunk(type: Type) = reference(type) with scope()

fun Reference.type(scope: Scope) =
	when (this) {
		is TypeReference -> type
		is IndexReference -> scope[index]
	}
