
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
        MOV     R3, #10             @ We will repeat 10 times
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
        cmp     R3, #0              @ Compare the cycle counter to 0
        SUBGT   R3, #1              @ Decrement if above 0
        BGT     counter_blink       @ Go back to display loop if above 0
exit:
		BL	    unmap_io        	@ unmap and close hardware addresses
		MOV	    R7, #SYS_EXIT
		SWI	    0