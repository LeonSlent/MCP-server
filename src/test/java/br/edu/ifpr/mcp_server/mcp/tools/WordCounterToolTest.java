package br.edu.ifpr.mcp_server.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

class WordCounterToolTest {
   private final WordCounterTool tool = new WordCounterTool();

   @Test
   void deveContarUmaLinha() {
      assertEquals("Palavras: 3 | Caracteres: 15 | " +
                   "Caracteres sem espaços: 13 | Linhas: 1",
                   tool.execute(Map.of("texto","Olá mundo cruel")));
   }

   @Test
   void deveContarVariasLinhas() {
      assertEquals("Palavras: 3 | Caracteres: 5 | " +
                   "Caracteres sem espaços: 3 | Linhas: 2",
                   tool.execute(Map.of("texto","a b\nc")));
   }

   @Test
   void deveIgnorarEspacosRepetidos() {
      assertEquals("Palavras: 2 | Caracteres: 8 | " +
                   "Caracteres sem espaços: 4 | Linhas: 1",
                   tool.execute(Map.of("texto","ab    cd")));
   }

   @Test
   void deveFalharSeTextoAusenteOuEmBranco() {
      assertThrows(IllegalArgumentException.class,() -> tool.execute(Map.of()));
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("texto","   ")));
   }
}
