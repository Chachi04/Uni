
.global main

.text
.include "Binary.s"
.include "Wait.s"

main:
	BL	map_io          	@ open /dev/mem and map hardware
    	BL	init_pins
@    	MVN	R0, #0          	@ Value to display
@    	BL	disp_num
        MVN     R0, #0
counter_blink:
        BL  disp_num                @ Display number
        MOV R1, R0
        MOV R0, 10
        BL wait
        MOV R1, R0

inc_counter_blink:
        cmp     R0, #1023  @ Compare to 1023
        ADDLT   R0, #1              @ if < 1023, increment counter
        MVNGE   R0, #0              @ if >= 1023, reset counter to 0
        BGE     check_exit          @ if >= 1023, decrement cycle counter and maybe exit
        BLT     counter_blink       @ ir < 1023, display number
check_exit:
        SUB SP 4
        MOV R0, SP
        MOV R1, 1
        BL read
        LDR R3, [R0]
        AND R3, #0b11111111
        cmp R3, #0x31
        BNE counter_blink
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