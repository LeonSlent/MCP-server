package br.edu.ifpr.mcp_server.mcp.tools;

import java.util.Map;

import br.edu.ifpr.mcp_server.domain.CpfValidator;
import br.edu.ifpr.mcp_server.mcp.utils.AbstractMcpTool;
import br.edu.ifpr.mcp_server.mcp.utils.ToolArguments;
import br.edu.ifpr.mcp_server.mcp.utils.ToolSchema;

public class CpfValidatorTool extends AbstractMcpTool {
   private static final String CPF = "cpf";

   @Override
   protected String name() { return "validar_cpf"; }

   @Override
   protected String title() { return "Validar CPF"; }

   @Override
   protected String description() {
      return "Verifica se um CPF é válido pelos dígitos verificadores. " +
             "Aceita com ou sem pontuação.";
   }

   @Override
   protected Map<String, Object> inputSchema() {
      return ToolSchema.create()
                       .required(CPF,"string","CPF (ex.: 529.982.247-25)")
                       .build();
   }

   @Override
   public String execute(Map<String, Object> arguments) {
      String cpf = ToolArguments.requireString(arguments,CPF);

      return CpfValidator.isValid(cpf) ? "CPF válido." : "CPF inválido.";
   }
}
