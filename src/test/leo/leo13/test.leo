define
	processor
	contains: function
		byte process
		gives: processor

	void processor
	of: processor
	gives: processor: function
		byte process
		gives: void processor

	processor
	ignore: space
	gives: processor: function
		byte process
		gives: given byte match
			space: given processor
			other
				given processor
				do: given byte process

void processor
do: process: byte: zero
do: process: byte: one
