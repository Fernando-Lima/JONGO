package br.edu.unoesc.DAO;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDAO<T> implements GenericDAO<T> {
	protected MongoCollection mc;
	protected DB db;
	protected Jongo jongo;

	public void conectar() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDB("projeto");
		jongo = new Jongo(db);

	}

	@Override
	public void salvar(T entidade) {
		conectar();
		mc = jongo.getCollection(entidade.getClass().getName());
		mc.insert(entidade);
	}

	@Override
	public void excluir(T entidade, String campo, String valor) {
		conectar();
		mc = jongo.getCollection(entidade.getClass().getName());
		mc.remove("{" + campo + ":'" + valor + "'}");
	}

	@Override
	public void update(T entidade, String campo, String valor) {
		conectar();
		mc = jongo.getCollection(entidade.getClass().getName());
		mc.update("{" + campo + ":'" + valor + "'}").with(entidade);

	}

}
