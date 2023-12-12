package br.com.trocabeer.controller.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trocabeer.domain.model.Cidade;
import br.com.trocabeer.domain.model.Estado;
import br.com.trocabeer.domain.repository.CidadeRepository;
import br.com.trocabeer.domain.repository.EstadoRepository;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

  @Autowired
  private CidadeRepository cidadeRepository;

  @Autowired
  private EstadoRepository estadoRepository;

  @GetMapping("/buscarCidades")
  public List<String> listarCidades() {
    List<Cidade> cidades = cidadeRepository.findAll();
    List<String> nomesCidades = cidades.stream()
        .map(Cidade::getNome)
        .collect(Collectors.toList());
    return nomesCidades;
  }

  @GetMapping("/buscarEstados")
  public List<String> listarEstados() {
    List<Estado> estados = estadoRepository.findAll();
    List<String> nomesEstados = estados.stream()
        .map(Estado::getNome)
        .collect(Collectors.toList());
    return nomesEstados;
  }

  @GetMapping("/buscarCidadesPorEstado")
  public List<Cidade> listarCidadesPorEstado(@RequestParam("estadoId") Long estadoId) {
    Estado estado = estadoRepository.findById(estadoId).orElseThrow(
        () -> new RuntimeException("Não foi encontrado Estado"));
    List<Cidade> cidades = cidadeRepository.findByEstado(estado);
    return cidades;
  }
}
