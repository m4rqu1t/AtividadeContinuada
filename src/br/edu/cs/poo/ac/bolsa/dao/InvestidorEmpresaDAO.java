package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;

public class InvestidorEmpresaDAO extends DAOGenerico {

    public InvestidorEmpresaDAO() {
        inicializarCadastro(InvestidorEmpresa.class);
    }

    public InvestidorEmpresa buscar(String cnpj) {
        return (InvestidorEmpresa) cadastro.buscar(cnpj);
    }

    public boolean incluir(InvestidorEmpresa investidor) {
        if (buscar(investidor.getCnpj()) == null) {
            cadastro.incluir(investidor, investidor.getCnpj());
            return true;
        } else {
            return false;
        }
    }

    public boolean alterar(InvestidorEmpresa investidor) {
        if (buscar(investidor.getCnpj()) != null) {
            cadastro.alterar(investidor, investidor.getCnpj());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluir(String cnpj) {
        if (buscar(cnpj) != null) {
            cadastro.excluir(cnpj);
            return true;
        } else {
            return false;
        }
    }
}
