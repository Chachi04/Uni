@ Binary.s
@ Adam Watkins, Richard Verhoeven
@ December 2021

@ Wire the Gertboard as follows: 						
@   On J2				On J3 			
@		GP0 -> 			B12 			
@		GP1 ->			B11 			
@		GP4 ->			B10 			
@		GP7 ->			B9 				
@		GP17-GP24 ->	B8-B3 			
@		GP25 -> 		B1				
@   Put output jumpers on B1-B12 (Output side of U3,U4,U5)	


.equ        SYS_EXIT,   0x1

.equ		GPCLR0, 0x28			@ Value to set a GPIO pin to OFF
.equ		GPSET0, 0x1C			@ Value to set a GPIO pin to ON

.equ 		DISP_MASK, 0b1001110001000000000010011100 @ TASK: Create a constant for the mask
                                    



@ Functions


@@@@@ set_pin_function : function to set pin n to output in GPSELm
@ Parameters: 
@   R0: pin number
@   R1: code of function (see chapter 6 BCM2837 manual for codes)
@ Returns:
@   R0:  -1 on error
set_pin_function:
				@ successively subtract 10 from R1 until <10
				@ store offset of of GPSELm in R5
		STMFD	SP!, {R2-R7, LR}	@ Save registers
		BL	    check_pin			@ Check if pin number OK
		CMP	    R0, #0				@ If returned value is 
		BLT	    exit_set_func		@   <0 (error) then exit function
				@ Find GPSELm from pin number
		CMP	    R0,#9				@ GPSEL0?
		MOV	    R5,#0
		BHI	    gpsel1
		BAL	    clr_GPSELm			@ Offset of GPSEL0 (= GPIO base address) in R5 = 0
gpsel1:	
        SUB	    R0, #10
		CMP	    R0, #9				@ GPSEL1?
		BHI	    gpsel2
		MOV	    R5,#4
		BAL	    clr_GPSELm			@ Offset of GPSEL1 in R5
gpsel2:	
        SUB	    R0, #10
		MOV	    R5,#8				@ Offset of GPSEL2 in R5
clr_GPSELm:	
        MOV	    R3, R0				@ Save R0
		MOV	    R6, #0b111			@ Load R6 with bit pattern for BIC to clear 3 bits
		MOV	    R2, #3
		MUL	    R7, R3, R2
		MOV	    R6, R6, LSL R7		@ Shift R6 R3*3 times left
clear:	LDR	    R3, =gpiobase
		LDR	    R2, [R3]			@ Load base memory address of gpio
		LDR	    R4, [R2,R5]			@ Load current contents of GPSELm
		BIC	    R4, R4, R6			@ Clear the 3 bits corresponding to the pin
		MOV	    R1, R1, LSL R7		@ Shift R1 (function) R7 times left
		ORR	    R4, R1				@ Set the function bits in R4 ( R4 is a copy of the
		                            @ Current GPSELm register with the 3 bits corresponding
								    @ To pin R1 set o 0)
		LDR	    R3, =gpiobase
		LDR	    R3, [R3]			@ Load memory base address of gpio
		STR	    R4, [R3,R5]			@ Copy R4 to GPSELm
exit_set_func:	
        LDMFD	SP!,{R2-R7, LR}	    @ Restore R2-R7 and LR
		MOV     PC, LR					@ R0 still holds GPIO base address if no error occurred..


@@@@ set_pin_value:	function to set the pin
@ Paramters:
@   R0: 	pin number
@   R1: 	offset of GPSET0/GPCLR0
@ Returns:
@   R0:		returns: -1 if error
set_pin_value:				
		STMFD	SP!, {R3, LR}
		MOV	    R3, R0				@ save R0
		BL	    check_pin			@ check if pin number is correct
		CMP	    R0, #0				@ if value returned from check_pin
		BLT	    ret_set				@     <1 then return (error)
		MOV	    R3, #1				@ will be shifted until pin position R1
		MOV	    R3, R3, LSL R0		@ shift by R0 bits left
		LDR	    R2, =gpiobase		@ gpio base address in memory
		LDR	    R2, [R2]
		STR	    R3, [R2,R1]			@ set or clear pin; R0+R2 address of GPSET/CLR0
								    @ notice that register is Write only
ret_set:
        LDMFD	SP!,{R3, LR}
		MOV     PC, LR              @ return - R0 still holds base address if no error occurred


@@@@ check_pin :	check if pin number is legal
@ Parameters:
@   R0: pin number
@ Return
@   R0: -1 if illegal
check_pin:
		CMP	    R0, #1				@ GPIO 0 and 1 not available
		BLS	    error				@ GPIO2 is connected to GP0, GPIO3 to GP1
		CMP	    R0, #5				@ GPIO5 not available
		BEQ	    error
		CMP	    R0, #6				@ GPIO6 not available
		BEQ	    error
		CMP	    R0, #16				@ GPIO 12, 13, 16 not available - R1 >16?
		BHI	    next_check			@ GPIO 14 and 15 set for UART so leave alone
		CMP	    R0, #11				@ GPIO# <12?
		BLS	    next_check
		BAL	    error
next_check:	
        CMP	    R0, #21				@ GPIO19, 20 and 21 not available
		BHI	    check_next
		CMP	    R0, #18
		BLS	    check_next
		BAL	    error
check_next:
        CMP	    R0, #27				@ GPIO27 is connected to GP21
		BEQ	    ret
		CMP	    R0, #25				@ no pins over 25
		BHI	    error
		MOV     PC, LR
error:	MOV	    R0, #-1				@ signal error to caller
ret:	MOV     PC, LR
