package br.edu.cs.poo.ac.bolsa.util;

public class ValidadorCpfCnpj {

    public static ResultadoValidacao validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        if (!isCpfValido(cpf)) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    public static ResultadoValidacao validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return ResultadoValidacao.NAO_INFORMADO;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return ResultadoValidacao.FORMATO_INVALIDO;
        }

        if (!isCnpjValido(cnpj)) {
            return ResultadoValidacao.DV_INVALIDO;
        }

        return null;
    }

    private static boolean isCpfValido(String cpf) {
        try {
            int sm, i, r, num, peso;
            char dig10, dig11;

            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig10 = '0';
            else dig10 = (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig11 = '0';
            else dig11 = (char) (r + 48);

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isCnpjValido(String cnpj) {
        try {
            int sm, i, r, num, peso;
            char dig13, dig14;

            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) dig13 = '0';
            else dig13 = (char) ((11 - r) + 48);

            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) dig14 = '0';
            else dig14 = (char) ((11 - r) + 48);

            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }
}
