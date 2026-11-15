package br.edu.ifpr.mcp_server.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

class CpfValidatorToolTest {
   private final CpfValidatorTool tool = new CpfValidatorTool();

   @Test
   void deveInformarCpfValido() {
      assertEquals("CPF válido.",tool.execute(Map.of("cpf","529.982.247-25")));
   }

   @Test
   void deveInformarCpfInvalido() {
      assertEquals("CPF inválido.",tool.execute(Map.of("cpf","529.982.247-24")));
   }

   @Test
   void deveFalharSeCpfAusenteOuEmBranco() {
      assertThrows(IllegalArgumentException.class,() -> tool.execute(Map.of()));
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("cpf"," ")));
   }
}
