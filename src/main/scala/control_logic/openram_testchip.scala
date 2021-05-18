// See README.md for license details.

package openram_testchip

import chisel3._
import chisel3.util.

class openram_testchip extends Module {
    val io = IO(new Bundle{
        val logical_analyzer_packet = Input(UInt(56.W)),
        val gpio_packet = Input(UInt(56.W)),
        val in_select = Input(Bool()),
        val sram0_rw_in = Input(UInt(32.W)),
        val sram0_r0_in = Input(UInt(32.W)),
        val sram1_rw_in = Input(UInt(32.W)),
        val sram1_ro_in = Input(UInt(32.W)),
        val sram0_connections = Output(UInt(55.W)),
        val sram1_connections = Output(UInt(55.W)),
        val sram_data = Output(UInt(32.W))
    })

    val input = Reg(UInt(55.W))

    val MOD = BigInt(1) << 55
    val MASK = MOD - BigInt(1)

    input := Mux(in_select, gpio_packet, logical_analyzer_packet)

    val chip_select = input(55)
    val csb0 = input(54)
    val web = input(53)

    switch(chip_select){
        is(0){
            sram0_connections := input
            sram1_connections := MASK
        }

        is(1){
            sram0_connections := MASK
            sram1_connections := input
        }
    }

    sram_data := 0.U
    //If operation is read
    when(web){
        sram_data := Mux(chip_select, Mux(csb0, sram1_ro_in, sram1_rw_in), Mux(csb0, sram0_r0_in, sram0_rw_in))
    }
}