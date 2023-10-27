package onl.andres.jsimplew.base.excp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalServerException extends ServiceException {

	private static final long serialVersionUID = 2937088215949192773L;

	private static Logger logger = LoggerFactory.getLogger(InternalServerException.class);

	public InternalServerException(Throwable cause) {
		super(cause);
		logger.error("original-cause: ", cause);
	}
}
