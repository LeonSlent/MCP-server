package br.edu.ifpr.mcp_server.mcp.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class ToolArgumentsTest {
   @Test
   void requireStringDeveRetornarOValor() {
      assertEquals("abc",ToolArguments.requireString(Map.of("k","abc"),"k"));
   }

   @Test
   void requireStringDeveFalharSeAusenteOuEmBranco() {
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.requireString(Map.of(),"k"));
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.requireString(Map.of("k","  "),"k"));
   }

   @Test
   void requireNumberDeveAceitarInteiroEDecimal() {
      assertEquals(3.0,ToolArguments.requireNumber(Map.of("k",3),"k"));
      assertEquals(2.5,ToolArguments.requireNumber(Map.of("k",2.5),"k"));
   }

   @Test
   void requireNumberDeveFalharSeAusenteOuNaoNumerico() {
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.requireNumber(Map.of(),"k"));
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.requireNumber(Map.of("k","x"),"k"));
   }

   @Test
   void requireIntDeveAceitarInteiroEFalharComDecimal() {
      assertEquals(10,ToolArguments.requireInt(Map.of("k",10),"k"));
      assertEquals(10,ToolArguments.requireInt(Map.of("k",10.0),"k"));
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.requireInt(Map.of("k",10.5),"k"));
   }

   @Test
   void optionalBooleanDeveUsarPadraoQuandoAusente() {
      assertFalse(ToolArguments.optionalBoolean(Map.of(),"k",false));
      assertTrue(ToolArguments.optionalBoolean(Map.of(),"k",true));
   }

   @Test
   void optionalBooleanDeveLerValorOuFalharSeInvalido() {
      assertTrue(ToolArguments.optionalBoolean(Map.of("k",true),"k",false));
      assertThrows(IllegalArgumentException.class,
                   () -> ToolArguments.optionalBoolean(Map.of("k","sim"),"k",false));
   }
}
