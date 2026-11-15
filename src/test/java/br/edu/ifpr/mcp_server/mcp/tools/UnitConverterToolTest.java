package br.edu.ifpr.mcp_server.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

class UnitConverterToolTest {
   private final UnitConverterTool tool = new UnitConverterTool();

   @Test
   void deveConverterQuilometrosParaMetros() {
      assertEquals("1.0000 km = 1000.0000 m",
                   tool.execute(Map.of("valor",1,"unidade_origem","km",
                                       "unidade_destino","m")));
   }

   @Test
   void deveConverterCelsiusParaFahrenheit() {
      assertEquals("100.0000 c = 212.0000 f",
                   tool.execute(Map.of("valor",100,"unidade_origem","C",
                                       "unidade_destino","F")));
   }

   @Test
   void deveFalharComUnidadeDesconhecida() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("valor",1,"unidade_origem","xx",
                                             "unidade_destino","m")));
   }

   @Test
   void deveFalharComCategoriasDiferentes() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("valor",1,"unidade_origem","kg",
                                             "unidade_destino","m")));
   }

   @Test
   void deveFalharComValorNaoNumerico() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("valor","abc","unidade_origem","km",
                                             "unidade_destino","m")));
   }

   @Test
   void deveFalharSeFaltarParametro() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("valor",1,"unidade_origem","km")));
   }
}
