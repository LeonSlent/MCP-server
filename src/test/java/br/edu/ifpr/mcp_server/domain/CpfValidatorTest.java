package br.edu.ifpr.mcp_server.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CpfValidatorTest {
   @Test
   void deveAceitarCpfValidoComPontuacao() {
      assertTrue(CpfValidator.isValid("529.982.247-25"));
   }

   @Test
   void deveAceitarCpfValidoSemPontuacao() {
      assertTrue(CpfValidator.isValid("52998224725"));
   }

   @Test
   void deveRejeitarDigitoVerificadorErrado() {
      assertFalse(CpfValidator.isValid("529.982.247-24"));
   }

   @Test
   void deveRejeitarDigitosRepetidos() {
      assertFalse(CpfValidator.isValid("111.111.111-11"));
   }

   @Test
   void deveRejeitarTamanhoIncorreto() {
      assertFalse(CpfValidator.isValid("123"));
   }

   @Test
   void deveRejeitarTextoSemNumeros() {
      assertFalse(CpfValidator.isValid("abc"));
   }
}
