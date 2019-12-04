package leo14.typed.compiler

import leo14.Keyword
import leo14.leonardoScript
import leo14.stringIn
import leo14.typed.key
import leo14.typed.staticTyped
import leo14.typed.type
import leo14.typed.typed

fun <T> Context<T>.preludeMemory(): Memory<T> =
	memory(
		nothingMemoryItem(),
		leonardoMemoryItem())

fun <T> Context<T>.nothingMemoryItem(): MemoryItem<T> =
	item(
		key(type(Keyword.NOTHING stringIn language)),
		value(memoryBinding(typed(), isAction = false)))

fun <T> Context<T>.leonardoMemoryItem(): MemoryItem<T> =
	item(
		key(type(Keyword.LEONARDO stringIn language)),
		value(memoryBinding(leonardoScript.staticTyped(literalCompile), isAction = false)))