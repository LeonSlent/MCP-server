package br.edu.ifpr.mcp_server.mcp.tools;

import java.util.Locale;
import java.util.Map;

import br.edu.ifpr.mcp_server.mcp.utils.AbstractMcpTool;
import br.edu.ifpr.mcp_server.mcp.utils.ToolArguments;
import br.edu.ifpr.mcp_server.mcp.utils.ToolSchema;

public class WordCounterTool extends AbstractMcpTool {
   private static final String TEXT = "texto";

   @Override
   protected String name() { return "contar_palavras"; }

   @Override
   protected String title() { return "Contar palavras"; }

   @Override
   protected String description() {
      return "Conta palavras, caracteres, caracteres sem espaços e linhas " +
             "de um texto.";
   }

   @Override
   protected Map<String, Object> inputSchema() {
      return ToolSchema.create()
                       .required(TEXT,"string","Texto a ser analisado")
                       .build();
   }

   @Override
   public String execute(Map<String, Object> arguments) {
      String text = ToolArguments.requireString(arguments,TEXT);

      return String.format(Locale.ROOT,
                           "Palavras: %d | Caracteres: %d | " +
                           "Caracteres sem espaços: %d | Linhas: %d",
                           countWords(text),text.length(),
                           text.replaceAll("\\s","").length(),text.lines().count());
   }

   private long countWords(String text) {
      return text.trim().split("\\s+").length;
   }
}
