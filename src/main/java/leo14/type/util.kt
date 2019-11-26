package leo14.type

import leo.base.fold
import leo.base.notNullOrError
import leo13.*
import leo14.type.thunk.Scope
import leo14.type.thunk.TypeThunk
import leo14.type.thunk.with

val Type.isEmpty get() = structureOrNull?.isEmpty ?: false
val Type.isNative get() = (this is NativeType)
val Type.structureOrNull get() = (this as? StructureType)?.structure
val Type.listOrNull get() = (this as? ListType)?.list
val Type.choiceOrNull get() = (this as? ChoiceType)?.choice
val Type.actionOrNull get() = (this as? ActionType)?.action

fun reference(type: Type): Reference = TypeReference(type)
fun reference(int: Int): Reference = IntReference(int)
fun reference(recursive: Recursive): Reference = RecursiveReference(recursive)

val Stack<Type>.scope get() = Scope(this)
fun scope(vararg types: Type) = stack(*types).scope
operator fun Scope.plus(type: Type) = typeStack.push(type).scope
operator fun Scope.get(int: Int): Type = typeStack.get(int).notNullOrError("$this[$int]")

val nativeType: Type = NativeType
fun type(structure: Structure): Type = StructureType(structure)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(action: Action): Type = ActionType(action)
fun type(list: List): Type = ListType(list)

fun type(vararg fields: Field) = type(structure(*fields))
fun type(string: String, vararg strings: String) = type(structure(string, *strings))
fun Type.plusOrNull(field: Field) = structureOrNull?.plus(field)?.let(::type)

infix fun Reference.actionTo(rhs: Reference) = Action(this, rhs)
infix fun Type.actionTo(rhs: Type) = reference(this) actionTo reference(rhs)

val Stack<Field>.choice get() = Choice(this)
fun choice(vararg fields: Field) = stack(*fields).choice
fun choice(string: String, vararg strings: String) = choice().fold(string, strings) { plus(it) }
operator fun Choice.plus(field: Field) = fieldStack.push(field).choice
operator fun Choice.plus(string: String) = plus(string.field)

val Stack<Field>.structure get() = Structure(this)
fun structure(vararg fields: Field) = stack(*fields).structure
fun structure(string: String, vararg strings: String) = structure().fold(string, strings) { plus(it) }
operator fun Structure.plus(field: Field) = fieldStack.push(field).structure
operator fun Structure.plus(string: String) = plus(string.field)
val Structure.isEmpty get() = fieldStack.isEmpty
val Structure.split get() = fieldStack.splitOrNull?.run { first.structure to second }
val Structure.onlyFieldOrNull get() = fieldStack.onlyOrNull
val Structure.previousOrNull get() = fieldStack.linkOrNull?.stack?.structure
val Structure.lastFieldOrNull get() = fieldStack.linkOrNull?.value

fun list(field: Field) = List(field)

infix fun String.fieldTo(reference: Reference) = Field(this, reference)
infix fun String.fieldTo(type: Type) = this fieldTo reference(type)
val String.field get() = this fieldTo type()

fun recursive(type: Type) = Recursive(type)

fun Reference.thunk(scope: Scope): TypeThunk =
	when (this) {
		is TypeReference -> type.with(scope)
		is IntReference ->
			scope.typeStack.link.run {
				if (int == 0) value with scope
				else reference(int.dec()).thunk(stack.scope)
			}
		is RecursiveReference ->
			recursive.type with scope.plus(recursive.type)
	}
