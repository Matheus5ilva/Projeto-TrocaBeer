package br.com.trocabeer.controller.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.trocabeer.domain.model.Cerveja;
import br.com.trocabeer.domain.model.ComplementoCerveja;
import br.com.trocabeer.domain.model.Estilo;
import br.com.trocabeer.domain.model.InformacaoTecnica;
import br.com.trocabeer.domain.model.Usuario;
import br.com.trocabeer.domain.model.enums.TipoTroca;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CervejaCadastroDTO {

	@NotBlank(message = "O nome da cerveja é obrigatório.")
	private String nome;
	
	@NotNull(message = "O estilo da cerveja é obrigatório.")
	private Estilo estilo;
	
	@NotBlank(message = "O volume/quantidade da cerveja é obrigatório.")
	private String volume;

	@NotBlank(message = "A coloração da cerveja é obrigatório.")
	private String coloracao;

	@NotBlank(message = "O amargor da cerveja é obrigatório.")
	private String amargor;

	@NotBlank(message = "O teor alcoólico da cerveja é obrigatório.")
	private String teorAlcoolico;

	@NotBlank(message = "A temperatura ideal da cerveja é obrigatório.")
	private String temperaturaIdeal;
	
	private InformacaoTecnica descricaoTecnica = new InformacaoTecnica();
	
	private Boolean ativo = true; // Defina um valor padrão como true

	private List<String> copoSugerido = new ArrayList<>(); // Inicialize a lista

	private List<String> comidaHarmonizada = new ArrayList<>(); // Inicialize a lista

	
	@NotNull(message = "A data de fabricação é obrigatório.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataFabricacao;

	@NotNull(message = "A data de validade é obrigatório.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataValidade;

	@Positive(message="O estoque tem que ser maior que 0.")
	private BigDecimal estoque;
	
	private BigDecimal estoqueMovimento;

	@Enumerated(EnumType.STRING)
	private TipoTroca tipoTroca;
	
	private Usuario usuario = new Usuario();
	
	public Cerveja toCerveja(Usuario usuario) {

		this.getDescricaoTecnica().setAmargor(this.getAmargor());
		this.getDescricaoTecnica().setColoracao(this.getColoracao());
		this.getDescricaoTecnica().setTemperaturaIdeal(this.getTemperaturaIdeal());
		this.getDescricaoTecnica().setTeorAlcoolico(this.getTeorAlcoolico());
		this.getDescricaoTecnica().setVolume(this.getVolume());
		this.setEstoqueMovimento(getEstoque());
		this.setUsuario(usuario);

		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(this, Cerveja.class);
	}
	
	public ComplementoCerveja toComplemento(Cerveja cerveja) {
		
		ComplementoCerveja complemento = new ComplementoCerveja();
		
		complemento.setDataFabricacao(this.getDataFabricacao());
		complemento.setDataValidade(this.getDataValidade());
		complemento.setEstoque(this.getEstoque());
		complemento.setTipoTroca(this.getTipoTroca());
		complemento.setCerveja(cerveja);
	
		return complemento;
	}
		
}
