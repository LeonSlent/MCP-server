package br.edu.ifpr.mcp_server.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;

class BmiToolTest {
   private final BmiTool tool = new BmiTool();

   private String imc(double peso, double altura) {
      return tool.execute(Map.of("peso_kg",peso,"altura_m",altura));
   }

   @Test
   void deveCalcularPesoNormal() {
      assertEquals("IMC: 22.86 - Peso normal",imc(70,1.75));
   }

   @Test
   void deveClassificarCadaFaixa() {
      assertTrue(imc(50,1.75).endsWith("Abaixo do peso"));
      assertTrue(imc(85,1.75).endsWith("Sobrepeso"));
      assertTrue(imc(95,1.75).endsWith("Obesidade grau I"));
      assertTrue(imc(110,1.75).endsWith("Obesidade grau II"));
      assertTrue(imc(130,1.75).endsWith("Obesidade grau III"));
   }

   @Test
   void deveFalharComPesoInvalido() {
      assertThrows(IllegalArgumentException.class,() -> imc(0,1.75));
      assertThrows(IllegalArgumentException.class,() -> imc(-5,1.75));
   }

   @Test
   void deveFalharComAlturaInvalida() {
      assertThrows(IllegalArgumentException.class,() -> imc(70,0));
      assertThrows(IllegalArgumentException.class,() -> imc(70,3.5));
   }

   @Test
   void deveFalharSeFaltarParametro() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("peso_kg",70)));
   }

   @Test
   void runDeveRetornarErroMcpQuandoEntradaInvalida() {
      CallToolRequest request = CallToolRequest.builder("calcular_imc")
                                               .arguments(Map.of("peso_kg",-1,
                                                                 "altura_m",1.7))
                                               .build();
      CallToolResult  result  = tool.run(null,request);

      assertTrue(result.isError());
      assertTrue(((TextContent) result.content().get(0)).text()
                                                        .contains("peso"));
   }

   @Test
   void runDeveRetornarSucessoQuandoEntradaValida() {
      CallToolRequest request = CallToolRequest.builder("calcular_imc")
                                               .arguments(Map.of("peso_kg",70,
                                                                 "altura_m",1.75))
                                               .build();
      CallToolResult  result  = tool.run(null,request);

      assertFalse(result.isError());
   }
}
