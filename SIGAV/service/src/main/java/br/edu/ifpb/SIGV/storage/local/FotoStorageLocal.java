package br.edu.ifpb.SIGV.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.SIGV.storage.FotoStorage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

/**
 * 
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 *
 */
public class FotoStorageLocal implements FotoStorage{
	
	private Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	
	private Path local;
	private Path localTemp;
	
	public FotoStorageLocal() {
		this(getDefault().getPath(System.getenv("HOME"), ".sigav"));
	}
	
	/**
	 * 
	 * @param path
	 */
	public FotoStorageLocal(Path path) {
		this.local = path;
		creatFolders();
	}


	/**
	 * 
	 */
	@Override
	public String saveLocalTemp(MultipartFile[] files) {
		String fileName = null;
		if(files != null && files.length > 0){
			MultipartFile file = files[0];
			
			fileName = this.generateFileName(file.getOriginalFilename());
			File dir = new File(this.localTemp.toAbsolutePath().toString() + 
							getDefault().getSeparator() + fileName);
			
			try {
				file.transferTo(dir);
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException("Erro ao salvar foto", e);			
			}
		}
		return fileName;
	}

	/**
	 * 
	 */
	@Override
	public byte[] getFotoTemporaria(String name) {
		try {
			return Files.readAllBytes(this.localTemp.resolve(name));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar foto temporaria", e);	
		}
	}
	
	@Override
	public byte[] getFoto(String foto) {
		try {
			return Files.readAllBytes(this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao recuperar foto", e);	
		}
	}
	
	/**
	 * Move para local e gera o thumbnail da foto salva.
	 */
	@Override
	public void save(String photo) {
		try {
			Files.move(this.localTemp.resolve(photo), this.local.resolve(photo));
			
		} catch (IOException e) {
			throw new RuntimeException("Erro ao mover foto para local", e);	
		}
		
		try {
			Thumbnails.of(this.local.resolve(photo).toString()).size(40, 29).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar thumbnail", e);
		}
	}
	
	/**
	 * Gera um ID aletório e concatena com nome;
	 * 
	 * @param originalName
	 * @return
	 */
	private String generateFileName(String originalName) {
		String newName = UUID.randomUUID().toString()+" - "+ originalName;
		
		return newName;
	}
	
	/**
	 * 
	 */
	private void creatFolders() {
		try {
			Files.createDirectories(this.local);
			this.localTemp = getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemp);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pastas criadas para salvar fotos.");
				LOGGER.debug("Pasta default: " + this.local.toAbsolutePath());
				LOGGER.debug("Pasta temporária: " + this.localTemp.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro criando pasta para salvar foto", e);
		}
	}
}
