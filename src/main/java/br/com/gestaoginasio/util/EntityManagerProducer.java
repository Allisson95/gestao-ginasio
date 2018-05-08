package br.com.gestaoginasio.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;
	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("hibernate");

	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("gestao-ginasio",
				convertResourceBundleToMap(resourceBundle));
	}

	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return this.factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}

	private Map<String, String> convertResourceBundleToMap(ResourceBundle resourceBundle) {
		Map<String, String> map = new HashMap<>();

		resourceBundle.keySet().forEach(key -> {
			map.put(key, resourceBundle.getString(key));
		});

		return map;
	}

}