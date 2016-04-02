package br.edu.unoesc.DAO;

public interface GenericDAO<T> {

	public void salvar(T entidade);

	public void update(T entidade, String campo, String valor);

	public void excluir(T entidade, String campo, String valor);

}
