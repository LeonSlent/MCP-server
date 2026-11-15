package br.edu.ifpr.mcp_server.mcp.tools;

import java.util.List;

import br.edu.ifpr.mcp_server.mcp.utils.McpToolDefinition;

/**
 * Padrão FACTORY: único lugar que conhece todas as ferramentas do servidor.
 * Para criar uma ferramenta nova: crie a classe e acrescente-a aqui. O
 * servidor não precisa ser alterado (Open/Closed).
 */
public final class ToolCatalog {
   private ToolCatalog() {}

   public static List<McpToolDefinition> createTools() {
      return List.of(new BmiTool(),
                     new UnitConverterTool(),
                     new PasswordGeneratorTool(),
                     new CpfValidatorTool(),
                     new WordCounterTool());
   }
}
