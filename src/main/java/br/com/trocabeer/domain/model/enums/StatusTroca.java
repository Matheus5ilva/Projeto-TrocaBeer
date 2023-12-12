package br.com.trocabeer.domain.model.enums;

public enum StatusTroca {
  ACEITO("Aceito"), RECUSADO("Recusado"), PENDENTE("Pendente"), ENTREGUE("Entregue");

  private String descricao;

  StatusTroca(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
