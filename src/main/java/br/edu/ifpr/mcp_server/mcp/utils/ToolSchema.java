package br.edu.ifpr.mcp_server.mcp.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Monta o esquema JSON (JSON Schema) dos parâmetros de uma ferramenta.
 * Evita repetir o mesmo "Map.of(...)" em todas as ferramentas (DRY) e
 * mantém a ordem dos parâmetros, que é a ordem em que o cliente os pergunta.
 */
public final class ToolSchema {
   private final Map<String, Object> properties = new LinkedHashMap<>();
   private final List<String>        required   = new ArrayList<>();

   private ToolSchema() {}

   public static ToolSchema create() {
      return new ToolSchema();
   }

   public ToolSchema required(String name, String type, String description) {
      return add(name,type,description,true);
   }

   public ToolSchema optional(String name, String type, String description) {
      return add(name,type,description,false);
   }

   private ToolSchema add(String name, String type, String description,
                          boolean isRequired) {
      properties.put(name,Map.of("type",type,"description",description));
      if (isRequired)
         required.add(name);
      return this;
   }

   public Map<String, Object> build() {
      Map<String, Object> schema = new LinkedHashMap<>();

      schema.put("type","object");
      schema.put("properties",properties);
      schema.put("required",required);
      schema.put("additionalProperties",false);
      return schema;
   }
}
