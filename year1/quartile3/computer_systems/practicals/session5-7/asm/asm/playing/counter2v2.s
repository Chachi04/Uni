
.global main

.text
.include "Binary.s"
.include "Wait.s"
.include "Init_pins.s"
.include "Hardware2.s"

main:
	BL	map_io          	@ open /dev/mem and map hardware
    	BL	init_pins
@    	MOV	R0, #0          	@ Value to display
@    	BL	disp_num
        MOV     R0, #0
counter_blink:
        MOV R1, R0
        BL  disp_num                @ Display number
        MOV R0, #10
        BL wait
        MOV R0, R1

inc_counter_blink:
	LDR	R1, =1023
        cmp     R0, R1	         	  @ Compare to 1023
        ADDLT   R0, #1              @ if < 1023, increment counter
        MOVGE   R0, #0              @ if >= 1023, reset counter to 0
        BGE     check_exit          @ if >= 1023, decrement cycle counter and maybe exit
        BLT     counter_blink       @ ir < 1023, display number
check_exit:
	LDR R0, =input
        MOV R1, #3
        BL read
	LDR R0, =input
        LDR R3, [R0]
        AND R3, #0b11111111
        cmp R3, #0x31
	MOVEQ R0, #0
        BEQ counter_blink
exit:
		BL	    unmap_io        	@ unmap and close hardware addresses
		MOV	    R7, #SYS_EXIT
		SWI	    0

@@@@ read: read a string from keyboard and store in variable
@ Parameters:
@   R0: address of where to store string
@   R1: number of characters to store
@ Returns:
@   none
read:
        STMFD SP!, {R7, LR}     	@ Push used registers and LR to stack
        MOV R2, R1                  @ TASK: Move number of characters(R1) to R2
        MOV R1, R0                  @ Task: Move address of input string(R0) to R1
        MOV R7, #3                  @ TASK: Put the Syscall number in R?
        MOV R0, #0                  @ TASK: Put the keyboard STDIN in R?
        SWI 0						@ TASK: Uncomment this line to make the syscall
        LDMFD SP!, {R7, LR}     	@ Restore used registers (update SP with !)
        MOV  PC, LR

.data
input: 		.space 3
dev_mem:	.asciz	"/dev/mem"
.align 4
file_desc:	.word	0x0			    @ file descriptor
gpiobase:	.word	0x0			    @ address to which gpio is mapped
clockbase: 	.word	0x0
disp_bits:
    .word   0b100             		@ bits[0]: GPIO2  represents bit 0 (GP0 on GB)
    .word   0b1000             		@ bits[1]: GPIO3  represents bit 1 (GP1 on GB)
	.word	0b10000
	.word	0b1000000
	.word	0b100000000000000000
	.word	0b1000000000000000000
	.word	0x8000000
	.word	0b10000000000000000000000
	.word	0b100000000000000000000000
	.word	0b1000000000000000000000000
