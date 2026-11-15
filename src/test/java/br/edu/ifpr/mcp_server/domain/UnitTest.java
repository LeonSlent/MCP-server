package br.edu.ifpr.mcp_server.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UnitTest {
   private static final double DELTA = 0.0001;

   @Test
   void deveConverterComprimento() {
      assertEquals(1000.0,Unit.KILOMETER.convertTo(Unit.METER,1),DELTA);
      assertEquals(2.5,Unit.CENTIMETER.convertTo(Unit.METER,250),DELTA);
      assertEquals(1609.344,Unit.MILE.convertTo(Unit.METER,1),DELTA);
   }

   @Test
   void deveConverterMassa() {
      assertEquals(0.45359237,Unit.POUND.convertTo(Unit.KILOGRAM,1),DELTA);
      assertEquals(500.0,Unit.KILOGRAM.convertTo(Unit.GRAM,0.5),DELTA);
   }

   @Test
   void deveConverterTemperatura() {
      assertEquals(212.0,Unit.CELSIUS.convertTo(Unit.FAHRENHEIT,100),DELTA);
      assertEquals(0.0,Unit.FAHRENHEIT.convertTo(Unit.CELSIUS,32),DELTA);
      assertEquals(273.15,Unit.CELSIUS.convertTo(Unit.KELVIN,0),DELTA);
   }

   @Test
   void deveEncontrarUnidadeIgnorandoMaiusculasEEspacos() {
      assertEquals(Unit.KILOMETER,Unit.fromSymbol(" KM "));
   }

   @Test
   void deveFalharComUnidadeDesconhecida() {
      assertThrows(IllegalArgumentException.class,() -> Unit.fromSymbol("xyz"));
   }

   @Test
   void deveFalharAoConverterCategoriasDiferentes() {
      assertThrows(IllegalArgumentException.class,
                   () -> Unit.KILOGRAM.convertTo(Unit.METER,1));
   }
}
