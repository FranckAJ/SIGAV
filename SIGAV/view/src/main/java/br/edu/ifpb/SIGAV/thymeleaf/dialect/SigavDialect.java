package br.edu.ifpb.SIGAV.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import br.edu.ifpb.SIGAV.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import br.edu.ifpb.SIGAV.thymeleaf.processor.MessageElementTagProcessor;
import br.edu.ifpb.SIGAV.thymeleaf.processor.OrderElementTagProcessor;
import br.edu.ifpb.SIGAV.thymeleaf.processor.PaginationElementTagProcessor;


/**
 * 
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 *
 */
public class SigavDialect extends AbstractProcessorDialect{

	/**
	 * 
	 */
	public SigavDialect() {
		super("Faj Sigav", "sigav", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	/**
	 * 
	 */
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		
		final Set<IProcessor> processors = new HashSet<>();
		
		processors.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processors.add(new MessageElementTagProcessor(dialectPrefix));
		processors.add(new OrderElementTagProcessor(dialectPrefix));
		processors.add(new PaginationElementTagProcessor(dialectPrefix));
		
		return processors;
	}
}
