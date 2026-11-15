package br.edu.ifpr.mcp_server.mcp.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class PasswordGeneratorToolTest {
   private final PasswordGeneratorTool tool = new PasswordGeneratorTool();

   private boolean hasSymbol(String password) {
      return password.chars().anyMatch(c -> PasswordGeneratorTool.SYMBOLS.indexOf(c) >= 0);
   }

   @Test
   void deveGerarSenhaComOTamanhoPedidoEGrupos() {
      // repete várias vezes porque a senha é aleatória
      for (int i = 0; i < 100; i++) {
         String password = tool.execute(Map.of("tamanho",12));

         assertEquals(12,password.length());
         assertTrue(password.chars().anyMatch(Character::isLowerCase));
         assertTrue(password.chars().anyMatch(Character::isUpperCase));
         assertTrue(password.chars().anyMatch(Character::isDigit));
         assertFalse(hasSymbol(password));
      }
   }

   @Test
   void deveIncluirSimbolosQuandoPedido() {
      for (int i = 0; i < 100; i++) {
         String password = tool.execute(Map.of("tamanho",8,
                                               "incluir_simbolos",true));
         assertEquals(8,password.length());
         assertTrue(hasSymbol(password));
      }
   }

   @Test
   void deveFalharComTamanhoForaDoLimite() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("tamanho",7)));
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("tamanho",65)));
   }

   @Test
   void deveFalharComTamanhoNaoInteiroOuAusente() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("tamanho",10.5)));
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of()));
   }

   @Test
   void deveFalharComIncluirSimbolosInvalido() {
      assertThrows(IllegalArgumentException.class,
                   () -> tool.execute(Map.of("tamanho",10,
                                             "incluir_simbolos","sim")));
   }
}
