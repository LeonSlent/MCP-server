package br.edu.ifpr.mcp_server.mcp.utils;

import java.util.Map;

/**
 * Leitura e validação dos argumentos enviados pelo cliente.
 * Todo erro de validação vira uma IllegalArgumentException com mensagem
 * clara, que a classe AbstractMcpTool converte em resposta de erro MCP.
 */
public final class ToolArguments {
   private ToolArguments() {}

   public static String requireString(Map<String, Object> args, String key) {
      Object value = args.get(key);

      if (value == null || value.toString().isBlank())
         throw new IllegalArgumentException("Parâmetro obrigatório " +
                                            "ausente: '" + key + "'.");
      return value.toString();
   }

   public static double requireNumber(Map<String, Object> args, String key) {
      Object value = args.get(key);

      if (value == null)
         throw new IllegalArgumentException("Parâmetro obrigatório " +
                                            "ausente: '" + key + "'.");
      // números vindos de JSON podem ser Integer, Long, Double...
      if (value instanceof Number number)
         return number.doubleValue();

      throw new IllegalArgumentException("O parâmetro '" + key +
                                         "' deve ser numérico.");
   }

   public static int requireInt(Map<String, Object> args, String key) {
      double value = requireNumber(args,key);

      if (value != Math.rint(value))
         throw new IllegalArgumentException("O parâmetro '" + key +
                                            "' deve ser um número inteiro.");
      return (int) value;
   }

   public static boolean optionalBoolean(Map<String, Object> args, String key,
                                         boolean defaultValue) {
      Object value = args.get(key);

      if (value == null)
         return defaultValue;
      if (value instanceof Boolean bool)
         return bool;

      throw new IllegalArgumentException("O parâmetro '" + key +
                                         "' deve ser true ou false.");
   }
}
