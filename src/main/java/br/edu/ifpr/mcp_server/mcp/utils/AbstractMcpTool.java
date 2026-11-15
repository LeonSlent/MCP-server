package br.edu.ifpr.mcp_server.mcp.utils;

import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.server.McpServer.SingleSessionSyncSpecification;
import io.modelcontextprotocol.server.McpServer.SyncSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;

/**
 * Padrão de projeto TEMPLATE METHOD.
 *
 * Esta classe contém o que é igual em TODAS as ferramentas (registrar no
 * servidor, montar a resposta, tratar erros de validação). Cada ferramenta
 * filha informa apenas o que muda: nome, descrição, parâmetros e a lógica
 * em "execute".
 */
public abstract class AbstractMcpTool implements McpToolDefinition {
   protected abstract String name();
   protected abstract String title();
   protected abstract String description();
   protected abstract Map<String, Object> inputSchema();

   /**
    * Lógica da ferramenta. Recebe os argumentos e devolve o texto de
    * resposta. Deve lançar IllegalArgumentException se a entrada for inválida.
    */
   public abstract String execute(Map<String, Object> arguments);

   @Override
   public void register(SyncSpecification<SingleSessionSyncSpecification> mcp) {
      Tool tool = Tool.builder(name(),inputSchema())
                      .title(title())
                      .description(description())
                      .build();
      SyncToolSpecification toolSpec = SyncToolSpecification.builder()
                                                            .tool(tool)
                                                            .callHandler(this::run)
                                                            .build();
      mcp.tools(toolSpec);
   }

   @Override
   public CallToolResult run(McpSyncServerExchange exchange,
                             CallToolRequest request) {
      Map<String, Object> arguments = request.arguments() != null
                                      ? request.arguments() : Map.of();
      try {
         return buildResult(execute(arguments),false);
      }
      catch (IllegalArgumentException ex) {
         // erro de entrada do usuário: resposta MCP com isError = true
         return buildResult(ex.getMessage(),true);
      }
   }

   private CallToolResult buildResult(String text, boolean error) {
      TextContent content = TextContent.builder(text).build();

      return CallToolResult.builder()
                           .content(List.of(content))
                           .isError(error)
                           .build();
   }
}
