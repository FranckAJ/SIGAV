package br.edu.ifpb.SIGAV.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.edu.ifpb.SIGAV.config.JPAConfig;
import br.edu.ifpb.SIGAV.config.WebConfig;
import br.edu.ifpb.SIGV.config.ServiceConfig;

/**
 * Configuração do Dispatcher servlet (Front Controller do spring)
 * 
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Configurações de conexão e transação do spring com BD
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class };
	}

	/**
	 * Mapeia controllers para o Dispather servlet
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	/**
	 * padrão de URL que será delegado para o Dispatcher servlet.
	 * 
	 * Mapeia para o Dispather servlet apartir do nome do projeto.
	 * 
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
		
        return new Filter[] { characterEncodingFilter };
	}
	
	/**
	 * Para rebimento de arquivos
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}

}
