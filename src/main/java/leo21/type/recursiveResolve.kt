package leo21.type

import leo13.map

val Recursive.resolve: Line
	get() =
		line.shift(0, this)

fun Type.shift(depth: Int, recursive: Recursive): Type =
	lineStack.map { line(recursive(extend(depth, recursive))) }.type

fun Choice.shift(depth: Int, recursive: Recursive): Choice =
	lineStack.map { shift(depth, recursive) }.choice

fun Recursive.shift(depth: Int, recursive: Recursive): Recursive =
	recursive(line.shift(depth.inc(), recursive))

fun Recurse.shift(depth: Int, recursive: Recursive): Line =
	if (index == depth) line(recursive(recursive.line.tail(this)))
	else line(this)

fun Line.shift(depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> line(field.shift(depth, recursive))
		is ChoiceLine -> line(choice.shift(depth, recursive))
		is ArrowLine -> this
		is RecursiveLine -> line(this.recursive.shift(depth, recursive))
		is RecurseLine -> recurse.shift(depth, recursive)
	}

fun Field.shift(depth: Int, recursive: Recursive): Field =
	name fieldTo rhs.shift(depth, recursive)

fun Type.extend(depth: Int, recursive: Recursive): Type =
	lineStack.map { extend(depth, recursive) }.type

fun Choice.extend(depth: Int, recursive: Recursive): Choice =
	lineStack.map { extend(depth, recursive) }.choice

fun Recursive.extend(depth: Int, recursive: Recursive): Recursive =
	recursive(line.extend(depth.inc(), recursive))

fun Recurse.extend(depth: Int, recursive: Recursive): Line =
	if (index == depth) recursive.line.tail(this)
	else line(this)

fun Line.extend(depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> line(field.extend(depth, recursive))
		is ChoiceLine -> line(choice.extend(depth, recursive))
		is ArrowLine -> this
		is RecursiveLine -> line(this.recursive.extend(depth, recursive))
		is RecurseLine -> recurse.extend(depth, recursive)
	}

fun Field.extend(depth: Int, recursive: Recursive): Field =
	name fieldTo rhs.extend(depth, recursive)

fun Type.tail(recurse: Recurse): Type =
	lineStack.map { line(recurse) }.type

fun Choice.tail(recurse: Recurse): Choice =
	lineStack.map { tail(recurse) }.choice

fun Recursive.tail(recurse: Recurse): Recursive =
	recursive(line(recurse))

fun Line.tail(recurse: Recurse): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> field.tail(recurse)
		is ChoiceLine -> line(choice.tail(recurse))
		is ArrowLine -> this
		is RecursiveLine -> line(recursive.tail(recurse))
		is RecurseLine -> this
	}

fun Field.tail(recurse: Recurse): Line =
	name lineTo rhs.tail(recurse)
