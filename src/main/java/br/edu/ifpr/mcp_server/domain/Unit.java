package br.edu.ifpr.mcp_server.domain;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

/**
 * Unidades de medida suportadas. Cada unidade sabe converter de/para uma
 * "unidade base" da sua categoria (metro, quilograma e Celsius). Para
 * acrescentar uma unidade nova basta incluir uma linha aqui (Open/Closed).
 */
public enum Unit {
   METER("m",Category.LENGTH,v -> v,v -> v),
   KILOMETER("km",Category.LENGTH,v -> v * 1000,v -> v / 1000),
   CENTIMETER("cm",Category.LENGTH,v -> v / 100,v -> v * 100),
   MILE("mi",Category.LENGTH,v -> v * 1609.344,v -> v / 1609.344),
   FOOT("ft",Category.LENGTH,v -> v * 0.3048,v -> v / 0.3048),

   KILOGRAM("kg",Category.MASS,v -> v,v -> v),
   GRAM("g",Category.MASS,v -> v / 1000,v -> v * 1000),
   POUND("lb",Category.MASS,v -> v * 0.45359237,v -> v / 0.45359237),

   CELSIUS("c",Category.TEMPERATURE,v -> v,v -> v),
   FAHRENHEIT("f",Category.TEMPERATURE,v -> (v - 32) * 5 / 9,
                                       v -> v * 9 / 5 + 32),
   KELVIN("k",Category.TEMPERATURE,v -> v - 273.15,v -> v + 273.15);

   public enum Category {
      LENGTH("comprimento"), MASS("massa"), TEMPERATURE("temperatura");

      private final String label;

      Category(String label) { this.label = label; }

      public String label() { return label; }
   }

   private final String              symbol;
   private final Category            category;
   private final DoubleUnaryOperator toBase;
   private final DoubleUnaryOperator fromBase;

   Unit(String symbol, Category category, DoubleUnaryOperator toBase,
        DoubleUnaryOperator fromBase) {
      this.symbol   = symbol;
      this.category = category;
      this.toBase   = toBase;
      this.fromBase = fromBase;
   }

   public String symbol() {
      return symbol;
   }

   public double convertTo(Unit target, double value) {
      if (category != target.category)
         throw new IllegalArgumentException(String.format(
                   "Não é possível converter '%s' (%s) para '%s' (%s).",
                   symbol,category.label(),target.symbol,
                   target.category.label()));
      return target.fromBase.applyAsDouble(toBase.applyAsDouble(value));
   }

   public static Unit fromSymbol(String symbol) {
      String wanted = symbol.trim();

      for (Unit unit : values())
         if (unit.symbol.equalsIgnoreCase(wanted))
            return unit;

      throw new IllegalArgumentException("Unidade desconhecida: '" + symbol +
                                         "'. Unidades suportadas: " +
                                         supportedSymbols() + ".");
   }

   public static String supportedSymbols() {
      return Arrays.stream(values())
                   .map(Unit::symbol)
                   .collect(Collectors.joining(", "));
   }
}
