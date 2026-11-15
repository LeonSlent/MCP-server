package br.edu.ifpr.mcp_server.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import tools.jackson.databind.json.JsonMapper;

/**
 * Teste de integração: sobe o servidor MCP de verdade (o .jar gerado pelo
 * perfil "server") e executa cada ferramenta como um cliente MCP faria.
 */
class McpServer01IT {
   private static final Logger LOG = LoggerFactory.getLogger(McpServer01IT.class);
   private static final String SERVER_PATH = "server/mcp_server01_server.jar";

   private static McpSyncClient client;

   @BeforeAll
   static void setUpBeforeClass() throws Exception {
      File serverJar = new File(SERVER_PATH);
      assertTrue(serverJar.isFile(),"JAR do servidor não encontrado: " +
                                    serverJar.getAbsolutePath());

      ServerParameters params = ServerParameters.builder("java")
                                                .args("-jar",serverJar.getAbsolutePath())
                                                .build();
      JacksonMcpJsonMapper jsonMapper = new JacksonMcpJsonMapper(new JsonMapper());
      StdioClientTransport transport  = new StdioClientTransport(params,jsonMapper);

      transport.setStdErrorHandler(line -> LOG.info("[SERVER STDERR] " + line));

      client = McpClient.sync(transport).build();
      client.initialize();
   }

   @AfterAll
   static void tearDownAfterClass() throws Exception {
      if (client != null)
         client.closeGracefully();
   }

   private CallToolResult call(String tool, Map<String, Object> arguments) {
      CallToolResult result = client.callTool(CallToolRequest.builder(tool)
                                                             .arguments(arguments)
                                                             .build());
      assertNotNull(result,"O servidor não retornou resultado.");
      assertFalse(result.content().isEmpty(),"O resultado deveria ter 'content'.");
      return result;
   }

   private String text(CallToolResult result) {
      return ((TextContent) result.content().get(0)).text();
   }

   @Test
   void deveDisponibilizarAsCincoFerramentas() {
      List<String> names = client.listTools().tools().stream()
                                 .map(tool -> tool.name())
                                 .toList();

      assertTrue(names.containsAll(List.of("calcular_imc","converter_unidade",
                                           "gerar_senha","validar_cpf",
                                           "contar_palavras")),
                 "Ferramentas encontradas: " + names);
   }

   @Test
   void todasAsFerramentasDevemTerDescricaoESchema() {
      client.listTools().tools().forEach(tool -> {
         assertNotNull(tool.description(),tool.name() + " sem descrição.");
         assertNotNull(tool.inputSchema(),tool.name() + " sem inputSchema.");
      });
   }

   @Test
   void deveExecutarCalcularImc() {
      CallToolResult result = call("calcular_imc",Map.of("peso_kg",70,
                                                         "altura_m",1.75));
      assertFalse(result.isError());
      assertEquals("IMC: 22.86 - Peso normal",text(result));
   }

   @Test
   void deveRetornarErroMcpParaImcInvalido() {
      CallToolResult result = call("calcular_imc",Map.of("peso_kg",-1,
                                                         "altura_m",1.75));
      assertTrue(result.isError());
   }

   @Test
   void deveExecutarConverterUnidade() {
      CallToolResult result = call("converter_unidade",
                                   Map.of("valor",1,"unidade_origem","km",
                                          "unidade_destino","m"));
      assertFalse(result.isError());
      assertEquals("1.0000 km = 1000.0000 m",text(result));
   }

   @Test
   void deveRetornarErroMcpParaConversaoImpossivel() {
      CallToolResult result = call("converter_unidade",
                                   Map.of("valor",1,"unidade_origem","kg",
                                          "unidade_destino","m"));
      assertTrue(result.isError());
   }

   @Test
   void deveExecutarGerarSenha() {
      CallToolResult result = call("gerar_senha",Map.of("tamanho",12,
                                                        "incluir_simbolos",true));
      assertFalse(result.isError());
      assertEquals(12,text(result).length());
   }

   @Test
   void deveExecutarValidarCpf() {
      assertEquals("CPF válido.",
                   text(call("validar_cpf",Map.of("cpf","529.982.247-25"))));
      assertEquals("CPF inválido.",
                   text(call("validar_cpf",Map.of("cpf","111.111.111-11"))));
   }

   @Test
   void deveExecutarContarPalavras() {
      CallToolResult result = call("contar_palavras",Map.of("texto","Olá mundo cruel"));

      assertFalse(result.isError());
      assertTrue(text(result).startsWith("Palavras: 3"));
   }
}
