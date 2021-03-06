package br.edu.ifpb.SIGAV.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.SIGAV.controller.page.PageWrapper;
import br.edu.ifpb.SIGAV.domain.Veiculo;
import br.edu.ifpb.SIGAV.enumerations.EspecieVeiculo;
import br.edu.ifpb.SIGAV.enumerations.Origin;
import br.edu.ifpb.SIGAV.filter.VeiculoFilter;
import br.edu.ifpb.SIGAV.repository.FabricanteRepository;
import br.edu.ifpb.SIGAV.repository.VeiculoRepository;
import br.edu.ifpb.SIGV.VeiculoService;

/**
 * 
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 *
 */
@Controller
@RequestMapping("/veiculos")
public class VeiculoController {
	
	@Autowired
	private VeiculoService veiculoService;
	
	@Autowired
	private FabricanteRepository fabricanteRepository;
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	/**
	 * 
	 * @param veiculo
	 * @return
	 */
	@RequestMapping("/novo")
	public ModelAndView novo(Veiculo veiculo){
		ModelAndView mv = new ModelAndView("veiculo/cadastro");
		mv.addObject("fabricantes", fabricanteRepository.findAll());
		mv.addObject("especies", EspecieVeiculo.values());
		mv.addObject("origins", Origin.values());
		return mv;
	}
	
	/**
	 * 
	 * @param veiculo
	 * @param result
	 * @param model
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView save(@Valid Veiculo veiculo, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			model.addAttribute(veiculo);
			return novo(veiculo);
		}

		veiculoService.save(veiculo);
		attributes.addFlashAttribute("message", "Veículo cadastrado com sucesso");
		return new ModelAndView("redirect:/veiculos/novo");
	}
	
	/**
	 * 
	 * @param veiculoFilter
	 * @param result
	 * @return
	 */
	@GetMapping
	public ModelAndView pesquisar(VeiculoFilter veiculoFilter, BindingResult result, 
			@PageableDefault(size=3) Pageable pageable, HttpServletRequest httpServletRequest){
		
		ModelAndView mv = new ModelAndView("veiculo/pesquisar");
		mv.addObject("fabricantes", fabricanteRepository.findAll());
		mv.addObject("especies", EspecieVeiculo.values());
		mv.addObject("origens", Origin.values());
		
		PageWrapper<Veiculo> page = new PageWrapper<>(veiculoRepository.filter(veiculoFilter, pageable), httpServletRequest);
		System.out.println(page.getTotalPages());
		mv.addObject("page", page);
		return mv;
	}
	
}
