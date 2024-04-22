.global main .equ PIN_OUTPUT, 1
.equ    REF_LED, 22
.equ    PWM_LED, 21
.equ    WAIT_TIME, 500

.text
.include "Wait.s"
.include "Hardware2.s"
.include "Binary.s"

main:
	BL  map_io
	MOV R0, #REF_LED  @ Select reference led
	MOV R1, #PIN_OUTPUT  @ Select output as function
	BL  set_pin_function @ Set pin to output
	CMP R0, #0   @ If return value < 0, exit
	BLT _exit
	MOV R0, #REF_LED  @ Select reference led
	MOV R1, #GPSET0  @ Turn on code
	BL  set_pin_value  @ Turn on pin/led

	@   Now set PWM_LED to output
	MOV R0, #PWM_LED  @ Select pwm led
	MOV R1, #PIN_OUTPUT  @ Select output as function
	BL  set_pin_function @ Set pin to output
	CMP R0, #0   @ If return value < 0, exit
	BLT _exit

	MOV R3, #100  @ We will use R3 as our counter

pwm_loop:
	MOV   R0, #PWM_LED  @ Select pwm led
	MOV   R1, #GPSET0  @ Turn on code
	BL    set_pin_value  @ Turn on led
	MOV   R0, #WAIT_TIME  @ Select time to wait
	BL    wait   @ wait
	MOV   R0, #PWM_LED  @ Select pwm led
	MOV   R1, #GPCLR0  @ Turn off code
	BL    set_pin_value  @ Turn off led
	MOV   R0, #WAIT_TIME  @ Select time to wait
	BL    wait   @ wait
	CMP   R3, #0   @ Check counter
	SUBGT R3, #1   @ Decrement counter if loops left
	BGT   pwm_loop  @ If counter > 0, repeat loop

_exit:
	BL     unmap_io
	MOV    R7, #SYS_EXIT  @ Select exit syscall
	SWI    0   @ Exit
	.data
	dev_mem: .asciz "/dev/mem"
	.align 4
	file_desc: .word 0x0       @ file descriptor

gpiobase:
	.word 0x0       @ address to which gpio is mapped

clockbase:
	.word 0x0
