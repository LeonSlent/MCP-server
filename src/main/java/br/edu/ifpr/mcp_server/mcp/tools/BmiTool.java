package br.edu.ifpr.mcp_server.mcp.tools;

import java.util.Locale;
import java.util.Map;

import br.edu.ifpr.mcp_server.mcp.utils.AbstractMcpTool;
import br.edu.ifpr.mcp_server.mcp.utils.ToolArguments;
import br.edu.ifpr.mcp_server.mcp.utils.ToolSchema;

public class BmiTool extends AbstractMcpTool {
   private static final String WEIGHT = "peso_kg";
   private static final String HEIGHT = "altura_m";
   private static final double MAX_HEIGHT = 3.0;

   @Override
   protected String name() { return "calcular_imc"; }

   @Override
   protected String title() { return "Calcular IMC"; }

   @Override
   protected String description() {
      return "Calcula o Índice de Massa Corporal (IMC) e informa a " +
             "classificação. Peso em quilogramas e altura em metros.";
   }

   @Override
   protected Map<String, Object> inputSchema() {
      return ToolSchema.create()
                       .required(WEIGHT,"number","Peso em quilogramas (ex.: 70.5)")
                       .required(HEIGHT,"number","Altura em metros (ex.: 1.75)")
                       .build();
   }

   @Override
   public String execute(Map<String, Object> arguments) {
      double weight = ToolArguments.requireNumber(arguments,WEIGHT);
      double height = ToolArguments.requireNumber(arguments,HEIGHT);

      validate(weight,height);

      double bmi = weight / (height * height);

      return String.format(Locale.ROOT,"IMC: %.2f - %s",bmi,classify(bmi));
   }

   private void validate(double weight, double height) {
      if (weight <= 0)
         throw new IllegalArgumentException("O peso deve ser maior que zero.");
      if (height <= 0 || height > MAX_HEIGHT)
         throw new IllegalArgumentException("A altura deve ser maior que " +
                                            "zero e no máximo 3 metros.");
   }

   private String classify(double bmi) {
      if (bmi < 18.5) return "Abaixo do peso";
      if (bmi < 25.0) return "Peso normal";
      if (bmi < 30.0) return "Sobrepeso";
      if (bmi < 35.0) return "Obesidade grau I";
      if (bmi < 40.0) return "Obesidade grau II";
      return "Obesidade grau III";
   }
}
