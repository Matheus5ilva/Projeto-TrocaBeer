package br.com.trocabeer.domain.model.enums;

public enum TipoTroca {
  TROCA("Troca"), APOIO("Apoio"), TROCA_APOIO("Troca/Apoio");

  private String descricao;

  TipoTroca(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
