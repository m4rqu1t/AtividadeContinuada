package br.edu.cs.poo.ac.bolsa.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class Investidor {

    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;
    private Contatos contatos;

    public Investidor(
        String nome,
        Endereco endereco,
        LocalDate dataCriacao,
        BigDecimal bonus,
        Contatos contatos
    ) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
        this.contatos = contatos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Contatos getContatos() {
        return contatos;
    }

    public void setContatos(Contatos contatos) {
        this.contatos = contatos;
    }

    protected LocalDate getDataCriacao() {
        return dataCriacao;
    }

    protected void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public int getIdade() {
        if (this.dataCriacao == null) return 0;
        return Period.between(this.dataCriacao, LocalDate.now()).getYears();
    }

    public void creditarBonus(BigDecimal valor) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0) {
            this.bonus = this.bonus.add(valor);
        }
    }

    public void debitarBonus(BigDecimal valor) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0) {
            this.bonus = this.bonus.subtract(valor);
        }
    }
}
