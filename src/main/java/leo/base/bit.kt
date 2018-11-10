package leo.base

enum class Bit {
  ZERO,
  ONE;

  override fun toString() = int.toString()
}

val Bit.int
  get() =
    ordinal

val Bit.char
  get() =
    when (this) {
      Bit.ZERO -> '0'
      Bit.ONE -> '1'
    }

val Bit.neg
  get() =
    when (this) {
      Bit.ZERO -> Bit.ONE
      Bit.ONE -> Bit.ZERO
    }

fun Bit.and(bit: Bit) =
    when (this) {
      Bit.ZERO ->
        when (bit) {
          Bit.ZERO -> Bit.ZERO
          Bit.ONE -> Bit.ZERO
        }
      Bit.ONE ->
        when (bit) {
          Bit.ZERO -> Bit.ZERO
          Bit.ONE -> Bit.ONE
        }
    }

fun Bit.or(bit: Bit) =
    when (this) {
      Bit.ZERO ->
        when (bit) {
          Bit.ZERO -> Bit.ZERO
          Bit.ONE -> Bit.ONE
        }
      Bit.ONE ->
        when (bit) {
          Bit.ZERO -> Bit.ONE
          Bit.ONE -> Bit.ONE
        }
    }

fun Bit.xor(bit: Bit) =
    when (this) {
      Bit.ZERO ->
        when (bit) {
          Bit.ZERO -> Bit.ZERO
          Bit.ONE -> Bit.ONE
        }
      Bit.ONE ->
        when (bit) {
          Bit.ZERO -> Bit.ONE
          Bit.ONE -> Bit.ZERO
        }
    }

val Int.bitOrNull
  get() =
    when (this) {
      0 -> Bit.ZERO
      1 -> Bit.ONE
      else -> null
    }

val Int.bit
  get() =
    if (this == 0) Bit.ZERO
    else Bit.ONE

fun <V> Bit.ifZero(value: V, fn: (V) -> V): V =
    when (this) {
      Bit.ZERO -> fn(value)
      Bit.ONE -> value
    }

fun Appendable.append(bit: Bit) =
    append(bit.char)
