package com.it.media.renderer;

import java.io.File;

import com.it.hbean.DefaultPrototypeLoader;
import com.it.hbean.HBeanContainer;
import com.it.hbean.PrototypeLoader;

/**
 * Fichero de configuracion de HBean
 *  
 * @author Ricard Forner
 * @version 1.0
 */
public class HBeanConfiguration {

	public HBeanConfiguration() {
	}
	
	public void setRepositoryDir(String repositoryDir) {
		PrototypeLoader loader = new DefaultPrototypeLoader(new File(repositoryDir));
		com.it.hbean.Configuration configuration = new com.it.hbean.Configuration(null, loader, null);
		HBeanContainer.setConfiguration(configuration);
	}
}
