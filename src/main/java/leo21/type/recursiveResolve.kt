package leo21.type

import leo13.indexed
import leo13.map

val Recursive.resolve: Line
	get() =
		line.shift(0, this)

fun Type.shift(depth: Int, recursive: Recursive): Type =
	lineStack.indexed.map { line(recursive(value.extend(index, depth, recursive))) }.type

fun Choice.shift(depth: Int, recursive: Recursive): Choice =
	lineStack.map { shift(depth, recursive) }.choice

fun Recursive.shift(depth: Int, recursive: Recursive): Recursive =
	recursive(line.shift(depth.inc(), recursive))

//fun Recurse.shift(depth: Int, recursive: Recursive): Line =
//	if (index == depth) line(recursive(recursive.line.tail(this)))
//	else line(this)

fun Line.shift(depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> line(field.shift(depth, recursive))
		is ChoiceLine -> line(choice.shift(depth, recursive))
		is ArrowLine -> this
		is RecursiveLine -> line(this.recursive.shift(depth, recursive))
		is RecurseLine -> null!!//recurse.shift(depth, recursive)
	}

fun Field.shift(depth: Int, recursive: Recursive): Field =
	name fieldTo rhs.shift(depth, recursive)

fun Type.extend(lineIndex: Int, depth: Int, recursive: Recursive): Type =
	lineStack.map { extend(lineIndex, depth, recursive) }.type

fun Choice.extend(lineIndex: Int, depth: Int, recursive: Recursive): Choice =
	lineStack.map { extend(lineIndex, depth, recursive) }.choice

fun Recursive.extend(lineIndex: Int, depth: Int, recursive: Recursive): Recursive =
	recursive(line.extend(lineIndex, depth.inc(), recursive))

fun Recurse.extend(lineIndex: Int, depth: Int, recursive: Recursive): Line =
	if (index == depth) recursive.line.tail(lineIndex, this)
	else line(this)

fun Line.extend(lineIndex: Int, depth: Int, recursive: Recursive): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> line(field.extend(lineIndex, depth, recursive))
		is ChoiceLine -> line(choice.extend(lineIndex, depth, recursive))
		is ArrowLine -> this
		is RecursiveLine -> line(this.recursive.extend(lineIndex, depth, recursive))
		is RecurseLine -> recurse.extend(lineIndex, depth, recursive)
	}

fun Field.extend(lineIndex: Int, depth: Int, recursive: Recursive): Field =
	name fieldTo rhs.extend(lineIndex, depth, recursive)

fun Type.tail(lineIndex: Int, recurse: Recurse): Type =
	lineStack.indexed.map {
		if (index == lineIndex) line(recurse)
		else value
	}.type

fun Choice.tail(lineIndex: Int, recurse: Recurse): Choice =
	lineStack.map { tail(lineIndex, recurse) }.choice

fun Recursive.tail(recurse: Recurse): Recursive =
	recursive(line(recurse))

fun Line.tail(lineIndex: Int, recurse: Recurse): Line =
	when (this) {
		StringLine -> this
		NumberLine -> this
		is FieldLine -> field.tail(lineIndex, recurse)
		is ChoiceLine -> line(choice.tail(lineIndex, recurse))
		is ArrowLine -> this
		is RecursiveLine -> line(recursive.tail(recurse))
		is RecurseLine -> this
	}

fun Field.tail(lineIndex: Int, recurse: Recurse): Line =
	name lineTo rhs.tail(lineIndex, recurse)
