package br.edu.cs.poo.ac.bolsa.negocio;

import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;
import br.edu.cs.poo.ac.bolsa.util.ValidadorCpfCnpj;
import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestidorMediator {

    private DAOInvestidorEmpresa daoInvEmp = new DAOInvestidorEmpresa();
    private DAOInvestidorPessoa daoInvPes = new DAOInvestidorPessoa();

    private MensagensValidacao validarEndereco(Endereco endereco) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (endereco == null) {
            msgs.adicionar("Endereço é obrigatório.");
            return msgs;
        }

        if (
            endereco.getLogradouro() == null ||
            endereco.getLogradouro().trim().isEmpty()
        ) {
            msgs.adicionar("Logradouro é obrigatório.");
        }
        if (
            endereco.getNumero() == null ||
            endereco.getNumero().trim().isEmpty()
        ) {
            msgs.adicionar("Número é obrigatório.");
        }
        if (
            endereco.getCidade() == null ||
            endereco.getCidade().trim().isEmpty()
        ) {
            msgs.adicionar("Cidade é obrigatório.");
        }
        if (
            endereco.getEstado() == null ||
            endereco.getEstado().trim().isEmpty()
        ) {
            msgs.adicionar("Estado é obrigatório.");
        }
        if (endereco.getPais() == null || endereco.getPais().trim().isEmpty()) {
            msgs.adicionar("País é obrigatório.");
        }
        return msgs;
    }

    private MensagensValidacao validarContatos(
        Contatos contatos,
        boolean ehPessoaJuridica
    ) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (contatos == null) {
            msgs.adicionar("Contatos são obrigatórios.");
            return msgs;
        }

        if (
            contatos.getEmail() == null || contatos.getEmail().trim().isEmpty()
        ) {
            msgs.adicionar("E-mail é obrigatório.");
        } else if (
            !contatos.getEmail().contains("@") ||
            !contatos.getEmail().contains(".")
        ) {
            msgs.adicionar("E-mail inválido.");
        }

        boolean temFixo =
            contatos.getTelefoneFixo() != null &&
            !contatos.getTelefoneFixo().trim().isEmpty();
        boolean temCelular =
            contatos.getTelefoneCelular() != null &&
            !contatos.getTelefoneCelular().trim().isEmpty();
        boolean temZap =
            contatos.getNumeroWhatsApp() != null &&
            !contatos.getNumeroWhatsApp().trim().isEmpty();

        if (!temFixo && !temCelular && !temZap) {
            msgs.adicionar("Pelo menos um telefone deve ser informado.");
        }

        if (temFixo && !contatos.getTelefoneFixo().matches("[0-9]+")) {
            msgs.adicionar("Telefone fixo deve conter apenas números.");
        }
        if (temCelular && !contatos.getTelefoneCelular().matches("[0-9]+")) {
            msgs.adicionar("Telefone celular deve conter apenas números.");
        }
        if (temZap && !contatos.getNumeroWhatsApp().matches("[0-9]+")) {
            msgs.adicionar("WhatsApp deve conter apenas números.");
        }

        if (ehPessoaJuridica) {
            if (
                contatos.getNomeParaContato() == null ||
                contatos.getNomeParaContato().trim().isEmpty()
            ) {
                msgs.adicionar(
                    "Nome para contato é obrigatório para pessoa jurídica."
                );
            }
        }

        return msgs;
    }

    private MensagensValidacao validar(DadosInvestidor dadosInv) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (dadosInv == null) {
            msgs.adicionar("Dados do investidor não informados.");
            return msgs;
        }

        if (dadosInv.getNome() == null || dadosInv.getNome().trim().isEmpty()) {
            msgs.adicionar("Nome é obrigatório.");
        }
        if (dadosInv.getDataCriacao() == null) {
            msgs.adicionar("Data é obrigatória.");
        } else if (dadosInv.getDataCriacao().isAfter(LocalDate.now())) {
            msgs.adicionar("Data não pode ser no futuro.");
        }
        if (dadosInv.getBonus() == null) {
            msgs.adicionar("Bônus é obrigatório.");
        } else if (dadosInv.getBonus().compareTo(BigDecimal.ZERO) < 0) {
            msgs.adicionar("Bônus não pode ser negativo.");
        }

        msgs.adicionar(validarEndereco(dadosInv.getEndereco()));
        msgs.adicionar(
            validarContatos(
                dadosInv.getContatos(),
                dadosInv.ehInvestidorEmpresa()
            )
        );

        return msgs;
    }

    private MensagensValidacao validarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (ie == null) {
            msgs.adicionar("Investidor Empresa não informado.");
            return msgs;
        }

        DadosInvestidor dados = new DadosInvestidor(ie, null);
        msgs.adicionar(validar(dados));

        if (ValidadorCpfCnpj.validarCnpj(ie.getCnpj()) != null) {
            msgs.adicionar("CNPJ inválido.");
        }
        if (ie.getFaturamento() < 100000.0) {
            msgs.adicionar("Faturamento deve ser maior ou igual a 100.000.");
        }

        return msgs;
    }

    private MensagensValidacao validarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = new MensagensValidacao();

        if (ip == null) {
            msgs.adicionar("Investidor Pessoa não informado.");
            return msgs;
        }

        DadosInvestidor dados = new DadosInvestidor(null, ip);
        msgs.adicionar(validar(dados));

        if (ValidadorCpfCnpj.validarCpf(ip.getCpf()) != null) {
            msgs.adicionar("CPF inválido.");
        }
        if (ip.getRenda() < 10000.0) {
            msgs.adicionar("Renda deve ser maior ou igual a 10.000.");
        }

        return msgs;
    }

    public MensagensValidacao incluirInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            boolean sucesso = daoInvEmp.incluir(ie);
            if (!sucesso) {
                msgs.adicionar("Investidor Empresa já existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorEmpresa(InvestidorEmpresa ie) {
        MensagensValidacao msgs = validarInvestidorEmpresa(ie);
        if (msgs.estaVazio()) {
            boolean sucesso = daoInvEmp.alterar(ie);
            if (!sucesso) {
                msgs.adicionar("Investidor Empresa não existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorEmpresa(String cnpj) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (ValidadorCpfCnpj.validarCnpj(cnpj) != null) {
            msgs.adicionar("CNPJ inválido.");
            return msgs;
        }

        boolean sucesso = daoInvEmp.excluir(cnpj);
        if (!sucesso) {
            msgs.adicionar("Investidor Empresa não existente.");
        }
        return msgs;
    }

    public InvestidorEmpresa buscarInvestidorEmpresa(String cnpj) {
        if (ValidadorCpfCnpj.validarCnpj(cnpj) != null) {
            return null;
        }
        return daoInvEmp.buscar(cnpj);
    }

    public MensagensValidacao incluirInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            boolean sucesso = daoInvPes.incluir(ip);
            if (!sucesso) {
                msgs.adicionar("Investidor Pessoa já existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao alterarInvestidorPessoa(InvestidorPessoa ip) {
        MensagensValidacao msgs = validarInvestidorPessoa(ip);
        if (msgs.estaVazio()) {
            boolean sucesso = daoInvPes.alterar(ip);
            if (!sucesso) {
                msgs.adicionar("Investidor Pessoa não existente.");
            }
        }
        return msgs;
    }

    public MensagensValidacao excluirInvestidorPessoa(String cpf) {
        MensagensValidacao msgs = new MensagensValidacao();
        if (ValidadorCpfCnpj.validarCpf(cpf) != null) {
            msgs.adicionar("CPF inválido.");
            return msgs;
        }

        boolean sucesso = daoInvPes.excluir(cpf);
        if (!sucesso) {
            msgs.adicionar("Investidor Pessoa não existente.");
        }
        return msgs;
    }

    public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
        if (ValidadorCpfCnpj.validarCpf(cpf) != null) {
            return null;
        }
        return daoInvPes.buscar(cpf);
    }
}
