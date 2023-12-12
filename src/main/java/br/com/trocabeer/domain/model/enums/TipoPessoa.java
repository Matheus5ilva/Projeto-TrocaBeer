package br.com.trocabeer.domain.model.enums;

public enum TipoPessoa {
  MESTRE_CERVEJEIRO("Mestre Cervejeiro"), APRECIADOR("Apreciador");

  private String descricao;

  TipoPessoa(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
