package vm3

fun checkIndex(size: Int, index: Int) {
	if (index < 0 || index >= size) error("checkIndex($size, $index)")
}