.global main

.equ	PIN_OUTPUT,	1
.equ	PWM_LED,	21
.equ	WAIT_TIME,	500

.text
.include "Wait.s"
.include "Hardware2.s"
.include "Binary.s"




main:
	BL	map_io

	@ We select the PWM_LED to be the one controlled
	MOV	R0, #PWM_LED		@ Select pwm led
	MOV	R1, #PIN_OUTPUT		@ Select output as function
	BL	set_pin_function	@ Set pin to output
	CMP	R0, #0			@ If return value < 0, exit
	BLT	_exit


	BL	print_output		@ ask for brightness

	LDR	R0, =input		@ Select input address
	MOV	R1, #3			@ We want to read one byte
	BL	read
	LDR 	R0, =input		@ Select input address
	LDRB	R0, [R0]		@ Load byte read
	AND	R0, #0b11111111		@ Mask so that only one by is present
	SUB	R0, #0x31		@ Substract '1' so we get a number in the range [0, 4]
	CMP	R0, #0			@ Make sure it's positive
	BLT	_exit
	CMP	R0, #4			@ Make sure it's not larger than 4
	BGT	_exit
	LDR 	R1, =brightness		@ Load address of brightness arra
	ADD	R1, R0, LSL #2		@ Add offset * 4 to array base
	LDR	R4, [R1]		@ Load brightness value from array
	MOV	R5, #1000		@ Set R1 to max bightness
	SUB	R5, R4			@ Substract brightness to obtain off time

	MOV	R3, #10			@ We will use R3 as our counter

pwm_loop:
	MOV	R0, #PWM_LED		@ Select pwm led
	MOV	R1, #GPSET0		@ Turn on code
	BL	set_pin_value		@ Turn on led
	MOV	R0, R4			@ Select time to wait
	BL	wait			@ wait
	MOV	R0, #PWM_LED		@ Select pwm led
	MOV	R1, #GPCLR0		@ Turn off code
	BL	set_pin_value		@ Turn off led
	MOV	R0, R5		@ Select time to wait
	BL	wait			@ wait
	CMP	R3, #0			@ Check counter
	SUBGT	R3, #1			@ Decrement counter if loops left
	BGT	pwm_loop		@ If counter > 0, repeat loop 

_exit:
	BL	unmap_io	@ unmap the io
	MOV	R7, #SYS_EXIT	@ Select exist syscall
	SWI	0		@ Ask for syscall


@ Funcitons

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

@@@@ print_output: prints output

print_output:
	STMFD	SP!, {R0-R2, R7, LR}	@ Push used registers to stack
	LDR	R1, =output		@ Select output string
	MOV	R2, #output_len		@ Select output string length
	MOV	R7, #4			@ Select write syscall
	MOV	R0, #1			@ Select STDOUT
	SWI	0			@ Ask for syscall
	LDMFD	SP!, {R0-R2, R7, LR}	@ Restore registers
	MOV	PC, LR



.data
input:		.space	3
output:		.asciz	"Enter a brightness level between 1 and 5: "
.equ		output_len, 42
dev_mem:	.asciz	"/dev/mem"
.align 4
file_desc:	.word	0x0			    @ file descriptor
gpiobase:	.word	0x0			    @ address to which gpio is mapped
clockbase: 	.word	0x0

brightness:	
		.word	200, 400, 600, 800, 1000
