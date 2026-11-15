package br.edu.ifpr.mcp_server.mcp.tools;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.edu.ifpr.mcp_server.mcp.utils.AbstractMcpTool;
import br.edu.ifpr.mcp_server.mcp.utils.ToolArguments;
import br.edu.ifpr.mcp_server.mcp.utils.ToolSchema;

public class PasswordGeneratorTool extends AbstractMcpTool {
   static final String SYMBOLS = "!@#$%&*?-_";

   private static final String LOWER   = "abcdefghijklmnopqrstuvwxyz";
   private static final String UPPER   = LOWER.toUpperCase();
   private static final String DIGITS  = "0123456789";
   private static final String LENGTH  = "tamanho";
   private static final String SYMBOLS_PARAM = "incluir_simbolos";
   private static final int    MIN_LENGTH = 8;
   private static final int    MAX_LENGTH = 64;

   private final Random random;

   public PasswordGeneratorTool() {
      this(new SecureRandom());
   }

   // permite injetar outro gerador (por exemplo, um previsível nos testes)
   PasswordGeneratorTool(Random random) {
      this.random = random;
   }

   @Override
   protected String name() { return "gerar_senha"; }

   @Override
   protected String title() { return "Gerar senha segura"; }

   @Override
   protected String description() {
      return "Gera uma senha aleatória com letras minúsculas, maiúsculas e " +
             "números (e símbolos, se pedido). Tamanho de 8 a 64.";
   }

   @Override
   protected Map<String, Object> inputSchema() {
      return ToolSchema.create()
                       .required(LENGTH,"integer","Tamanho da senha (8 a 64)")
                       .optional(SYMBOLS_PARAM,"boolean",
                                 "Incluir símbolos (padrão: false)")
                       .build();
   }

   @Override
   public String execute(Map<String, Object> arguments) {
      int     length         = ToolArguments.requireInt(arguments,LENGTH);
      boolean includeSymbols = ToolArguments.optionalBoolean(arguments,
                                                             SYMBOLS_PARAM,false);
      if (length < MIN_LENGTH || length > MAX_LENGTH)
         throw new IllegalArgumentException("O tamanho deve estar entre " +
                                            MIN_LENGTH + " e " + MAX_LENGTH + ".");
      return generate(length,includeSymbols);
   }

   private String generate(int length, boolean includeSymbols) {
      List<String> groups = new ArrayList<>(List.of(LOWER,UPPER,DIGITS));

      if (includeSymbols)
         groups.add(SYMBOLS);

      String          all   = String.join("",groups);
      List<Character> chars = new ArrayList<>();
      // garante ao menos um caractere de cada grupo...
      for (String group : groups)
         chars.add(pick(group));
      // ...e completa o restante com qualquer caractere permitido
      while (chars.size() < length)
         chars.add(pick(all));

      Collections.shuffle(chars,random);

      StringBuilder password = new StringBuilder();

      chars.forEach(password::append);
      return password.toString();
   }

   private char pick(String source) {
      return source.charAt(random.nextInt(source.length()));
   }
}
