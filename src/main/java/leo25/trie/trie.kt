package leo25.trie

internal const val MAX_BRANCHING_FACTOR = 32
internal const val LOG_MAX_BRANCHING_FACTOR = 5
internal const val MAX_BRANCHING_FACTOR_MINUS_ONE = MAX_BRANCHING_FACTOR - 1
internal const val ENTRY_SIZE = 2
internal const val MAX_SHIFT = 30

/**
 * Gets trie index segment of the specified [index] at the level specified by [shift].
 *
 * `shift` equal to zero corresponds to the root level.
 * For each lower level `shift` increments by [LOG_MAX_BRANCHING_FACTOR].
 */
internal fun indexSegment(index: Int, shift: Int): Int =
	(index shr shift) and MAX_BRANCHING_FACTOR_MINUS_ONE

private fun <K, V> Array<Any?>.insertEntryAtIndex(keyIndex: Int, key: K, value: V): Array<Any?> {
	val newBuffer = arrayOfNulls<Any?>(this.size + ENTRY_SIZE)
	this.copyInto(newBuffer, endIndex = keyIndex)
	this.copyInto(newBuffer, keyIndex + ENTRY_SIZE, startIndex = keyIndex, endIndex = this.size)
	newBuffer[keyIndex] = key
	newBuffer[keyIndex + 1] = value
	return newBuffer
}

@ExperimentalStdlibApi
private fun Array<Any?>.replaceEntryWithNode(keyIndex: Int, nodeIndex: Int, newNode: Trie<*, *>): Array<Any?> {
	val newNodeIndex = nodeIndex - ENTRY_SIZE  // place where to insert new node in the new buffer
	val newBuffer = arrayOfNulls<Any?>(this.size - ENTRY_SIZE + 1)
	this.copyInto(newBuffer, endIndex = keyIndex)
	this.copyInto(newBuffer, keyIndex, startIndex = keyIndex + ENTRY_SIZE, endIndex = nodeIndex)
	newBuffer[newNodeIndex] = newNode
	this.copyInto(newBuffer, newNodeIndex + 1, startIndex = nodeIndex, endIndex = this.size)
	return newBuffer
}

private fun <K, V> Array<Any?>.replaceNodeWithEntry(nodeIndex: Int, keyIndex: Int, key: K, value: V): Array<Any?> {
	val newBuffer = this.copyOf(this.size + 1)
	newBuffer.copyInto(newBuffer, nodeIndex + 2, nodeIndex + 1, this.size)
	newBuffer.copyInto(newBuffer, keyIndex + 2, keyIndex, nodeIndex)
	newBuffer[keyIndex] = key
	newBuffer[keyIndex + 1] = value
	return newBuffer
}

private fun Array<Any?>.removeEntryAtIndex(keyIndex: Int): Array<Any?> {
	val newBuffer = arrayOfNulls<Any?>(this.size - ENTRY_SIZE)
	this.copyInto(newBuffer, endIndex = keyIndex)
	this.copyInto(newBuffer, keyIndex, startIndex = keyIndex + ENTRY_SIZE, endIndex = this.size)
	return newBuffer
}

private fun Array<Any?>.removeNodeAtIndex(nodeIndex: Int): Array<Any?> {
	val newBuffer = arrayOfNulls<Any?>(this.size - 1)
	this.copyInto(newBuffer, endIndex = nodeIndex)
	this.copyInto(newBuffer, nodeIndex, startIndex = nodeIndex + 1, endIndex = this.size)
	return newBuffer
}

@ExperimentalStdlibApi
class Trie<K, V>(
	private var dataMap: Int,
	private var nodeMap: Int,
	buffer: Array<Any?>
) {

	internal var buffer: Array<Any?> = buffer
		private set

	/** Returns number of entries stored in this trie node (not counting subnodes) */
	internal fun entryCount(): Int = dataMap.countOneBits()

	// here and later:
	// positionMask — an int in form 2^n, i.e. having the single bit set, whose ordinal is a logical position in buffer


	/** Returns true if the data bit map has the bit specified by [positionMask] set, indicating there's a data entry in the buffer at that position. */
	internal fun hasEntryAt(positionMask: Int): Boolean {
		return dataMap and positionMask != 0
	}

	/** Returns true if the node bit map has the bit specified by [positionMask] set, indicating there's a subtrie node in the buffer at that position. */
	private fun hasNodeAt(positionMask: Int): Boolean {
		return nodeMap and positionMask != 0
	}

	/** Gets the index in buffer of the data entry key corresponding to the position specified by [positionMask]. */
	internal fun entryKeyIndex(positionMask: Int): Int {
		return ENTRY_SIZE * (dataMap and (positionMask - 1)).countOneBits()
	}

	/** Gets the index in buffer of the subtrie node entry corresponding to the position specified by [positionMask]. */
	internal fun nodeIndex(positionMask: Int): Int {
		return buffer.size - 1 - (nodeMap and (positionMask - 1)).countOneBits()
	}

	/** Retrieves the buffer element at the given [keyIndex] as key of a data entry. */
	private fun keyAtIndex(keyIndex: Int): K {
		@Suppress("UNCHECKED_CAST")
		return buffer[keyIndex] as K
	}

	/** Retrieves the buffer element next to the given [keyIndex] as value of a data entry. */
	private fun valueAtKeyIndex(keyIndex: Int): V {
		@Suppress("UNCHECKED_CAST")
		return buffer[keyIndex + 1] as V
	}

	/** Retrieves the buffer element at the given [nodeIndex] as subtrie node. */
	internal fun nodeAtIndex(nodeIndex: Int): Trie<K, V> {
		@Suppress("UNCHECKED_CAST")
		return buffer[nodeIndex] as Trie<K, V>
	}

	private fun insertEntryAt(positionMask: Int, key: K, value: V): Trie<K, V> {
//        assert(!hasEntryAt(positionMask))

		val keyIndex = entryKeyIndex(positionMask)
		val newBuffer = buffer.insertEntryAtIndex(keyIndex, key, value)
		return Trie(dataMap or positionMask, nodeMap, newBuffer)
	}

	private fun updateValueAtIndex(keyIndex: Int, value: V): Trie<K, V> {
//        assert(buffer[keyIndex + 1] !== value)

		val newBuffer = buffer.copyOf()
		newBuffer[keyIndex + 1] = value
		return Trie(dataMap, nodeMap, newBuffer)
	}

	/** The given [newNode] must not be a part of any persistent map instance. */
	private fun updateNodeAtIndex(nodeIndex: Int, positionMask: Int, newNode: Trie<K, V>): Trie<K, V> {
//        assert(buffer[nodeIndex] !== newNode)
		val newNodeBuffer = newNode.buffer
		if (newNodeBuffer.size == 2 && newNode.nodeMap == 0) {
			if (buffer.size == 1) {
//                assert(dataMap == 0 && nodeMap xor positionMask == 0)
				newNode.dataMap = nodeMap
				return newNode
			}

			val keyIndex = entryKeyIndex(positionMask)
			val newBuffer = buffer.replaceNodeWithEntry(nodeIndex, keyIndex, newNodeBuffer[0], newNodeBuffer[1])
			return Trie(dataMap xor positionMask, nodeMap xor positionMask, newBuffer)
		}

		val newBuffer = buffer.copyOf(buffer.size)
		newBuffer[nodeIndex] = newNode
		return Trie(dataMap, nodeMap, newBuffer)
	}

	private fun removeNodeAtIndex(nodeIndex: Int, positionMask: Int): Trie<K, V>? {
//        assert(hasNodeAt(positionMask))
		if (buffer.size == 1) return null

		val newBuffer = buffer.removeNodeAtIndex(nodeIndex)
		return Trie(dataMap, nodeMap xor positionMask, newBuffer)
	}

	private fun bufferMoveEntryToNode(
		keyIndex: Int, positionMask: Int, newKeyHash: Int,
		newKey: K, newValue: V, shift: Int
	): Array<Any?> {
		val storedKey = keyAtIndex(keyIndex)
		val storedKeyHash = storedKey.hashCode()
		val storedValue = valueAtKeyIndex(keyIndex)
		val newNode = makeNode(
			storedKeyHash, storedKey, storedValue,
			newKeyHash, newKey, newValue, shift + LOG_MAX_BRANCHING_FACTOR
		)

		val nodeIndex = nodeIndex(positionMask) + 1 // place where to insert new node in the current buffer

		return buffer.replaceEntryWithNode(keyIndex, nodeIndex, newNode)
	}


	private fun moveEntryToNode(
		keyIndex: Int, positionMask: Int, newKeyHash: Int,
		newKey: K, newValue: V, shift: Int
	): Trie<K, V> {
//        assert(hasEntryAt(positionMask))
//        assert(!hasNodeAt(positionMask))

		val newBuffer = bufferMoveEntryToNode(keyIndex, positionMask, newKeyHash, newKey, newValue, shift)
		return Trie(dataMap xor positionMask, nodeMap or positionMask, newBuffer)
	}

	/** Creates a new TrieNode for holding two given key value entries */
	private fun makeNode(
		keyHash1: Int, key1: K, value1: V,
		keyHash2: Int, key2: K, value2: V, shift: Int
	): Trie<K, V> {
		if (shift > MAX_SHIFT) {
//            assert(key1 != key2)
			// when two key hashes are entirely equal: the last level subtrie node stores them just as unordered list
			return Trie(0, 0, arrayOf(key1, value1, key2, value2))
		}

		val setBit1 = indexSegment(keyHash1, shift)
		val setBit2 = indexSegment(keyHash2, shift)

		if (setBit1 != setBit2) {
			val nodeBuffer = if (setBit1 < setBit2) {
				arrayOf(key1, value1, key2, value2)
			} else {
				arrayOf(key2, value2, key1, value1)
			}
			return Trie((1 shl setBit1) or (1 shl setBit2), 0, nodeBuffer)
		}
		// hash segments at the given shift are equal: move these entries into the subtrie
		val node = makeNode(keyHash1, key1, value1, keyHash2, key2, value2, shift + LOG_MAX_BRANCHING_FACTOR)
		return Trie<K, V>(0, 1 shl setBit1, arrayOf(node))
	}

	private fun removeEntryAtIndex(keyIndex: Int, positionMask: Int): Trie<K, V>? {
//        assert(hasEntryAt(positionMask))
		if (buffer.size == ENTRY_SIZE) return null
		val newBuffer = buffer.removeEntryAtIndex(keyIndex)
		return Trie(dataMap xor positionMask, nodeMap, newBuffer)
	}

	private fun collisionRemoveEntryAtIndex(i: Int): Trie<K, V>? {
		if (buffer.size == ENTRY_SIZE) return null
		val newBuffer = buffer.removeEntryAtIndex(i)
		return Trie(0, 0, newBuffer)
	}

	private fun collisionKeyIndex(key: Any?): Int {
		for (i in 0 until buffer.size step ENTRY_SIZE) {
			if (key == keyAtIndex(i)) return i
		}
		return -1
	}

	private fun collisionContainsKey(key: K): Boolean {
		return collisionKeyIndex(key) != -1
	}

	private fun collisionGet(key: K): V? {
		val keyIndex = collisionKeyIndex(key)
		return if (keyIndex != -1) valueAtKeyIndex(keyIndex) else null
	}

	private fun collisionPut(key: K, value: V): Trie<K, V>? {
		val keyIndex = collisionKeyIndex(key)
		if (keyIndex != -1) {
			if (value === valueAtKeyIndex(keyIndex)) {
				return null
			}
			val newBuffer = buffer.copyOf()
			newBuffer[keyIndex + 1] = value
			return Trie(0, 0, newBuffer)
		}
		val newBuffer = buffer.insertEntryAtIndex(0, key, value)
		return Trie(0, 0, newBuffer)
	}

	private fun collisionRemove(key: K): Trie<K, V>? {
		val keyIndex = collisionKeyIndex(key)
		if (keyIndex != -1) {
			return collisionRemoveEntryAtIndex(keyIndex)
		}
		return this
	}

	private fun collisionRemove(key: K, value: V): Trie<K, V>? {
		val keyIndex = collisionKeyIndex(key)
		if (keyIndex != -1 && value == valueAtKeyIndex(keyIndex)) {
			return collisionRemoveEntryAtIndex(keyIndex)
		}
		return this
	}

	fun containsKey(keyHash: Int, key: K, shift: Int): Boolean {
		val keyPositionMask = 1 shl indexSegment(keyHash, shift)

		if (hasEntryAt(keyPositionMask)) { // key is directly in buffer
			return key == keyAtIndex(entryKeyIndex(keyPositionMask))
		}
		if (hasNodeAt(keyPositionMask)) { // key is in node
			val targetNode = nodeAtIndex(nodeIndex(keyPositionMask))
			if (shift == MAX_SHIFT) {
				return targetNode.collisionContainsKey(key)
			}
			return targetNode.containsKey(keyHash, key, shift + LOG_MAX_BRANCHING_FACTOR)
		}

		// key is absent
		return false
	}

	fun get(keyHash: Int, key: K, shift: Int): V? {
		val keyPositionMask = 1 shl indexSegment(keyHash, shift)

		if (hasEntryAt(keyPositionMask)) { // key is directly in buffer
			val keyIndex = entryKeyIndex(keyPositionMask)

			if (key == keyAtIndex(keyIndex)) {
				return valueAtKeyIndex(keyIndex)
			}
			return null
		}
		if (hasNodeAt(keyPositionMask)) { // key is in node
			val targetNode = nodeAtIndex(nodeIndex(keyPositionMask))
			if (shift == MAX_SHIFT) {
				return targetNode.collisionGet(key)
			}
			return targetNode.get(keyHash, key, shift + LOG_MAX_BRANCHING_FACTOR)
		}

		// key is absent
		return null
	}

	fun put(keyHash: Int, key: K, value: @UnsafeVariance V, shift: Int): Trie<K, V>? {
		val keyPositionMask = 1 shl indexSegment(keyHash, shift)

		if (hasEntryAt(keyPositionMask)) { // key is directly in buffer
			val keyIndex = entryKeyIndex(keyPositionMask)

			if (key == keyAtIndex(keyIndex)) {
				if (valueAtKeyIndex(keyIndex) === value) return null

				return updateValueAtIndex(keyIndex, value)
			}
			return moveEntryToNode(keyIndex, keyPositionMask, keyHash, key, value, shift)
		}
		if (hasNodeAt(keyPositionMask)) { // key is in node
			val nodeIndex = nodeIndex(keyPositionMask)

			val targetNode = nodeAtIndex(nodeIndex)
			return if (shift == MAX_SHIFT) {
				targetNode.collisionPut(key, value) ?: return null
			} else {
				targetNode.put(keyHash, key, value, shift + LOG_MAX_BRANCHING_FACTOR) ?: return null
			}
		}

		// no entry at this key hash segment
		return insertEntryAt(keyPositionMask, key, value)
	}

	fun remove(keyHash: Int, key: K, shift: Int): Trie<K, V>? {
		val keyPositionMask = 1 shl indexSegment(keyHash, shift)

		if (hasEntryAt(keyPositionMask)) { // key is directly in buffer
			val keyIndex = entryKeyIndex(keyPositionMask)

			if (key == keyAtIndex(keyIndex)) {
				return removeEntryAtIndex(keyIndex, keyPositionMask)
			}
			return this
		}
		if (hasNodeAt(keyPositionMask)) { // key is in node
			val nodeIndex = nodeIndex(keyPositionMask)

			val targetNode = nodeAtIndex(nodeIndex)
			val newNode = if (shift == MAX_SHIFT) {
				targetNode.collisionRemove(key)
			} else {
				targetNode.remove(keyHash, key, shift + LOG_MAX_BRANCHING_FACTOR)
			}
			return replaceNode(targetNode, newNode, nodeIndex, keyPositionMask)
		}

		// key is absent
		return this
	}

	private fun replaceNode(targetNode: Trie<K, V>, newNode: Trie<K, V>?, nodeIndex: Int, positionMask: Int) = when {
		newNode == null ->
			removeNodeAtIndex(nodeIndex, positionMask)
		targetNode !== newNode ->
			updateNodeAtIndex(nodeIndex, positionMask, newNode)
		else ->
			this
	}

	companion object {
		val EMPTY = Trie<Nothing, Nothing>(0, 0, emptyArray())
	}
}

