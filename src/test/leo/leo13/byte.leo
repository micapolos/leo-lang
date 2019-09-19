define: byte has
	first: bit
	second: bit
	third: bit
	fourth: bit
	fifth: bit
	sixth: bit
	seventh: bit
	eight: bit

define
	byte: zero
	gives: byte
		first: bit: zero
		second: bit: zero
		third: bit: zero
		fourth: bit: zero
		fifth: bit: zero
		sixth: bit: zero
		seventh: bit: zero
		eight: bit: zero

define
	inverse: byte
	gives: byte
		first: given inverse byte first bit negate
		second: given inverse byte second bit negate
		third: given inverse byte third bit negate
		fourth: given inverse byte fourth bit negate
		fifth: given inverse byte fifth bit negate
		sixth: given inverse byte sixth bit negate
		seventh: given inverse byte seventh bit negate
		eight: given inverse byte eight bit negate
