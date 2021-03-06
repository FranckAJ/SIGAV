package br.edu.ifpb.SIGAV.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.SIGAV.dto.FotoDTO;
import br.edu.ifpb.SIGV.storage.FotoStorage;
import br.edu.ifpb.SIGV.storage.FotoStorageRunnable;

/**
 * 
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 *
 */
@RestController
@RequestMapping("/fotos")
public class FotoController {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	
	/**
	 * 
	 * @param files
	 * @return
	 */
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files){
		
		DeferredResult<FotoDTO> result = new DeferredResult<>();
		
		Thread thread = new Thread(new FotoStorageRunnable(files, result, fotoStorage));
		thread.start();
		
		return result;
	}
	
	@GetMapping(value="/temp/{name:.*}")
	public byte[] getFotoTemp(@PathVariable String name){
		
		return fotoStorage.getFotoTemporaria(name);
		
	} 

	@GetMapping(value="/{name:.*}")
	public byte[] getFoto(@PathVariable String name){
		
		return fotoStorage.getFoto(name);
		
	}

}
