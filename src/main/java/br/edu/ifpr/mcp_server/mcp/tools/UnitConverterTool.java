package br.edu.ifpr.mcp_server.mcp.tools;

import java.util.Locale;
import java.util.Map;

import br.edu.ifpr.mcp_server.domain.Unit;
import br.edu.ifpr.mcp_server.mcp.utils.AbstractMcpTool;
import br.edu.ifpr.mcp_server.mcp.utils.ToolArguments;
import br.edu.ifpr.mcp_server.mcp.utils.ToolSchema;

public class UnitConverterTool extends AbstractMcpTool {
   private static final String VALUE = "valor";
   private static final String FROM  = "unidade_origem";
   private static final String TO    = "unidade_destino";

   @Override
   protected String name() { return "converter_unidade"; }

   @Override
   protected String title() { return "Converter unidade de medida"; }

   @Override
   protected String description() {
      return "Converte um valor entre unidades de comprimento, massa e " +
             "temperatura. Unidades suportadas: " + Unit.supportedSymbols() + ".";
   }

   @Override
   protected Map<String, Object> inputSchema() {
      return ToolSchema.create()
                       .required(VALUE,"number","Valor a ser convertido")
                       .required(FROM,"string","Unidade de origem (ex.: km)")
                       .required(TO,"string","Unidade de destino (ex.: mi)")
                       .build();
   }

   @Override
   public String execute(Map<String, Object> arguments) {
      double value = ToolArguments.requireNumber(arguments,VALUE);
      Unit   from  = Unit.fromSymbol(ToolArguments.requireString(arguments,FROM));
      Unit   to    = Unit.fromSymbol(ToolArguments.requireString(arguments,TO));
      double result = from.convertTo(to,value);

      return String.format(Locale.ROOT,"%.4f %s = %.4f %s",
                           value,from.symbol(),result,to.symbol());
   }
}
