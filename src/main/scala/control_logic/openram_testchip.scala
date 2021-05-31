// See README.md for license details.

package openram_testchip

import chisel3._
import chisel3.util._

class openram_testchip extends Module {
    val io = IO(new Bundle{
        val logical_analyzer_packet = Input(UInt(86.W))
        val gpio_packet = Input(UInt(56.W))
        val in_select = Input(Bool())
        val sram0_rw_in = Input(UInt(32.W))
        val sram0_r0_in = Input(UInt(32.W))
        val sram1_rw_in = Input(UInt(32.W))
        val sram1_ro_in = Input(UInt(32.W))
        val sram2_rw_in = Input(UInt(32.W))
        val sram3_rw_in = Input(UInt(32.W))
        val sram4_rw_in = Input(UInt(32.W))
        val sram5_rw_in = Input(UInt(64.W))
        val sram0_connections = Output(UInt(55.W))
        val sram1_connections = Output(UInt(55.W))
        val sram2_connections = Output(UInt(48.W))
        val sram3_connections = Output(UInt(46.W))
        val sram4_connections = Output(UInt(47.W))
        val sram5_connections = Output(UInt(83.W))
        val sram_data = Output(UInt(64.W))
    })

    val input = Reg(UInt(86.W))

    def getMask(bitWidth: Int): UInt = {
        val MOD = BigInt(1) << bitWidth
        val MASK = MOD - BigInt(1)
        MASK.U
    }

    input := Mux(io.in_select, io.gpio_packet, io.logical_analyzer_packet)

    val chip_select: UInt = input(85, 83)
    
    io.sram0_connections := getMask(55)
    io.sram1_connections := getMask(55)
    io.sram2_connections := getMask(48)
    io.sram3_connections := getMask(46)
    io.sram4_connections := getMask(47)
    io.sram5_connections := getMask(83)
    
    val csb0 = input(54)
    val web = input(53)

    switch(chip_select){
        is(0.U){
            io.sram0_connections := input.tail(31)
        }

        is(1.U){
            io.sram1_connections := input.tail(31)
        }

        is(2.U){
            io.sram2_connections := input.tail(38)
        }

        is(3.U){
            io.sram3_connections := input.tail(40)
        }
        
        is(4.U){
            io.sram4_connections := input.tail(39)
        }

        is(5.U){
            io.sram5_connections := input.tail(3)
        }
    }

    io.sram_data := 0.U
    //If operation is read
    when(web){
        switch(chip_select){
            
            is(0.U){
                io.sram_data := Mux(csb0, io.sram0_r0_in, io.sram0_rw_in)
            }

            is(1.U){
                io.sram_data :=  Mux(csb0, io.sram1_ro_in, io.sram1_rw_in)
            }

            is(2.U){
                io.sram_data := io.sram2_rw_in
            }

            is(3.U){
                io.sram_data := io.sram3_rw_in
            }

            is(4.U){
                io.sram_data := io.sram4_rw_in
            }
            
            is(5.U){
                io.sram_data := io.sram5_rw_in
            }
        }
    }
}

import chisel3.stage.ChiselStage

object openram_testchipDriver extends App {
  (new ChiselStage).emitVerilog(new openram_testchip)
}