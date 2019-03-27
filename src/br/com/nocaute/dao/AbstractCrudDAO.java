package br.com.nocaute.dao;

import br.com.nocaute.dao.contracts.Crud;
import br.com.nocaute.dao.contracts.Selectable;
import br.com.nocaute.model.contracts.Model;

public abstract class AbstractCrudDAO<T extends Model> extends AbstractDAO<T> implements Crud<T>, Selectable<T> {
}
