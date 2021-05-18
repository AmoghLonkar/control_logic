import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec

import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

class OpenramTestChipTester extends FreeSpec with ChiselScalatestTester {
    
  "OpenramTestChip should write to SRAM 0" in {
    test(new openram_testchip) { dut =>
    1'b0, 1'b0, 1'b0, 4'd15, 8'd1, 32'd1, 1'b0, 8'd0
            val packet = BigInt("1E020000000200", 16)
            dut.io.logical_analyzer_packet.poke(packet.U)
            dut.io.gpio_packet.poke(0.U)
            dut.io.in_select.poke(false.B)
            clock.step()
            dut.io.sram0_connections.expect(packet.U)
            dut.io.sram1_connections.expect((scala.math.pow(2, 55)).U)
    }  
  }

  "OpenramTestChip should write to SRAM 1" in {
    
  }

  "OpenramTestChip should read from R/W Port in SRAM 0" in {
    
  }

  "OpenramTestChip should read from RO Port in SRAM 0" in {
    
  }

  "OpenramTestChip should read from R/W Port in SRAM 1" in {
    
  }

  "OpenramTestChip should read from RO Port in SRAM 1" in {
    
  }
}
