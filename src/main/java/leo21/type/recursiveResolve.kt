package leo21.type

import leo13.map

val Recursive.resolve: Type
	get() =
		type.shift(0, this)

fun Type.shift(depth: Int, recursive: Recursive): Type =
	when (this) {
		is StructType -> type(struct.shift(depth, recursive))
		is ChoiceType -> type(choice.shift(depth, recursive))
		is RecursiveType -> type(this.recursive.shift(depth, recursive))
		is RecurseType -> recurse.shift(depth, recursive)
	}

fun Struct.shift(depth: Int, recursive: Recursive): Struct =
	lineStack.map { shift(depth, recursive) }.struct

fun Choice.shift(depth: Int, recursive: Recursive): Choice =
	lineStack.map { shift(depth, recursive) }.choice

fun Recursive.shift(depth: Int, recursive: Recursive): Recursive =
	recursive(type.shift(depth.inc(), recursive))

fun Recurse.shift(depth: Int, recursive: Recursive): Type =
	if (index == depth) type(recursive(recursive.type.tail(this)))
	else type(this)

fun Line.shift(depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		DoubleLine -> this
		is FieldLine -> line(field.shift(depth, recursive))
		is ArrowLine -> this
	}

fun Field.shift(depth: Int, recursive: Recursive): Field =
	name fieldTo type(recursive(rhs.extend(depth, recursive)))

fun Type.extend(depth: Int, recursive: Recursive): Type =
	when (this) {
		is StructType -> type(struct.extend(depth, recursive))
		is ChoiceType -> type(choice.extend(depth, recursive))
		is RecursiveType -> type(this.recursive.extend(depth, recursive))
		is RecurseType -> recurse.extend(depth, recursive)
	}

fun Struct.extend(depth: Int, recursive: Recursive): Struct =
	lineStack.map { extend(depth, recursive) }.struct

fun Choice.extend(depth: Int, recursive: Recursive): Choice =
	lineStack.map { extend(depth, recursive) }.choice

fun Recursive.extend(depth: Int, recursive: Recursive): Recursive =
	recursive(type.extend(depth.inc(), recursive))

fun Recurse.extend(depth: Int, recursive: Recursive): Type =
	if (index == depth) recursive.type.tail(this)
	else type(this)

fun Line.extend(depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		DoubleLine -> this
		is FieldLine -> line(field.extend(depth, recursive))
		is ArrowLine -> this
	}

fun Field.extend(depth: Int, recursive: Recursive): Field =
	name fieldTo rhs.extend(depth, recursive)

fun Type.tail(recurse: Recurse): Type =
	when (this) {
		is StructType -> type(struct.tail(recurse))
		is ChoiceType -> type(choice.tail(recurse))
		is RecursiveType -> type(recursive.tail(recurse))
		is RecurseType -> this
	}

fun Struct.tail(recurse: Recurse): Struct =
	lineStack.map { tail(recurse) }.struct

fun Choice.tail(recurse: Recurse): Choice =
	lineStack.map { tail(recurse) }.choice

fun Recursive.tail(recurse: Recurse): Recursive =
	recursive(type(recurse))

fun Line.tail(recurse: Recurse): Line =
	when (this) {
		StringLine -> this
		DoubleLine -> this
		is FieldLine -> field.tail(recurse)
		is ArrowLine -> this
	}

fun Field.tail(recurse: Recurse): Line =
	name lineTo type(recurse)
