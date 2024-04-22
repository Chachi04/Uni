.global main

.text
.include "Binary.s"
.include "Wait.s"
.include "Init_pins.s"
.include "Hardware2.s"

main:
	BL  map_io           @ open /dev/mem and map hardware
	BL  init_pins
	@   MVN R0, #0           @ Value to display
	@   BL disp_num
	MVN R0, #0
	MOV R3, #10             @ We will repeat 10 times

counter_blink:
	BL  disp_num                @ Display number
	MOV R1, R0
	MOV R0, #10
	BL  wait
	MOV R0, R1

inc_counter_blink:
	LDR   R1, =1023
	cmp   R0, R1    @ Compare to 1023
	ADDLT R0, #1              @ if < 1023, increment counter
	MVNGE R0, #0              @ if >= 1023, reset counter to 0
	BGE   check_exit          @ if >= 1023, decrement cycle counter and maybe exit
	BLT   counter_blink       @ ir < 1023, display number

check_exit:
	cmp   R3, #0              @ Compare the cycle counter to 0
	SUBGT R3, #1              @ Decrement if above 0
	BGT   counter_blink       @ Go back to display loop if above 0

exit:
	BL  unmap_io         @ unmap and close hardware addresses
	MOV R7, #SYS_EXIT
	SWI 0

	.data
	dev_mem: .asciz "/dev/mem"
	.align 4
	file_desc: .word 0x0       @ file descriptor

gpiobase:
	.word 0x0       @ address to which gpio is mapped

clockbase:
	.word 0x0

disp_bits:
	.word 0b100               @ bits[0]: GPIO2  represents bit 0 (GP0 on GB)
	.word 0b1000               @ bits[1]: GPIO3  represents bit 1 (GP1 on GB)
	.word 0b10000
	.word 0b1000000
	.word 0b100000000000000000
	.word 0b1000000000000000000
	.word 0x8000000
	.word 0b10000000000000000000000
	.word 0b100000000000000000000000
	.word 0b1000000000000000000000000
