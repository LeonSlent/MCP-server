package br.edu.ifpr.mcp_server.domain;

/** Validação de CPF pelos dois dígitos verificadores. */
public final class CpfValidator {
   private static final int CPF_LENGTH = 11;

   private CpfValidator() {}

   /** Aceita CPF com ou sem pontuação (ex.: 529.982.247-25). */
   public static boolean isValid(String cpf) {
      String digits = cpf.replaceAll("\\D","");

      if (digits.length() != CPF_LENGTH || allDigitsEqual(digits))
         return false;

      return checkDigit(digits,9) == digitAt(digits,9) &&
             checkDigit(digits,10) == digitAt(digits,10);
   }

   private static boolean allDigitsEqual(String digits) {
      return digits.chars().distinct().count() == 1;
   }

   private static int checkDigit(String digits, int length) {
      int sum = 0;

      for (int i = 0; i < length; i++)
         sum += digitAt(digits,i) * (length + 1 - i);

      int rest = (sum * 10) % 11;

      return rest == 10 ? 0 : rest;
   }

   private static int digitAt(String digits, int index) {
      return digits.charAt(index) - '0';
   }
}
