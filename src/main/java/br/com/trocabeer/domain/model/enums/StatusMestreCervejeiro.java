package br.com.trocabeer.domain.model.enums;

public enum StatusMestreCervejeiro {
  GRATUITO("Aceito"), PREMIUM("Recusado");

  private String descricao;

  StatusMestreCervejeiro(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
